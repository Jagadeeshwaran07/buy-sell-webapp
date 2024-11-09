package com.labweek.menumate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;


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
    private String price;
    private String category;
    private String image;  // Store the


    // Getters and Setters
}