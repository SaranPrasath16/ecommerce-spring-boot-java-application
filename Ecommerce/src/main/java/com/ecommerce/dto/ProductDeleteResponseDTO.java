package com.ecommerce.dto;

public class ProductDeleteResponseDTO {
    private String id;
    private String productMsg;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductMsg() {
		return productMsg;
	}
	public void setProductMsg(String productMsg) {
		this.productMsg = productMsg;
	}
	public ProductDeleteResponseDTO(String id, String productMsg) {
		super();
		this.id = id;
		this.productMsg = productMsg;
	}
	public ProductDeleteResponseDTO() {
		super();
	}
}
