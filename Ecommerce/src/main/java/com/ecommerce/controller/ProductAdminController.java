package com.ecommerce.controller;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.dto.ProductDeleteResponseDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.dto.ProductRequestDTO;
import com.ecommerce.dto.ProductUpdateRequestDTO;
import com.ecommerce.middleware.AuthRequired;
import com.ecommerce.services.admin.ProductAdminImpl;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/productadmin")
public class ProductAdminController {
	private final ProductAdminImpl productAdminImpl;
 
    public ProductAdminController(ProductAdminImpl productAdminImpl) {
		super();
		this.productAdminImpl = productAdminImpl;
	}

	@GetMapping("/product")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getAllProduct(HttpServletRequest request) {
        List<ProductGetResponseDTO> productDTOList = productAdminImpl.getAllProduct();
        return ResponseEntity.ok(productDTOList);
    }

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthRequired
    public ResponseEntity<String> addProduct(
            @RequestParam("product_Category") String category,
            @RequestParam("product_Name") String productName,
            @RequestParam("product_Description") String productDescription,
            @RequestParam("product_Price") double productPrice,
            @RequestParam("no_Of_Stocks") int noOfStocks,
            @RequestParam("product_Images") MultipartFile[] images,
            HttpServletRequest request) {

        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setCategory(category);
        productRequestDTO.setProductName(productName);
        productRequestDTO.setProductDescription(productDescription);
        productRequestDTO.setProductPrice(productPrice);
        productRequestDTO.setNoOfStocks(noOfStocks);

        String msg = productAdminImpl.addProduct(productRequestDTO, images);
        return ResponseEntity.ok(msg);
    }
    
    @PutMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AuthRequired
    public ResponseEntity<String> updateProductById(
            @RequestParam("product_Id") String productId,
            @RequestParam("product_New_Name") String productNewName,
            @RequestParam("product_New_Category") String productNewCategory,
            @RequestParam("product_New_Description") String productNewDescription,
            @RequestParam("product_New_Price") double productNewPrice,
            @RequestParam("product_New_Stock") int productNewStock,
            @RequestParam("images_To_Delete") List<String> imagesToDelete,
            @RequestParam(value = "product_Images", required = false) MultipartFile[] images,
            HttpServletRequest request) {

        ProductUpdateRequestDTO productUpdateRequestDTO = new ProductUpdateRequestDTO();
        productUpdateRequestDTO.setProductId(productId);
        productUpdateRequestDTO.setProductNewName(productNewName);
        productUpdateRequestDTO.setProductNewCategory(productNewCategory);
        productUpdateRequestDTO.setProductNewDescription(productNewDescription);
        productUpdateRequestDTO.setProductNewPrice(productNewPrice);
        productUpdateRequestDTO.setProductNewStock(productNewStock);
        productUpdateRequestDTO.setImagesToDelete(imagesToDelete);
        
        System.out.println(imagesToDelete);

        String msg = productAdminImpl.updateProductById(productUpdateRequestDTO, images);
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/product")
    @AuthRequired
    public ResponseEntity<String> deleteByName(@RequestParam("product_Id") String productId){
        ProductDeleteResponseDTO productDeleteResponseDTO = productAdminImpl.deleteProductByName(productId);
        return ResponseEntity.ok(productDeleteResponseDTO.getProductMsg()+" and the Product Id: "+productDeleteResponseDTO.getId());
    }
    
    @GetMapping("/product/review")
    public ResponseEntity<Object> getProductReviews(@RequestParam("product_Id") String productId){
    	Object reviewList = productAdminImpl.getProductReviews(productId);
        return ResponseEntity.ok(reviewList);
    }
    
    @GetMapping("/product/name")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductByName(@RequestParam("product_Name") String productName){
    	List<ProductGetResponseDTO> productGetResponseDTO = productAdminImpl.getProductByName(productName);
        return ResponseEntity.ok(productGetResponseDTO);
    }
    
    @GetMapping("/product/category")
    @AuthRequired
    public ResponseEntity<List<ProductGetResponseDTO>> getProductsByCategory(@RequestParam("product_Category") String category){
    	List<ProductGetResponseDTO> productGetResponseDTO = productAdminImpl.getProductByCategory(category);
        return ResponseEntity.ok(productGetResponseDTO);
    }
    

}
