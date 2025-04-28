package com.ecommerce.dto;

import java.util.List;

public class ProductUpdateRequestDTO {
    private String productId;
    private String productNewCategory;
    private String productNewName;
    private String productNewDescription;
    private double productNewPrice;
    private int productNewStock;
    private List<String> imagesToDelete;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductNewCategory() {
		return productNewCategory;
	}
	public void setProductNewCategory(String productNewCategory) {
		this.productNewCategory = productNewCategory;
	}
	public String getProductNewName() {
		return productNewName;
	}
	public void setProductNewName(String productNewName) {
		this.productNewName = productNewName;
	}
	public String getProductNewDescription() {
		return productNewDescription;
	}
	public void setProductNewDescription(String productNewDescription) {
		this.productNewDescription = productNewDescription;
	}
	public double getProductNewPrice() {
		return productNewPrice;
	}
	public void setProductNewPrice(double productNewPrice) {
		this.productNewPrice = productNewPrice;
	}
	public int getProductNewStock() {
		return productNewStock;
	}
	public void setProductNewStock(int productNewStock) {
		this.productNewStock = productNewStock;
	}
	public List<String> getImagesToDelete() {
		return imagesToDelete;
	}
	public void setImagesToDelete(List<String> imagesToDelete) {
		this.imagesToDelete = imagesToDelete;
	}
	public ProductUpdateRequestDTO(String productId, String productNewCategory, String productNewName,
			String productNewDescription, double productNewPrice, int productNewStock, List<String> imagesToDelete) {
		super();
		this.productId = productId;
		this.productNewCategory = productNewCategory;
		this.productNewName = productNewName;
		this.productNewDescription = productNewDescription;
		this.productNewPrice = productNewPrice;
		this.productNewStock = productNewStock;
		this.imagesToDelete = imagesToDelete;
	}
	public ProductUpdateRequestDTO() {
		super();
	}

}
