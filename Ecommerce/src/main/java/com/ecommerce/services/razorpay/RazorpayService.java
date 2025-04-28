package com.ecommerce.services.razorpay;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.razorpay.Invoice;
import com.razorpay.RazorpayClient;

@Service
public class RazorpayService {
	
	private final RazorpayClient razorpayClient;
	private static final String HMAC_SHA256 = "HmacSHA256";
	private static final String WEBHOOK_SECRET = "secureWebhookKey$98";

	
    public RazorpayService(RazorpayClient razorpayClient) {
		super();
		this.razorpayClient = razorpayClient;
	}


    public Map<String, String> generateInvoice(String customerName, String customerEmail, double amountInRupees) throws Exception {
        JSONObject invoiceRequest = new JSONObject();
        invoiceRequest.put("type", "invoice");
        invoiceRequest.put("description", "Invoice for order payment");

        JSONObject customerDetails = new JSONObject();
        customerDetails.put("name", customerName);
        customerDetails.put("email", customerEmail);
        invoiceRequest.put("customer", customerDetails);

        JSONArray lineItems = new JSONArray();
        JSONObject item = new JSONObject()
            .put("name", "Order payment for: " + customerName)
            .put("amount", (int)(amountInRupees * 100))
            .put("currency", "INR")
            .put("quantity", 1);
        lineItems.put(item);

        invoiceRequest.put("line_items", lineItems);
        invoiceRequest.put("sms_notify", 1);
        invoiceRequest.put("email_notify", 1);

        long currentTimestamp = System.currentTimeMillis() / 1000;
        long expiryTimestamp = currentTimestamp + (24 * 60 * 60);
        invoiceRequest.put("expire_by", expiryTimestamp);

        Invoice invoice = razorpayClient.invoices.create(invoiceRequest);

        Map<String, String> response = new HashMap<>();
        response.put("invoice_id", invoice.get("id"));
        response.put("short_url", invoice.get("short_url"));
        return response;
    }
 
    public boolean verifyRazorpayWebhookSignature(String payloadBody, String providedSignature) {
        try {
            String generatedSignature = generateHmacSha256Hex(payloadBody);
            return generatedSignature.equals(providedSignature);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Razorpay signature: " + e.getMessage(), e);
        }
    }

    private String generateHmacSha256Hex(String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(WEBHOOK_SECRET.getBytes(), HMAC_SHA256);
        sha256_HMAC.init(secretKeySpec);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
