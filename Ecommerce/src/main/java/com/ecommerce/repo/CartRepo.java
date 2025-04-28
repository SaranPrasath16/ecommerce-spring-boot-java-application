package com.ecommerce.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.ecommerce.model.Cart;

@Repository
public interface CartRepo extends MongoRepository<Cart, String> {
	
    @Query("{'_id' : ?0}")
    Cart findByCartId(String CartId);
	
	
}
