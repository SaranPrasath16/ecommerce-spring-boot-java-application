package com.ecommerce.model;

public class CartItems {
	private String name;
	private String productId;
	private int quantity;
	private double price;
	private boolean selectedForPayment;
	
	public boolean isSelectedForPayment() {
		return selectedForPayment;
	}
	public void setSelectedForPayment(boolean selectedForPayment) {
		this.selectedForPayment = selectedForPayment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public CartItems(String name, String productId, int quantity, double price,
			boolean selectedForPayment) {
		super();
		this.name = name;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.selectedForPayment = selectedForPayment;
	}
	public CartItems() {
		super();
	}

}
