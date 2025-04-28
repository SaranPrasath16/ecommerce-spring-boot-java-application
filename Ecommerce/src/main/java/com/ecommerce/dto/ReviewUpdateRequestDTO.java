package com.ecommerce.dto;

import java.util.List;

public class ReviewUpdateRequestDTO {
    private String reviewId;
    private double rating;
    private String comment;
    private List<String> userImageToDelete;

	public List<String> getUserImageToDelete() {
		return userImageToDelete;
	}
	public void setUserImageToDelete(List<String> userImageToDelete) {
		this.userImageToDelete = userImageToDelete;
	}
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
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
	public ReviewUpdateRequestDTO(String reviewId, double rating, String comment, List<String> userImageToDelete) {
		super();
		this.reviewId = reviewId;
		this.rating = rating;
		this.comment = comment;
		this.userImageToDelete = userImageToDelete;
	}
	public ReviewUpdateRequestDTO() {
		super();
	}

}
