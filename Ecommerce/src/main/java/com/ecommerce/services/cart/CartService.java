package com.ecommerce.services.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.CartItemAddRequestDTO;
import com.ecommerce.dto.CartItemUpdateRequestDTO;
import com.ecommerce.exceptionhandler.EntityCreationException;
import com.ecommerce.exceptionhandler.EntityDeletionException;
import com.ecommerce.exceptionhandler.EntityPushException;
import com.ecommerce.exceptionhandler.EntityUpdationException;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.middleware.JwtAspect;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItems;
import com.ecommerce.model.Product;
import com.ecommerce.repo.CartRepo;
import com.ecommerce.repo.ProductRepo;
import com.mongodb.client.result.UpdateResult;

@Service
public class CartService {
	
    private final CartRepo cartRepo;
    private final CartItemService cartItemService;
    private final MongoTemplate mongoTemplate;
    private final ProductRepo productRepo;

	public CartService(CartRepo cartRepo, CartItemService cartItemService, MongoTemplate mongoTemplate,
			ProductRepo productRepo) {
		super();
		this.cartRepo = cartRepo;
		this.cartItemService = cartItemService;
		this.mongoTemplate = mongoTemplate;
		this.productRepo = productRepo;
	}

	public Cart getCart(String cartId) {
        if (cartId.isEmpty() || cartId ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }
        Optional<Cart> optionalCart = Optional.ofNullable(cartRepo.findByCartId(cartId));
        if(optionalCart.isPresent()){
            return optionalCart.get();
        }
        throw new ResourceNotFoundException("Cart not found");
    }

	public void addCartId(String userId) {
        Cart cart = new Cart(userId, new ArrayList<CartItems>(), 0);
        Optional<Cart> optionalCart = Optional.of(cartRepo.save(cart));

        if(optionalCart.isPresent()){
            System.out.println("Cart created");
            return;
        }

        throw new EntityCreationException("Error creating the cart");
		
	}

