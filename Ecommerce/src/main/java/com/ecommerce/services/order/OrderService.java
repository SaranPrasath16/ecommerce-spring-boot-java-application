package com.ecommerce.services.order;

import java.io.ByteArrayInputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.ecommerce.dto.OrderAddResponseDTO;
import com.ecommerce.dto.OrderGetResponseDTO;
import com.ecommerce.exceptionhandler.EntityDeletionException;
import com.ecommerce.exceptionhandler.EntityUpdationException;
import com.ecommerce.exceptionhandler.InvalidInputException;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.exceptionhandler.UnauthorizedException;
import com.ecommerce.middleware.JwtAspect;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItems;
import com.ecommerce.model.Orders;
import com.ecommerce.model.Payment;
import com.ecommerce.model.User;
import com.ecommerce.repo.OrderRepo;
import com.ecommerce.repo.PaymentRepo;
import com.ecommerce.repo.UserRepo;
import com.ecommerce.services.cart.CartService;
import com.ecommerce.services.email.EmailService;
import com.ecommerce.services.product.ProductService;
import com.ecommerce.services.razorpay.RazorpayService;
import com.ecommerce.util.InvoicePdfGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.UpdateResult;

@Service
public class OrderService {
	
	private final CartService cartService;
	private final ProductService productService;
	private final OrderRepo orderRepo;
	private final PaymentRepo paymentRepo;
	private final RazorpayService razorpayService;
	private final UserRepo userRepo;
	private final EmailService emailService;
	private final MongoTemplate mongotemplate;
	private final InvoicePdfGenerator invoicePdfGenerator;
	private String paymentId;
	
	public OrderService(CartService cartService, ProductService productService, OrderRepo orderRepo,
			PaymentRepo paymentRepo, RazorpayService razorpayService, UserRepo userRepo, EmailService emailService,
			MongoTemplate mongotemplate, InvoicePdfGenerator invoicePdfGenerator) {
		super();
		this.cartService = cartService;
		this.productService = productService;
		this.orderRepo = orderRepo;
		this.paymentRepo = paymentRepo;
		this.razorpayService = razorpayService;
		this.userRepo = userRepo;
		this.emailService = emailService;
		this.mongotemplate = mongotemplate;
		this.invoicePdfGenerator = invoicePdfGenerator;
	}

	public Map<String, Object> checkout() {

	    String cartId = JwtAspect.getCurrentUserId();
	    if (cartId == null || cartId.isEmpty()) {
	        throw new ResourceNotFoundException("User ID not found in JWT token.");
	    }

	    User user = userRepo.findUserById(cartId);
	    List<CartItems> cartItems = cartService.getCartItems(cartId);
	    if (cartItems.isEmpty()) {
	        throw new ResourceNotFoundException("No items found in cart");
	    }
	    List<String> outOfStock = new ArrayList<>();
	    List<CartItems> outOfStockItems = new ArrayList<>();
	    ZonedDateTime nowInIST = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String paymentDateTime = nowInIST.format(formatter);

	    double finalAmount = 0.0;
	    List<CartItems> itemsToProcess = new ArrayList<>();
	    for (CartItems item : cartItems) {
	        int availableStock = productService.getAvailableStock(item.getProductId());
	        if (item.getQuantity() > availableStock) {
	            outOfStock.add(item.getName());
	            outOfStockItems.add(item);
	        } else {
	            finalAmount += item.getPrice();
	            itemsToProcess.add(item);
	        }
	    }
	    if (itemsToProcess.isEmpty()) {
	        throw new ResourceNotFoundException("All items are out of stock. No payment link generated.");
	    }
	    try {
	        Map<String, String> paymentLinkData = razorpayService.generateInvoice(user.getUserName(), user.getEmail(),finalAmount);
	        String paymentLinkUrl = paymentLinkData.get("short_url");

	        Payment payment = new Payment(
	                cartId,
	                null,
	                null,
	                null,
	                null,
	                null,
	                "INITIATED",
	                paymentDateTime
	        );
	        paymentId=payment.getPaymentId();
	        paymentRepo.save(payment);
	        emailService.sendPaymentLink(user.getEmail(), user.getUserName(), paymentLinkUrl);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Payment link generation failed.");
	    }
	    cartService.deleteSelectedCartItems(cartId, outOfStockItems);
	    Map<String, Object> response = new HashMap<>();
	    response.put("outOfStockItems", outOfStock);
	    response.put("message", "Payment link sent to your registered email. Complete the payment to confirm the order.");

	    return response;
	}

