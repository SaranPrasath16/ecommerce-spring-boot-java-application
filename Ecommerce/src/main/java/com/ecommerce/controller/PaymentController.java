package com.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.OrderAddResponseDTO;
import com.ecommerce.services.user.UserImpl;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	private final UserImpl userImpl;

	public PaymentController(UserImpl userImpl) {
		this.userImpl = userImpl;
	}
	
    @PostMapping("/order")
    public ResponseEntity<OrderAddResponseDTO> placeOrder(@RequestBody String payload, @RequestHeader("X-Razorpay-Signature") String razorpaySignature) {
        System.out.println("Received Razorpay Webhook: " + payload);
        OrderAddResponseDTO orderResponse = userImpl.placeOrder(payload,razorpaySignature);
        HttpStatus status;
        if (!orderResponse.getSuccessfulOrders().isEmpty() && orderResponse.getFailedOrders().isEmpty()) {
            status = HttpStatus.OK;
        } else if (!orderResponse.getSuccessfulOrders().isEmpty()) {
            status = HttpStatus.PARTIAL_CONTENT;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(orderResponse);
    }

}
