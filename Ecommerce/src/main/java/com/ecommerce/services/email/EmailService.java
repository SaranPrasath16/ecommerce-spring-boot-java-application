package com.ecommerce.services.email;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ecommerce.model.CartItems;
import com.ecommerce.model.Orders;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
    private final long OTP_EXPIRATION_MINUTES = 5;
	
    @Autowired
    private JavaMailSender mailSender;
	
    public void sendOtpByEmail(String email, int otp, String userName){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("QuickPikk <quickpikk1@gmail.com>");
        message.setSubject("Your One-Time Password (OTP) for Verification");
        String emailBody = "Dear "+ userName +",\n\n"
                + "Your One-Time Password (OTP) for verification is: "+ otp +"\n\n"
                + "This OTP is valid for " + OTP_EXPIRATION_MINUTES + " minutes. "
                + "Please do not share this OTP with anyone for security reasons.\n\n"
                + "If you did not request this OTP, please ignore this email.\n\n"
                + "Best regards,\n"
                + "QuickPikk Team";

        message.setText(emailBody);
        mailSender.send(message);
    }
    
    public void sendPaymentLink(String email, String userName, String paymentLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("QuickPikk <quickpikk1@gmail.com>");
        message.setSubject("Complete Your Payment for Your Order");

        String emailBody = "Dear " + userName + ",\n\n"
                + "Thank you for shopping with us!\n\n"
                + "To complete your purchase, please click the payment link below:\n"
                + paymentLink + "\n\n"
                + "Please complete the payment within 30 minutes to avoid order cancellation.\n"
                + "Do not share this payment link with anyone for security reasons.\n\n"
                + "If you did not place this order, please ignore this email.\n\n"
                + "Best regards,\n"
                + "QuickPikk Team";

        message.setText(emailBody);
        mailSender.send(message);
    }
    
    public void sendAccountCreatedEmail(String email, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("QuickPikk <quickpikk1@gmail.com>");
        message.setSubject("Welcome to QuickPikk - Your Account Is Ready!");

        String emailBody = "Hi " + userName + ",\n\n"
                + "Thank you for signing up at QuickPikk! 🛍️\n\n"
                + "Your account has been successfully created, and you're now ready to explore our latest collections.\n\n"
                + "Here's what you can do next:\n"
                + "- Browse new arrivals\n"
                + "- Track your orders\n"
                + "- Save your favorites to your wishlist\n"
                + "- Enjoy exclusive offers and deals\n\n"
                + "We’re excited to have you with us!\n"
                + "Happy shopping! 🛒\n\n"
                + "Best regards,\n"
                + "QuickPikk Team";

        message.setText(emailBody);
        mailSender.send(message);
    }

    public void intimateSuperAdmins(List<String> adminEmails,User user) {
        if (adminEmails == null || adminEmails.isEmpty()) {
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmails.toArray(new String[0]));
        message.setFrom("QuickPikk <quickpikk1@gmail.com>");
        message.setSubject("🚀 New User Registered on QuickPikk!");

        String emailBody = "Hello Superadmin,\n\n"
                + "A new user has successfully registered on QuickPikk. 🎉\n\n"
                + "👤 User Details:\n"
                + "- UserId: " + user.getUserId() + "\n"
                + "- UserName: " + user.getUserName() + "\n"
                + "- Email: " + user.getEmail() + "\n"
                + "- Mobile Number: " + user.getMobile() + "\n\n"
                + "Please review the user if necessary.\n\n"
                + "Thank you for managing the platform! 🙌\n\n"
                + "Best regards,\n"
                + "QuickPikk Team";

        message.setText(emailBody);
        mailSender.send(message);
    }
    
    public void intimateOrderAdmins(List<String> orderAdminEmails, Orders order) {
        if (orderAdminEmails == null || orderAdminEmails.isEmpty() || order == null) {
            return;
        }

        String orderId = order.getOrderId();
        String paymentStatus = order.getPaymentStatus();
        List<CartItems> orderItems = order.getCartItems();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(orderAdminEmails.toArray(new String[0]));
        message.setFrom("QuickPikk <quickpikk1@gmail.com>");
        message.setSubject("🚨 New Order Update on QuickPikk!");

        StringBuilder orderItemsDetails = new StringBuilder();
        for (CartItems item : orderItems) {
            orderItemsDetails.append("- Product: ").append(item.getName())
                             .append("\n\n  Quantity: ").append(item.getQuantity())
                             .append("\n\n");
        }

        String emailBody = "Hello Order Admin,\n\n"
                + "An order update has been received on QuickPikk. 🚀\n\n"
                + "🛒 Order Details:\n"
                + "- OrderId: " + orderId + "\n\n"
                + "- Payment Status: " + paymentStatus + "\n\n"
                + "- Ordered Items:\n"
                + orderItemsDetails.toString() + "\n\n"
                + "Please review the order for further actions.\n\n"
                + "Thank you for managing the orders! 🙌\n\n"
                + "Best regards,\n"
                + "QuickPikk Team";

        message.setText(emailBody);
        mailSender.send(message);
    }
    
    public void intimateProductAdminsOnAddingProduct(List<String> productAdminEmails, Product product) {
        if (productAdminEmails == null || productAdminEmails.isEmpty() || product == null) {
            return;
        }
        String category = product.getCategory();
        String productId=product.getProductId();
        String productName = product.getProductName();
        Double price = product.getProductPrice();
        String description = product.getProductDescription();
        List<String> images = product.getImageUrls();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(productAdminEmails.toArray(new String[0]));
        message.setFrom("QuickPikk <quickpikk1@gmail.com>");
        message.setSubject("🛍️ New Product Added to QuickPikk!");

        StringBuilder imagesList = new StringBuilder();
        if (images != null && !images.isEmpty()) {
            for (String imageUrl : images) {
                imagesList.append("- ").append(imageUrl).append("\n");
            }
        } else {
            imagesList.append("No images uploaded.");
        }

        String emailBody = "Hello Product Admin,\n\n"
                + "A new product has been successfully added to QuickPikk! 🎉\n\n"
                + "📦 Product Details:\n"
                + "- Category: " + category + "\n\n"
                + "- ProductId: " + productId + "\n\n"
                + "- Name: " + productName + "\n\n"
                + "- Price: ₹" + price + "\n\n"
                + "- Description: " + description + "\n\n"
                + "- Images:\n" + imagesList.toString() + "\n\n"
                + "Kindly verify the listing and ensure everything looks perfect.\n\n"
                + "Thank you for managing the products! 🙌\n\n"
                + "Best regards,\n"
                + "QuickPikk Team";

        message.setText(emailBody);
        mailSender.send(message);
    }
    
    public void sendOrderConfirmationWithInvoice(String toEmail, String customerName, String orderId, ByteArrayInputStream invoicePdfStream) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setFrom("QuickPikk <quickpikk1@gmail.com>");
            helper.setSubject("🧾 Your Order " + orderId + " is Confirmed");

            String emailBody = "Hi " + customerName + ",\n\n"
                    + "We're happy to let you know that your order with QuickPikk is placed successfully! 🎉\n"
                    + "We will share tracking details within 24 - 48 hours.\n\n"
                    + "🛍️ Order ID: " + orderId + "\n\n"
                    + "We've attached your invoice to this email for your reference.\n\n"
                    + "Thanks for shopping with QuickPikk – we can't wait to serve you again! 😊\n\n"
                    + "Warm regards,\n"
                    + "QuickPikk Team";
            helper.setText(emailBody);
            ByteArrayResource resource = new ByteArrayResource(invoicePdfStream.readAllBytes());
            helper.addAttachment("Invoice_" + orderId + ".pdf", resource);

            mailSender.send(message);
            System.out.println("Order confirmation email sent to " + toEmail);

        } catch (Exception e) {
            System.err.println("Error sending order confirmation email: " + e.getMessage());
            e.printStackTrace();
        }
    }





}
