package com.ecommerce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dto.CartItemAddRequestDTO;
import com.ecommerce.dto.CartItemUpdateRequestDTO;
import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.dto.ReviewRequestDTO;
import com.ecommerce.dto.ReviewUpdateRequestDTO;
import com.ecommerce.middleware.AuthRequired;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Orders;
import com.ecommerce.services.user.UserImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
	private final UserImpl userImpl;

	public UserController(UserImpl userImpl) {
		this.userImpl = userImpl;
	}
	
    @GetMapping("/homepage")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getHomePage(){
        List<ProductGetResponseDTO> productList = userImpl.getHomePage();
        return  ResponseEntity.ok(productList);
    }
    
    @GetMapping("/cart")
    @AuthRequired
    public ResponseEntity<Cart> getBuyerCart(HttpServletRequest request){
    	Cart cart = userImpl.getCart();
        return ResponseEntity.ok(cart);
    }
    
    @PostMapping("/cart")
    @AuthRequired
    public ResponseEntity<String> addProductToCart(@RequestBody CartItemAddRequestDTO cartItemAddRequestDTO){
        String msg = userImpl.addProductToCart(cartItemAddRequestDTO);
        return ResponseEntity.ok(msg);
    }
    
    @PutMapping("/cart")
    @AuthRequired
    public ResponseEntity<String> updateCartItem(@RequestBody CartItemUpdateRequestDTO cartItemUpdateRequestDTO){
        String msg = userImpl.updateCartItem(cartItemUpdateRequestDTO);
        return ResponseEntity.ok(msg);
    }
    @DeleteMapping("/cart")
    @AuthRequired
    public ResponseEntity<String> deleteCartItem(@RequestParam("product_Id") String productId){
        String msg = userImpl.deleteCartItem(productId);
        return ResponseEntity.ok(msg);
    }
    
    @PostMapping("/checkout")
    @AuthRequired
    public ResponseEntity<Map<String, Object>> checkout() {
        Map<String, Object> checkoutResponse = userImpl.checkout();
		@SuppressWarnings("unchecked")
		List<String> outOfStockItems = (List<String>) checkoutResponse.get("outOfStockItems");
        String message = (String) checkoutResponse.get("message");
        HttpStatus status;
        if (outOfStockItems.isEmpty()) {
            status = HttpStatus.OK;
        } 
        else {
            status = HttpStatus.PARTIAL_CONTENT;
        }
        checkoutResponse.put("message", message);
        return ResponseEntity.status(status).body(checkoutResponse);
    }
        
    @GetMapping("/orders")
    @AuthRequired
    public ResponseEntity<OrderGetResponseDTO> getAllOrders(){
        OrderGetResponseDTO orderGetResponseDTO = userImpl.getAllOrders();
        return ResponseEntity.ok(orderGetResponseDTO);
    }
    
    @GetMapping("/order")
    @AuthRequired
    public ResponseEntity<Orders> getOrder(@RequestParam("order_Id") String orderId){
    	Orders order = userImpl.getSpecificOrder(orderId);
        return ResponseEntity.ok(order);
    }
    
    @DeleteMapping("/order")
    @AuthRequired
    public ResponseEntity<String> deleteOrder(@RequestParam("order_Id") String orderId){
        String msg = userImpl.deleteOrder(orderId);
        return ResponseEntity.ok(msg);
    }
    
    @GetMapping("/search/name")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductByName(@RequestParam("product_Name") String productName){
    	List<ProductGetResponseDTO> productGetResponseDTO = userImpl.getProductByName(productName);
        return ResponseEntity.ok(productGetResponseDTO);
    }
    
    @GetMapping("/search/category")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductsByCategory(@RequestParam("product_Category") String category){
    	List<ProductGetResponseDTO> productGetResponseDTO = userImpl.getProductByCategory(category);
        return ResponseEntity.ok(productGetResponseDTO);
    }
    
    @GetMapping("/search/name-category")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductByNameAndCategory(@RequestParam("product_Name") String name, @RequestParam("product_Category") String category){
    	List<ProductGetResponseDTO> productGetResponseDTO = userImpl.getProductByNameAndCategory(name, category);
        return ResponseEntity.ok(productGetResponseDTO);
    }
    
    @GetMapping("/sort/byprice/high-to-low")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductByPriceHighToLow(@RequestBody List<ProductGetResponseDTO> productList){
    	List<ProductGetResponseDTO> productGetResponseDTO = userImpl.getProductByPriceHighToLow(productList);
        return ResponseEntity.ok(productGetResponseDTO);
    }

    @GetMapping("/sort/byprice/low-to-high")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductByPriceLowToHigh(@RequestBody List<ProductGetResponseDTO> productList){
    	List<ProductGetResponseDTO> productGetResponseDTO = userImpl.getProductByPriceLowToHigh(productList);
        return ResponseEntity.ok(productGetResponseDTO);
    }
    
    @GetMapping("/filter/price")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductByPriceRange(@RequestParam("min_Price") double minPrice, @RequestParam("max_Price") double maxPrice){
    	List<ProductGetResponseDTO> productGetResponseDTO = userImpl.getProductByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(productGetResponseDTO);
    }
    
    @GetMapping("/product/review")
    @AuthRequired
    public ResponseEntity<Object> getProductReviews(@RequestParam("product_Id") String productId){
    	Object reviewList = userImpl.getProductReviews(productId);
        return ResponseEntity.ok(reviewList);
    }
    
    @PostMapping(value = "/product/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthRequired
    public ResponseEntity<String> addReview(
            @RequestParam("product_Id") String productId,
            @RequestParam("user_Rating") double rating,
            @RequestParam("user_Comment") String comment,
            @RequestParam(value = "user_Image_Urls", required = false) MultipartFile[] userImageUrls,
            HttpServletRequest request) {

        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setProductId(productId);
        reviewRequestDTO.setRating(rating);
        reviewRequestDTO.setComment(comment);

        String msg = userImpl.addProductReviews(reviewRequestDTO, userImageUrls);
        return ResponseEntity.ok(msg);
    }

    
    @PutMapping(value = "/product/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthRequired
    public ResponseEntity<String> updateReview(
            @RequestParam("review_Id") String reviewId,
            @RequestParam("user_Rating") double rating,
            @RequestParam("user_Comment") String comment,
            @RequestParam(value = "user_Image_To_Delete", required = false) List<String> userImageToDelete,
            @RequestParam(value = "user_Image_Urls", required = false) MultipartFile[] userImageUrls) {

        ReviewUpdateRequestDTO reviewUpdateRequestDTO = new ReviewUpdateRequestDTO();
        reviewUpdateRequestDTO.setReviewId(reviewId);
        reviewUpdateRequestDTO.setRating(rating);
        reviewUpdateRequestDTO.setComment(comment);
        reviewUpdateRequestDTO.setUserImageToDelete(userImageToDelete);

        String msg = userImpl.updateProductReviews(reviewUpdateRequestDTO, userImageUrls);
        return ResponseEntity.ok(msg);
    }

    
    @DeleteMapping("/product/review")
    @AuthRequired
    public ResponseEntity<String> deleteReview(@RequestParam("review_Id") String reviewId){
        String msg = userImpl.deleteProductReviews(reviewId);
        return ResponseEntity.ok(msg);
    }
}
