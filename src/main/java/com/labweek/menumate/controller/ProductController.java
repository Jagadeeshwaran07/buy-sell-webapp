package com.labweek.menumate.controller;


import com.labweek.menumate.dto.NewProductDto;
import com.labweek.menumate.entity.NewProductEntity;
import com.labweek.menumate.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService; // private ProductService productAddService;

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId)
    {
        System.out.println("kko");
        boolean isDeleted = productService.deleteProductById(productId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<NewProductDto> addProduct(
            @RequestParam("userName") String userName,
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("purchaseDate") String purchaseDate,
            @RequestParam(value = "dateListed", required = false) String dateListed,
            @RequestParam("price") Double price,  // Change to Double
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image) {

        System.out.println("kko");

        if (dateListed == null || dateListed.isEmpty()) {
            dateListed = LocalDate.now().toString();
        }

        byte[] imageBytes = null;
        try {
            imageBytes = image.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error if image conversion fails
        }

        // Create DTO with image bytes
        NewProductDto newProductDto = NewProductDto.builder()
                .userName(userName)
                .productName(productName)
                .description(description)
                .purchaseDate(purchaseDate)
                .dateListed(dateListed)
                .price(price)  // Use Double here
                .image(imageBytes)
                .category(category)
                .build();

        // Call the service to add the product
        NewProductDto addedProduct = productService.addProduct(newProductDto);

        return ResponseEntity.ok(addedProduct);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<NewProductEntity>> getProductsByCategory(@PathVariable String category) {
        System.out.println("get category");
        // Call the service to get products by category
        List<NewProductEntity> products = productService.getProductsByCategory(category);
        // Check if products were found for the category
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if no products found
            }
        // Return the list of products found for the category
        return ResponseEntity.ok(products);}


    // New endpoint to get recently listed products
    @GetMapping("/recent")
    public ResponseEntity<List<NewProductEntity>> getRecentlyListedProducts() {
        List<NewProductEntity> recentProducts = productService.getRecentlyListedProducts();
        return recentProducts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(recentProducts);
    }

    // New endpoint for searching products by keyword
    @GetMapping("/search")
    public ResponseEntity<List<NewProductEntity>> searchProducts(@RequestParam("keyword") String keyword) {
        List<NewProductEntity> searchResults = productService.searchProducts(keyword);
        return searchResults.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(searchResults);
    }

//    // Endpoint to get products sorted by price (low to high)
//    @GetMapping("/sort/price/asc")
//    public ResponseEntity<List<NewProductEntity>> getProductsSortedByPriceAsc() {
//        List<NewProductEntity> products = productService.getProductsSortedByPriceAsc();
//        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
//    }
//
//    // Endpoint to get products sorted by price (high to low)
//    @GetMapping("/sort/price/desc")
//    public ResponseEntity<List<NewProductEntity>> getProductsSortedByPriceDesc() {
//        List<NewProductEntity> products = productService.getProductsSortedByPriceDesc();
//        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
//    }

    @GetMapping("/sort/price")
    public ResponseEntity<List<NewProductEntity>> getProductsSortedByPrice(
            @RequestParam(value = "order", defaultValue = "asc") String order) {
        List<NewProductEntity> products;

        if ("desc".equalsIgnoreCase(order)) {
            products = productService.getProductsSortedByPriceDesc();
        } else {
            products = productService.getProductsSortedByPriceAsc();
        }

        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }


}