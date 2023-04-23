package org.example.registrationdashboardservice.model.product;

import lombok.Data;

@Data
public class ProductOption {
    private Integer id;
    private String name;
    private Float price;
    private String currency;
}