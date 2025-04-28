package com.ecommerce.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ecommerce.util.IdGenerator;

@Document(collection = "reviews")
@TypeAlias("Review")
public class Review {
	@Id
	private String reviewId;
	private String userId;
	private String productId;
	private double rating;
	private String comment;
	private List<String> userImageUrls;

	public List<String> getUserImageUrls() {
		return userImageUrls;
	}
	public void setUserImageUrls(List<String> userImageUrls) {
		this.userImageUrls = userImageUrls;
	}
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
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
	public Review() {
		super();
        if (this.reviewId == null || this.reviewId.isEmpty()) {
            this.reviewId = "REVIEW_" + IdGenerator.generateRandomNumber();
        }
	}
	public Review(String userId, String productId, double rating, String comment, List<String> userImageUrls) {
		this();
		this.userId = userId;
		this.productId = productId;
		this.rating = rating;
		this.comment = comment;
		this.userImageUrls = userImageUrls;
	}



}
