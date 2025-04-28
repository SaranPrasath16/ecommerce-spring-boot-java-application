package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.middleware.AuthRequired;
import com.ecommerce.services.admin.OrderAdminImpl;

@RestController
@RequestMapping("/admin/ordersadmin")
public class OrdersAdminController {
	
	private final OrderAdminImpl orderAdminImpl;
		
    public OrdersAdminController(OrderAdminImpl orderAdminImpl) {
		super();
		this.orderAdminImpl = orderAdminImpl;
	}

	@GetMapping("/orders")
    @AuthRequired
    public ResponseEntity<OrderGetResponseDTO> getEntireOrders(){
        OrderGetResponseDTO orderGetResponseDTO = orderAdminImpl.getEntireOrders();
        return ResponseEntity.ok(orderGetResponseDTO);
    }
	
	@GetMapping("/order")
    @AuthRequired
    public ResponseEntity<OrderGetResponseDTO> getOrder(@RequestParam("order_Id") String orderId){
        OrderGetResponseDTO orderGetResponseDTO = orderAdminImpl.getOrder(orderId);
        return ResponseEntity.ok(orderGetResponseDTO);
    }
	
    @GetMapping("/orders-by-status")
    @AuthRequired
    public ResponseEntity<OrderGetResponseDTO> getOrdersByStatus(@RequestParam("Status") String status){
        OrderGetResponseDTO orderGetResponseDTO = orderAdminImpl.getOrdersByStatus(status);
        return ResponseEntity.ok(orderGetResponseDTO);
    }
    
    @PutMapping("/order")
    @AuthRequired
    public ResponseEntity<String> updateOrder(@RequestParam("order_Id") String orderId, @RequestParam("Status") String status){
        String msg = orderAdminImpl.updateOrder(orderId, status);
        return ResponseEntity.ok(msg);
    }

}
