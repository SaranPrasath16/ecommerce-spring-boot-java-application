package com.ecommerce.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Review;

@Repository
public interface ReviewRepo extends MongoRepository<Review, String>{
	
	@Query("{ 'productId' : ?0 }")
	List<Review> findByProductId(String productId);

	@Query("{ '_id' : ?0 }")
	Review findByReviewId(String reviewId);
	
	@Query(value = "{ '_id': ?0, 'userId': ?1 }", delete = true)
	long deleteByIdAndUserId(String reviewId, String userId);
	
	@Query("{'userId':?0,'productId':?1 }")
	Optional<Review> findByUserIdAndProductId(String userId, String productId);


}
