package com.labweek.menumate.services;

import com.labweek.menumate.dto.NewProductDto;
import com.labweek.menumate.entity.NewProductEntity;
import com.labweek.menumate.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ProductAddService {

    @Autowired
    private ProductRepository productRepository;

    public NewProductDto addProduct(NewProductDto newProductDto) {
        // Convert NewProductDto to NewProductEntity
        NewProductEntity productEntity = NewProductEntity.builder()
                .productName(newProductDto.getProductName())
                .description(newProductDto.getDescription())
                .purchaseDate(newProductDto.getPurchaseDate())
                .price(newProductDto.getPrice())
                .listedDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .image(newProductDto.getImage()) // Set the image (byte array)
                .build();

        // Save to database
        NewProductEntity savedProduct = productRepository.save(productEntity);

        // Convert saved entity back to NewProductDto
        NewProductDto savedProductDto = new NewProductDto();
        savedProductDto.setProductName(savedProduct.getProductName());
        savedProductDto.setDescription(savedProduct.getDescription());
        savedProductDto.setPurchaseDate(savedProduct.getPurchaseDate());
        savedProductDto.setListedDate(savedProduct.getListedDate());
        savedProductDto.setImage(savedProduct.getImage()); // Add image field in the DTO


        return savedProductDto;
    }
}