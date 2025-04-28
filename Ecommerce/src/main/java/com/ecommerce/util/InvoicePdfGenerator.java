package com.ecommerce.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import com.ecommerce.exceptionhandler.ResourceNotFoundException;
import com.ecommerce.model.CartItems;
import com.ecommerce.model.Orders;
import com.ecommerce.model.Payment;
import com.ecommerce.model.User;
import com.ecommerce.repo.UserRepo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class InvoicePdfGenerator {
	
	private final UserRepo userRepo;

    public InvoicePdfGenerator(UserRepo userRepo) {
		super();
		this.userRepo = userRepo;
	}
   
	public ByteArrayInputStream generatePdf(Orders order, Payment payment) {
        Document document = new Document(PageSize.A4, 36, 36, 72, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        User user = userRepo.findById(payment.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph header = new Paragraph("Invoice Bill", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph(" "));

            PdfPTable addresses = new PdfPTable(2);
            addresses.setWidthPercentage(100);
            addresses.setWidths(new int[]{1, 1});
            addresses.addCell(getBoxCell("Billing Address:\n" + user.getAddress(), PdfPCell.ALIGN_LEFT));
            addresses.addCell(getBoxCell("Shipping Address:\n" + user.getAddress(), PdfPCell.ALIGN_LEFT));
            document.add(addresses);
            document.add(new Paragraph(" "));

            PdfPTable meta = new PdfPTable(2);
            meta.setWidthPercentage(100);
            meta.setSpacingBefore(10);
            meta.addCell(getCell("Order Number: " + order.getOrderId(), PdfPCell.ALIGN_LEFT));
            meta.addCell(getCell("Invoice Number: " + payment.getRazorInvoiceId(), PdfPCell.ALIGN_RIGHT));
            meta.addCell(getCell("Order Date: " + order.getOrderDateTime(), PdfPCell.ALIGN_LEFT));
            meta.addCell(getCell("Invoice Date: " + payment.getPaymentDateTime(), PdfPCell.ALIGN_RIGHT));
            document.add(meta);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 4, 2, 2, 2});

            String[] headers = {"Sl. No", "Description", "Unit Price", "Quantity", "Total"};
            for (String h : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(h));
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(headerCell);
            }
            int index = 1;
            for (CartItems item : order.getCartItems()) {
                table.addCell(String.valueOf(index++));
                table.addCell(item.getName());
                double unitPrice = item.getPrice() / item.getQuantity();
                table.addCell("INR " + String.format("%.2f", unitPrice));
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("INR " + String.format("%.2f", item.getPrice()));
            }

            document.add(table);
            document.add(new Paragraph(" "));
            Paragraph total = new Paragraph("Total Invoice Value: INR " + order.getTotalAmount());
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);
            document.add(new Paragraph(" ")); 
            document.add(new Paragraph("Payment Mode: " + payment.getPaymentMethod()));
            document.add(new Paragraph("Transaction ID: " + payment.getTranscationId()));
            document.add(new Paragraph("Date & Time: " + payment.getPaymentDateTime()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Sold By: " + "QuickPikk"));
            document.add(new Paragraph("Bengaluru, Karnataka, India"));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private static PdfPCell getBoxCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(8);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        return cell;
    }
}

