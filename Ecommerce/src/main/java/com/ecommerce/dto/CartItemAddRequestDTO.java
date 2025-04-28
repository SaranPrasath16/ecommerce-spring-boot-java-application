package com.ecommerce.dto;

public class CartItemAddRequestDTO {
    private String productId;
    private int quantity;
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
	public CartItemAddRequestDTO(String productId, int quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}
	public CartItemAddRequestDTO() {
		super();
	}

}