	public OrderGetResponseDTO getAllOrders() {
        String userId = JwtAspect.getCurrentUserId();
        if (userId.isEmpty() || userId ==null) {
            throw new ResourceNotFoundException("User ID not found in JWT token.");
        }

        OrderGetResponseDTO orderGetResponseDTO = new OrderGetResponseDTO(orderRepo.findByUserId(userId));
        return orderGetResponseDTO;
	}

	public Orders getSpecificOrder(String orderId) {
        Optional<Orders> order = orderRepo.findById(orderId);
        if (JwtAspect.getCurrentUserId().equals(order.get().getUserId())) {
	        if(order.isPresent()){
	            return order.get();
	        }
	        throw new ResourceNotFoundException("Failed to fetch order with specified credentials.");
        }
        throw new UnauthorizedException("You are not authorized to view this order.");
	}

	public String deleteOrder(String orderId) {
        Optional<Orders> order = orderRepo.findById(orderId);
        if(order.isEmpty()){
            throw new ResourceNotFoundException("Failed to fetch order with provided credentials.");
        }
        if (JwtAspect.getCurrentUserId().equals(order.get().getUserId())) {
            if (order.get().getOrderStatus().equals("PENDING")) {
                long i = orderRepo.deleteByOrderId(orderId);
                if (i > 0) {
                    return "Order deleted successfully.";
                }
                throw new EntityDeletionException("Failed to delete the order.");
            }
            throw new InvalidInputException("Invalid order status provided.");
        }
        throw new UnauthorizedException("You are not authorized to delete this order.");
	}
	
