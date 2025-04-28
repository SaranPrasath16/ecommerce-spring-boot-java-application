package com.ecommerce.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ecommerce.util.IdGenerator;

@Document(collection="product")
@TypeAlias("Product")
public class Product {
	@Id
	private String productId;
	private String category;
	private String productName;
	private String productDescription;
	private double productPrice;
	private int noOfStocks;
	private List<String> imageUrls;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategoryId(String category) {
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
	public List<String> getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}
	public Product() {
		super();
        if (this.productId == null || this.productId.isEmpty()) {
            this.productId = "PRODUCT_" + IdGenerator.generateRandomNumber();
        }
	}
	public Product(String category, String productName, String productDescription, double productPrice,
			int noOfStocks, List<String> imageUrls) {
		this();
		this.category = category;
		this.productName = productName;
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.noOfStocks = noOfStocks;
		this.imageUrls = imageUrls;
	}
	

}
