package com.ecommerce.services.admin;


import org.springframework.stereotype.Service;

import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.services.order.OrderService;

@Service
public class OrderAdminImpl {
	
	private final OrderService orderService;

	public OrderAdminImpl(OrderService orderService) {
		super();
		this.orderService = orderService;
	}

	public OrderGetResponseDTO getEntireOrders() {
		return orderService.getEntireOrders();
	}

	public OrderGetResponseDTO getOrder(String orderId) {
		return orderService.getOrder(orderId);
	}

	public OrderGetResponseDTO getOrdersByStatus(String status) {
		return orderService.getOrderByStatus(status);
	}

	public String updateOrder(String orderId, String status) {
		return orderService.updateOrders(orderId, status);
	}
}
