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
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("purchaseDate") String purchaseDate,
            @RequestParam("listedDate") String listedDate,
          //  @RequestParam(value = "listedDate", required = false) String listedDate,
            @RequestParam("price") String price,
            @RequestParam("image") MultipartFile image) {


       //  listedDate = LocalDate.now().toString();  // Use current date as default

        // Convert MultipartFile to byte[]
        byte[] imageBytes = null;
        try {
            imageBytes = image.getBytes();
        } catch (Exception e) {
            e.printStackTrace();  // Handle error if image conversion fails
        }

        // Create DTO with image bytes
        NewProductDto newProductDto = NewProductDto.builder()
                .productName(productName)
                .description(description)
                .purchaseDate(purchaseDate)
                .listedDate(listedDate)
                .price(price)
                .image(imageBytes) // Add image data
                .build();

        // Call the service to add the product
        NewProductDto addedProduct = productAddService.addProduct(newProductDto);

        return ResponseEntity.ok(addedProduct);
    }
}