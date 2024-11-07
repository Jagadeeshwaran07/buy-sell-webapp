package com.labweek.menumate.controller;


import com.labweek.menumate.dto.NewProductDto;
import com.labweek.menumate.services.ProductAddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/products")
public class ProductAddController {

    @Autowired
    private ProductAddService productAddService;

    









    @PostMapping("/add")
    public ResponseEntity<NewProductDto> addProduct(
            @RequestParam("userName") String userName,
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("purchaseDate") String purchaseDate,
            @RequestParam(value = "dateListed", required = false) String dateListed,
            @RequestParam("price") String price,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image) {

        if (dateListed == null || dateListed.isEmpty()) {
            dateListed = LocalDate.now().toString();
        }

        byte[] imageBytes = null;
        try {
            imageBytes = image.getBytes();
        } catch (Exception e) {
            e.printStackTrace();  // Handle error if image conversion fails
        }

        // Create DTO with image bytes
        NewProductDto newProductDto = NewProductDto.builder()
                .userName(userName)
                .productName(productName)
                .description(description)
                .purchaseDate(purchaseDate)
                .dateListed(dateListed)
                .price(price)
                .image(imageBytes)// Add image data
                .category(category)
                .build();

        // Call the service to add the product
        NewProductDto addedProduct = productAddService.addProduct(newProductDto);

        return ResponseEntity.ok(addedProduct);
    }
}