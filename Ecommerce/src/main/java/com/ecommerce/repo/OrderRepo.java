package com.ecommerce.repo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.ecommerce.model.Orders;

@Repository
public interface OrderRepo extends MongoRepository<Orders, String> {
	
    @Query("{'userId' : ?0}")
    List<Orders> findByUserId(String userId);
    
    @Query(value = "{'_id' : ?0}", delete = true)
    long deleteByOrderId(String orderId);
    
    @Query("{'orderStatus': { $regex: ?0, $options: 'i' }}")
    List<Orders> findByStatus(String status);


}
