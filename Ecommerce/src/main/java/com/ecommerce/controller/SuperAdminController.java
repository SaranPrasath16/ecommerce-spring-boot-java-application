package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.dto.ReviewGetResponseDTO;
import com.ecommerce.middleware.AuthRequired;
import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.services.admin.AdminImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/superadmin")
public class SuperAdminController {
	
	private final AdminImpl adminImpl;

	public SuperAdminController(AdminImpl adminImpl) {
		super();
		this.adminImpl = adminImpl;
	}
	
    @GetMapping("/users")
    @AuthRequired
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        return ResponseEntity.ok(adminImpl.getAllUsers());
    }
    
    @DeleteMapping("/user")
    @AuthRequired
    public ResponseEntity<String> deleteUser(@RequestParam("user_Id") String userId, HttpServletRequest request) {
        return ResponseEntity.ok(adminImpl.deleteUser(userId));
    }
    
    @GetMapping("/user/reviews")
    @AuthRequired
    public ResponseEntity<List<ReviewGetResponseDTO>> getAllUserReviews(@RequestParam("user_Id") String userId, HttpServletRequest request) {
        return ResponseEntity.ok(adminImpl.getUserReviews(userId));
    }

    @GetMapping("/user/cart")
    @AuthRequired
    public ResponseEntity<Cart> getUserCart(@RequestParam("user_Id") String userId, HttpServletRequest request) {
        return ResponseEntity.ok(adminImpl.viewUserCart(userId));
    }

    @GetMapping("/user/orders")
    @AuthRequired
    public ResponseEntity<OrderGetResponseDTO> getUserOrders(@RequestParam("user_Id") String userId, HttpServletRequest request) {
        return ResponseEntity.ok(adminImpl.getUserOrders(userId));
    }

}
