package com.ecommerce.dto;

import java.util.List;

public class ReviewGetResponseDTO {
	private String userName;
	private String productName;
	private double rating;
	private String comment;
	private List<String> userImageUrls;
	public List<String> getUserImageUrls() {
		return userImageUrls;
	}
	public void setUserImageUrls(List<String> userImageUrls) {
		this.userImageUrls = userImageUrls;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public ReviewGetResponseDTO(String userName, String productName, double rating, String comment,
			List<String> userImageUrls) {
		super();
		this.userName = userName;
		this.productName = productName;
		this.rating = rating;
		this.comment = comment;
		this.userImageUrls = userImageUrls;
	}
	public ReviewGetResponseDTO() {
		super();
	}

}