	public OrderAddResponseDTO placeOrder(String payloadBody, String razorpaySignature) {
	    boolean isSignatureValid = razorpayService.verifyRazorpayWebhookSignature(payloadBody, razorpaySignature);
	    if (!isSignatureValid) {
	        throw new SecurityException("Invalid Razorpay signature. Possible tampering detected.");
	    }
	    Map<String, Object> payload = convertJsonToMap(payloadBody);
	    String event = (String) payload.get("event");
	    if ("payment_link.paid".equals(event)) {
	        System.out.println("Ignoring payment_link.paid event");
	        throw new UnauthorizedException("Ignoring payment_link.paid event");
	    }
	    Map<String, Object> payloadMap = (Map<String, Object>) payload.get("payload");
	    Map<String, Object> paymentWrapper = (Map<String, Object>) payloadMap.get("payment");
	    Map<String, Object> paymentEntity = (paymentWrapper != null) ? (Map<String, Object>) paymentWrapper.get("entity") : null;
	    if (paymentEntity == null) {
	        throw new IllegalStateException("Payment entity is missing");
	    }
	    Map<String, Object> acquirerData = (Map<String, Object>) paymentEntity.get("acquirer_data");
	    String bankTransactionId = (acquirerData != null) ? (String) acquirerData.get("bank_transaction_id") : null;
	    String paymentStatus = (String) paymentEntity.get("status");
	    String razorOrderId = (String) paymentEntity.get("order_id");
	    String razorInvoiceId = (String) paymentEntity.get("invoice_id");
	    String razorPaymentId = (String) paymentEntity.get("id");
	    String paymentMethod = (String) paymentEntity.get("method");

	    if (paymentId == null || paymentId.isEmpty()) {
	        throw new ResourceNotFoundException("Payment Id is not found");
	    }
	    Payment payment = paymentRepo.findByPaymentId(paymentId);
	    if (payment == null) {
	        throw new ResourceNotFoundException("No payment found with Payment ID: " + paymentId);
	    }
	    payment.setRazorOrderId(razorOrderId);
	    payment.setRazorPaymentId(razorPaymentId);
	    payment.setRazorInvoiceId(razorInvoiceId);
	    payment.setPaymentStatus(paymentStatus);
	    payment.setTranscationId(bankTransactionId);
	    payment.setPaymentMethod(paymentMethod);
	    paymentRepo.save(payment);
	    
	    if ("failed".equalsIgnoreCase(paymentStatus)) {
	        throw new UnauthorizedException("Ignoring webhook because payment is not captured yet. Status: " + paymentStatus);
	    }

	    String userId = payment.getUserId();
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    Cart cart = cartService.getCart(userId);
	    List<CartItems> cartItems = cartService.getCartItems(userId);

	    if (cartItems.isEmpty()) {
	        throw new ResourceNotFoundException("No items found in cart for user: " + userId);
	    }

	    List<String> successful = new ArrayList<>();
	    List<String> failed = new ArrayList<>();
	    ZonedDateTime nowInIST = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String orderDateTime = nowInIST.format(formatter);
	    try {
	        int totalQuantity = cartItems.stream().mapToInt(CartItems::getQuantity).sum();
	        Orders order = new Orders(
	            userId,
	            cartItems,
	            cart.getTotalAmount(),
	            totalQuantity,
	            payment.getPaymentId(),
	            "PAID",
	            "ORDER_CONFIRMED",
	            orderDateTime
	        );
	        Orders savedOrder = orderRepo.save(order);
	        for (CartItems item : cartItems) {
	            productService.updateProductStock(item.getProductId(), item.getQuantity());
	        }

	        cartService.deleteSelectedCartItems(userId, cartItems);
	        List<User> ordersAdmins = userRepo.findOrdersAdmins();
	        List<String> ordersAdminEmails = ordersAdmins.stream()
	                                                     .map(User::getEmail)
	                                                     .collect(Collectors.toList());
	        emailService.intimateOrderAdmins(ordersAdminEmails, savedOrder);
	        ByteArrayInputStream invoicePdf = invoicePdfGenerator.generatePdf(savedOrder, payment);
	        emailService.sendOrderConfirmationWithInvoice(user.getEmail(), user.getUserName(), savedOrder.getOrderId(), invoicePdf);
	        successful.add(savedOrder.getOrderId());

	    } catch (Exception e) {
	        cartItems.forEach(item -> failed.add(item.getName()));
	        e.printStackTrace();
	    }
	    OrderAddResponseDTO response = new OrderAddResponseDTO();
	    response.setSuccessfulOrders(successful);
	    response.setFailedOrders(failed);
	    return response;
	}

	private Map<String, Object> convertJsonToMap(String jsonString) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to parse JSON payload", e);
	    }
	}

	public OrderGetResponseDTO getEntireOrders() {
        List<Orders> orders = orderRepo.findAll();
        OrderGetResponseDTO orderGetResponseDTO = new OrderGetResponseDTO();
        orderGetResponseDTO.setOrderList(orders);
        return orderGetResponseDTO;
	}

	public OrderGetResponseDTO getOrder(String orderId) {
	    Orders order = orderRepo.findById(orderId)
	            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
	    OrderGetResponseDTO orderGetResponseDTO = new OrderGetResponseDTO();
	    orderGetResponseDTO.setOrderList(Collections.singletonList(order));
	    return orderGetResponseDTO;
	}

	public OrderGetResponseDTO getOrderByStatus(String status) {
	    List<Orders> orders = orderRepo.findByStatus(status);  
	    if (!orders.isEmpty()) {
	        OrderGetResponseDTO orderGetResponseDTO = new OrderGetResponseDTO();
	        orderGetResponseDTO.setOrderList(orders);
	        return orderGetResponseDTO;
	    }	    
	    throw new ResourceNotFoundException("Failed to fetch orders with provided status: " + status);
	}

	public String updateOrders(String orderId, String status) {
        Query query = new Query(Criteria.where("_id").is(orderId));
        Update update = new Update();
        update.set("orderStatus", status);
        UpdateResult updateResult = mongotemplate.updateFirst(query, update, Orders.class);
        if(updateResult.getModifiedCount() > 0){
            return "Order updated successfully";
        }
        throw new EntityUpdationException("Failed to update order");
	}




}


