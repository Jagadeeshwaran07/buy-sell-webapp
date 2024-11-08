package com.labweek.menumate.services;

import com.labweek.menumate.dto.NewProductDto;
import com.labweek.menumate.entity.NewProductEntity;
import com.labweek.menumate.exceptions.NoProductFoundException;
import com.labweek.menumate.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public boolean deleteProductById(Long productId) {
        // Check if the product exists
        Optional<NewProductEntity> product = productRepository.findById(productId);
        if (product.isPresent()) {
            // If the product exists, delete it
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }



    public NewProductDto addProduct(NewProductDto newProductDto) {
        // Convert NewProductDto to NewProductEntity
        NewProductEntity productEntity = NewProductEntity.builder()
                .productName(newProductDto.getProductName())
                .description(newProductDto.getDescription())
                .purchaseDate(newProductDto.getPurchaseDate())
                .userName(newProductDto.getUserName())
                .price(newProductDto.getPrice())  // Use Double for price
                .dateListed(LocalDate.now())
                .image(newProductDto.getImage())
                .category(newProductDto.getCategory())
                .build();

        // Save to database
        NewProductEntity savedProduct = productRepository.save(productEntity);

        // Convert saved entity back to NewProductDto
        NewProductDto savedProductDto = new NewProductDto();
        savedProductDto.setProductName(savedProduct.getProductName());
        savedProductDto.setDescription(savedProduct.getDescription());
        savedProductDto.setUserName(savedProduct.getUserName());
        savedProductDto.setPrice(savedProduct.getPrice());  // Use Double for price
        savedProductDto.setPurchaseDate(savedProduct.getPurchaseDate());
        savedProductDto.setDateListed(savedProduct.getDateListed().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        savedProductDto.setImage(savedProduct.getImage());
        savedProductDto.setCategory(savedProduct.getCategory());

        return savedProductDto;
    }

    @Transactional
    public List<NewProductEntity> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);}

    // New method to get recently listed products
    @Transactional
    public List<NewProductEntity> getRecentlyListedProducts() {
        return productRepository.findAllByOrderByDateListedDesc();
    }

    // Search for products by a keyword
    @Transactional
    public List<NewProductEntity> searchProducts(String keyword) {
        List<NewProductEntity> products = productRepository.searchProductsByName(keyword);

        if (products.isEmpty()) {
            throw new NoProductFoundException("No products exist with the provided keyword: " + keyword);
        }

        return products;
    }

    // Method to get products sorted by price (low to high)
    @Transactional
    public List<NewProductEntity> getProductsSortedByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();  // Ascending order by price
    }

    // Method to get products sorted by price (high to low)
    @Transactional
    public List<NewProductEntity> getProductsSortedByPriceDesc() {
        return productRepository.findAllByOrderByPriceDesc();  // Descending order by price
    }

}