package com.ecommerce.services.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.dto.ReviewGetResponseDTO;
import com.ecommerce.exceptionhandler.EntityDeletionException;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Orders;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.repo.CartRepo;
import com.ecommerce.repo.OrderRepo;
import com.ecommerce.repo.ProductRepo;
import com.ecommerce.repo.UserRepo;
import com.mongodb.client.result.DeleteResult;

@Service
public class AdminImpl {
	
	private final UserRepo userRepo;
	private final ProductRepo productRepo;
	private final CartRepo cartRepo;
	private final OrderRepo orderRepo;
	private final MongoTemplate mongoTemplate;


	public AdminImpl(UserRepo userRepo, ProductRepo productRepo, CartRepo cartRepo, OrderRepo orderRepo,
			MongoTemplate mongoTemplate) {
		super();
		this.userRepo = userRepo;
		this.productRepo = productRepo;
		this.cartRepo = cartRepo;
		this.orderRepo = orderRepo;
		this.mongoTemplate = mongoTemplate;
	}

	public List<User> getAllUsers() {
        List<User> userList = userRepo.findAll();
        if(!userList.isEmpty()){
            return userList;
        }
        throw new ResourceNotFoundException("No User found.");
    }

    public String deleteUser(String userId) {
        Query query = new Query(Criteria.where("_id").is(userId));
        DeleteResult deleteResult = mongoTemplate.remove(query, User.class);
        if(deleteResult.getDeletedCount()>0){
            return "Successfully deleted the User";
        }
        throw new EntityDeletionException("Failed to delete user with given credentials");
    }

	public List<ReviewGetResponseDTO> getUserReviews(String userId) {

	    Query query = new Query(Criteria.where("userId").is(userId));
	    List<Review> reviewList = mongoTemplate.find(query, Review.class);

       if(!reviewList.isEmpty()){
        	  return reviewList.stream().map(review -> {
        	        User user = userRepo.findById(review.getUserId())
        	                .orElseThrow(() -> new ResourceNotFoundException("User not found for ID: " + review.getUserId()));

        	        Product product = productRepo.findById(review.getProductId())
        	                .orElseThrow(() -> new ResourceNotFoundException("Product not found for ID: " + review.getProductId()));

        	        return new ReviewGetResponseDTO(
        	                user.getUserName(),
        	                product.getProductName(),
        	                review.getRating(),
        	                review.getComment(),
        	                review.getUserImageUrls()
        	        );
        	    }).collect(Collectors.toList());
        }
        throw new ResourceNotFoundException("Reviews not found for the User.");
	}

	public Cart viewUserCart(String userId) {
        Optional<Cart> optionalCart = Optional.ofNullable(cartRepo.findByCartId(userId));
        if(optionalCart.isPresent()){
            return optionalCart.get();
        }
        throw new ResourceNotFoundException("Cart not found");
	}

	public OrderGetResponseDTO getUserOrders(String userId) {
        List<Orders> orders = orderRepo.findByUserId(userId);
        if(orders.isEmpty()){
            throw new ResourceNotFoundException("No orders found!");
        }
        OrderGetResponseDTO orderGetResponseDTO = new OrderGetResponseDTO();
        orderGetResponseDTO.setOrderList(orders);
        return orderGetResponseDTO;
	}
	

}
