package com.labweek.menumate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewProductDto {

    private String userName;
    private String productName;
    private String description;
    private String purchaseDate;
    private String dateListed;
    private Double price;
    private byte[] image;
    private String category;


    // Getters and Setters
}