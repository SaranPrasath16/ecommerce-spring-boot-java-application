package com.ecommerce.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.model.CartItems;

@Repository
public interface CartItemRepo extends MongoRepository<CartItems, String> {

}
