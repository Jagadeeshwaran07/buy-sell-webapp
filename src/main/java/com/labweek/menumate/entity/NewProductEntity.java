package com.labweek.menumate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "productlist")

public class NewProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username", nullable = false)
    private String userName;

    @Column(name = "productname", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "purchasedate", nullable = false)
    private String purchaseDate;

    @Column(name = "datelisted", nullable = false)
    private LocalDate dateListed;

    @Column(name = "price", nullable = false)
    private Double price;

//    @Column(name = "category", nullable =false)
//    private String category;

    @Column(name = "category", nullable = false)
    private String category;

    @Lob
    @Column(name = "image", nullable = false)
    private String image; // Store the image as a byte array

}
