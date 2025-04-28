package com.ecommerce.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.ecommerce.model.Product;

@Repository
public interface ProductRepo extends MongoRepository<Product, String>{
	
    @Query("{'productPrice':{$gte : ?0, $lte : ?1}}")
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    
    @Query("{ 'productName' : ?0 }")
    Product findByProductName(String productName);
    
    @Query("{ '_id' : ?0 }")
    Product findProductById(String id);


}
