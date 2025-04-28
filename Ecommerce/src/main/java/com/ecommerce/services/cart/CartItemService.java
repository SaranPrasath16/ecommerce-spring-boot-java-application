package com.ecommerce.services.cart;

import org.springframework.stereotype.Service;
import com.ecommerce.model.CartItems;

@Service
public class CartItemService {
    
    public CartItems productToCartItem(String name, String productId, double price, int quantity){
    	CartItems cartItemsModel = new CartItems();
 
        price = price * quantity;
        cartItemsModel.setName(name);
        cartItemsModel.setProductId(productId);
        cartItemsModel.setQuantity(quantity);
        cartItemsModel.setPrice(price);
        cartItemsModel.setSelectedForPayment(true);

        return cartItemsModel;
    }

}
