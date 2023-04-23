package org.example.registrationdashboardservice.model.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("products")
@Data
public class ProductList {

    @Id
    private String id;
    private Integer eventId;
    private List<Product> products;
}