	public String addProduct(CartItemAddRequestDTO cartItemAddRequestDTO) {
        String cartId = JwtAspect.getCurrentUserId();
        if (cartId.isEmpty() || cartId ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }
        
        String productId = cartItemAddRequestDTO.getProductId();
        Product product=productRepo.findProductById(productId);
        int quantity = cartItemAddRequestDTO.getQuantity();
        double price = product.getProductPrice();
        String name = product.getProductName();
        
        CartItems cartItemsModel = cartItemExistsOrNot(cartId, productId);
        if(cartItemsModel != null) {
            quantity += cartItemsModel.getQuantity();
            price = quantity * price;
            Query query = new Query(Criteria.where("_id").is(cartId).and("cartItems.productId").is(productId));
            Update update = new Update();
            update.set("cartItems.$.quantity", quantity);
            update.set("cartItems.$.price", price);
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Cart.class);
            if(updateResult.getModifiedCount() > 0) {
                updateCartPrice(cartId);
                return "Product added successfully in cart";
            }
            throw new EntityPushException("Failed to add product in cart");
        }
        cartItemsModel = cartItemService.productToCartItem(name, productId, price, quantity);
        Query query = new Query(Criteria.where("_id").is(cartId));
        Update update = new Update();
        update.addToSet("cartItems", cartItemsModel);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Cart.class);
        if(updateResult.getModifiedCount() > 0) {
            updateCartPrice(cartId);
            return "Product added successfully in cart";
        }
        throw new EntityPushException("Failed to add product in cart");
    }
	
	private CartItems cartItemExistsOrNot(String cartId, String productId) {
        Query query = new Query(Criteria.where("_id").is(cartId).and("cartItems.productId").is(productId));
        Cart cart = mongoTemplate.findOne(query, Cart.class);
        if(cart != null && cart.getCartItems() != null) { 
            for(CartItems item : cart.getCartItems()) {
                if(item.getProductId().equals(productId)) {
                    return item;
                }
            }
        }
        return null;
    }
	
    private void updateCartPrice(String cartId){
        Query query = new Query(Criteria.where("_id").is(cartId));
        Cart cart = mongoTemplate.findOne(query, Cart.class);

        double totalAmount = cart.getCartItems().stream()
                .filter(CartItems::isSelectedForPayment)
                .mapToDouble(CartItems::getPrice)
                .sum();

        query = new Query(Criteria.where("_id").is(cartId));
        Update update = new Update();
        update.set("totalAmount", totalAmount);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Cart.class);
        if(updateResult.wasAcknowledged()){
            System.out.println("Updated cart price successfully in CartService.updateCartPrice");
            return;
        }

        throw new EntityUpdationException("Failed to update cart price");
    }

    public String updateCartItem(CartItemUpdateRequestDTO cartItemUpdateRequestDTO) {

        String cartId = JwtAspect.getCurrentUserId();
        if (cartId.isEmpty() || cartId ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }

        String productId = cartItemUpdateRequestDTO.getProductId();
        Product product=productRepo.findProductById(productId);
        double price = product.getProductPrice();
        boolean selection = cartItemUpdateRequestDTO.isSelectedForPayment();
        int quantity = cartItemUpdateRequestDTO.getQuantity();

        CartItems cartItem = cartItemExistsOrNot(cartId, productId);

        if (cartItem != null) {
            Query query = new Query(Criteria.where("_id").is(cartId).and("cartItems.productId").is(productId));
            Update update = new Update();
            update.set("cartItems.$.quantity", quantity);
            update.set("cartItems.$.price", quantity*price);
            update.set("cartItems.$.selectedForPayment", selection);

            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Cart.class);

            if (updateResult.getModifiedCount() > 0) {
                updateCartPrice(cartId);
                return "Successfully updated item in cart";
            } else {
                throw new EntityUpdationException("Error Updating item in cart");
            }
        }
        throw new ResourceNotFoundException("Cart Item doesnt exist");
    }

    public String deleteCartItem(String productId) {
        String cartId = JwtAspect.getCurrentUserId();
        if (cartId.isEmpty() || cartId ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }

        CartItems cartItem = cartItemExistsOrNot(cartId, productId);
        System.out.println(cartItem);
        if(cartItem != null) {
            Query query = new Query(Criteria.where("_id").is(cartId));

            Query pullQuery = new Query();
            pullQuery.addCriteria(Criteria.where("productId").is(cartItem.getProductId()));
            pullQuery.addCriteria(Criteria.where("quantity").is(cartItem.getQuantity()));
            pullQuery.addCriteria(Criteria.where("price").is(cartItem.getPrice()));

            Update update = new Update().pull("cartItems", pullQuery.getQueryObject());

            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Cart.class);
            if(updateResult.getModifiedCount() > 0){
                updateCartPrice(cartId);
                return "Item deleted successfully";
            }
            throw new EntityDeletionException("Error deleting cart item");
        }
        throw new ResourceNotFoundException("Cart item not found in cart");
    }
    
    public List<CartItems> getCartItems(String cartId){
        Cart cart = cartRepo.findById(cartId).orElseThrow(()->new RuntimeException("Error fetching the cart"));
        
        return cart.getCartItems()
                .stream()
                .filter(CartItems::isSelectedForPayment)
                .collect(Collectors.toList());
    }
    
    public void deleteSelectedCartItems(String cartId, List<CartItems> cartItems) {
        if (cartItems.isEmpty()) {
            return;
        }

        List<String> productIds = cartItems.stream()
                .map(cartItem -> cartItem.getProductId())
                .collect(Collectors.toList());
        
        Query query = new Query(Criteria.where("_id").is(cartId));
        Update update = new Update().pull("cartItems", Query.query(Criteria.where("productId").in(productIds)).getQueryObject());
        UpdateResult result = mongoTemplate.updateFirst(query, update, Cart.class);
        
        if (result.getMatchedCount() == 0) {
            throw new EntityDeletionException("Cart not found");
        }

        if (result.getModifiedCount() > 0) {
            System.out.println("Selected items deleted successfully");
            updateCartPrice(cartId);
        } 
        else {
            throw new EntityDeletionException("No matching items found in the cart for deletion");
        }
    }

}
