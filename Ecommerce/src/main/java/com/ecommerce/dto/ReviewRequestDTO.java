package com.ecommerce.dto;

public class ReviewRequestDTO {
    private String productId;
    private double rating;
    private String comment;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
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

	public ReviewRequestDTO(String productId, double rating, String comment) {
		super();
		this.productId = productId;
		this.rating = rating;
		this.comment = comment;
	}
	public ReviewRequestDTO() {
		super();
	}

}
