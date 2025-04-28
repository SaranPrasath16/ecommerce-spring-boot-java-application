package com.ecommerce.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ecommerce.util.IdGenerator;

@Document(collection="payment")
@TypeAlias("payment")
public class Payment {
	@Id
	private String paymentId;
	private String userId;
	private String razorOrderId;
	private String razorPaymentId;
	private String razorInvoiceId;
	private String transcationId;
	private String paymentMethod;
    private String paymentStatus;
	private String paymentDateTime;
	
	public String getTranscationId() {
		return transcationId;
	}
	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getRazorInvoiceId() {
		return razorInvoiceId;
	}
	public void setRazorInvoiceId(String razorInvoiceId) {
		this.razorInvoiceId = razorInvoiceId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getRazorOrderId() {
		return razorOrderId;
	}
	public void setRazorOrderId(String razorOrderId) {
		this.razorOrderId = razorOrderId;
	}
	public String getRazorPaymentId() {
		return razorPaymentId;
	}
	public void setRazorPaymentId(String razorPaymentId) {
		this.razorPaymentId = razorPaymentId;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentDateTime() {
		return paymentDateTime;
	}
	public void setPaymentDateTime(String paymentDateTime) {
		this.paymentDateTime = paymentDateTime;
	}


	public Payment(String userId, String razorOrderId, String razorPaymentId, String razorInvoiceId,
			String transcationId, String paymentMethod, String paymentStatus, String paymentDateTime) {
		this();
		this.userId = userId;
		this.razorOrderId = razorOrderId;
		this.razorPaymentId = razorPaymentId;
		this.razorInvoiceId = razorInvoiceId;
		this.transcationId = transcationId;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.paymentDateTime = paymentDateTime;
	}
	public Payment() {
        if (this.paymentId == null || this.paymentId.isEmpty()) {
            this.paymentId = "PAYMENT_" + IdGenerator.generateRandomNumber();
        }
	}
	

}
