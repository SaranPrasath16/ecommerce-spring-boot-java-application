package com.ecommerce.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cart")
@TypeAlias("Cart")
public class Cart {
	@Id
    private String cartId;
    private List<CartItems> cartItems;
    private double totalAmount;

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public List<CartItems> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItems> cartItem) {
		this.cartItems = cartItem;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}


	public Cart() {
		super();
	}

	public Cart(String cartId, List<CartItems> cartItems, double totalAmount) {
		super();
		this.cartId = cartId;
		this.cartItems = cartItems;
		this.totalAmount = totalAmount;
	}

}
