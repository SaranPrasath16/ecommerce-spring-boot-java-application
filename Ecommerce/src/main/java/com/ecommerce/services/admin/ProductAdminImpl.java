package com.ecommerce.services.admin;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.dto.ProductDeleteResponseDTO;
import com.ecommerce.dto.ProductGetResponseDTO;
import com.ecommerce.dto.ProductRequestDTO;
import com.ecommerce.dto.ProductUpdateRequestDTO;
import com.ecommerce.model.Product;
import com.ecommerce.services.product.ProductService;
import com.ecommerce.services.review.ReviewService;

@Service
public class ProductAdminImpl {
	private final ProductService productService;
	private final ReviewService reviewService;


	public ProductAdminImpl(ProductService productService, ReviewService reviewService) {
		super();
		this.productService = productService;
		this.reviewService = reviewService;
	}

	public String addProduct(ProductRequestDTO productRequestDTO, MultipartFile[] images) {
	       return productService.addProduct(productRequestDTO, images);
	}

	public String updateProductById(ProductUpdateRequestDTO productUpdateRequestDTO, MultipartFile[] images) {
		return productService.updateProductById(productUpdateRequestDTO, images);
	}

	public List<ProductGetResponseDTO> getAllProduct() {
        List<Product> productList = productService.getAllProducts();
        List<ProductGetResponseDTO> productDTOList = productList.stream()
                .map(product -> new ProductGetResponseDTO(
                        product.getCategory(),
                        product.getProductName(),
                        product.getProductDescription(),
                        product.getProductPrice(),
                        product.getImageUrls(),
                        reviewService.getProductReviews(product.getProductId())
                ))
                .collect(Collectors.toList());

        return productDTOList;
	}

	public ProductDeleteResponseDTO deleteProductByName(String productId) {
	    return productService.deleteById(productId);
	}

	public Object getProductReviews(String productId) {
		return reviewService.getProductReviews(productId);
	}

	public List<ProductGetResponseDTO> getProductByName(String productName) {
		return productService.getProductByName(productName);
	}

	public List<ProductGetResponseDTO> getProductByCategory(String category) {
		return productService.getProductByCategory(category);
	}
	
}
