package com.ecommerce.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Payment;

@Repository
public interface PaymentRepo extends MongoRepository<Payment, String>{
	
    @Query("{ 'razorPaymentLinkId' : ?0 }")
    Payment findByRazorPaymentLinkId(String razorPaymentLinkId);

    @Query("{ '_id' : ?0 }")
	Payment findByPaymentId(String paymentId);
	

}
