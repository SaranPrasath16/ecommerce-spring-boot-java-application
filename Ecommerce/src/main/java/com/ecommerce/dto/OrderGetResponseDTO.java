package com.ecommerce.dto;

import java.util.List;
import com.ecommerce.model.Orders;

public class OrderGetResponseDTO {
    private List<Orders> orderList;

	public List<Orders> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Orders> orderList) {
		this.orderList = orderList;
	}

	public OrderGetResponseDTO(List<Orders> orderList) {
		super();
		this.orderList = orderList;
	}

	public OrderGetResponseDTO() {
		super();
	}

}
