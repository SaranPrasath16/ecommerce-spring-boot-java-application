package com.ecommerce.dto;

public class CartItemUpdateRequestDTO {

    private String productId;
    private int quantity;
    private boolean selectedForPayment;
	public boolean isSelectedForPayment() {
		return selectedForPayment;
	}
	public void setSelectedForPayment(boolean selectedForPayment) {
		this.selectedForPayment = selectedForPayment;
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
	public CartItemUpdateRequestDTO(String productId, int quantity, boolean selectedForPayment) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.selectedForPayment = selectedForPayment;
	}
	public CartItemUpdateRequestDTO() {
		super();
	}

}
