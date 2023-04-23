package org.example.registrationdashboardservice.model.product;

import lombok.Data;

import java.util.List;

@Data
public class Product {
    private String name;
    private String fieldType;
    private List<ProductOption> productOptions;
}