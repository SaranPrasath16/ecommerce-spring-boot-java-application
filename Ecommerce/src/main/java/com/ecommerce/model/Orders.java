package com.ecommerce.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ecommerce.util.IdGenerator;

@Document(collection="orders")
@TypeAlias("Orders")
public class Orders {
	@Id
	private String orderId;
	private String userId;
	private List<CartItems> cartItems;
	private double totalAmount;
	private int noOfItems;
	private String paymentId;
	private String paymentStatus;
	private String orderStatus;
	private String orderDateTime;
	
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<CartItems> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<CartItems> cartItems) {
		this.cartItems = cartItems;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getNoOfItems() {
		return noOfItems;
	}
	public void setNoOfItems(int noOfItems) {
		this.noOfItems = noOfItems;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public Orders(String userId, List<CartItems> cartItems, double totalAmount, int noOfItems, String paymentId,
			String paymentStatus, String orderStatus, String orderDateTime) {
		this();
		this.userId = userId;
		this.cartItems = cartItems;
		this.totalAmount = totalAmount;
		this.noOfItems = noOfItems;
		this.paymentId = paymentId;
		this.paymentStatus = paymentStatus;
		this.orderStatus = orderStatus;
		this.orderDateTime = orderDateTime;
	}
	public Orders() {
        if (this.orderId == null || this.orderId.isEmpty()) {
            this.orderId = "ORDER_" + IdGenerator.generateRandomNumber();
        }
	}
	
}