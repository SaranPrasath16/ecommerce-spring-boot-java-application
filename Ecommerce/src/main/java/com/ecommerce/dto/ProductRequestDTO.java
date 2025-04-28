package com.ecommerce.dto;

public class ProductRequestDTO {
    private String category;
    private String productName;
    private String productDescription;
    private double productPrice;
    private int noOfStocks;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public int getNoOfStocks() {
		return noOfStocks;
	}
	public void setNoOfStocks(int noOfStocks) {
		this.noOfStocks = noOfStocks;
	}
	public ProductRequestDTO(String category, String productName, String productDescription, double productPrice,
			int noOfStocks) {
		super();
		this.category = category;
		this.productName = productName;
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.noOfStocks = noOfStocks;
	}
	public ProductRequestDTO() {
		super();
	}


}
