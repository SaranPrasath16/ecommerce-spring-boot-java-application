   package com.ecommerce.repo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.ecommerce.model.User;

@Repository
public interface UserRepo extends MongoRepository<User, String>{
    
	@Query("{ 'email' : ?0 }")
    User findByEmail(String email);
	
	@Query("{ '_id' : ?0 }")
	User findUserById(String id);
	
    @Query("{ 'isMainAdmin': true }")
    List<User> findSuperAdmins();
    
    @Query("{ 'isOrdersAdmin': true }")
    List<User> findOrdersAdmins();
    
    @Query("{ 'isProductAdmin': true }")
    List<User> findProductAdmins();


}

