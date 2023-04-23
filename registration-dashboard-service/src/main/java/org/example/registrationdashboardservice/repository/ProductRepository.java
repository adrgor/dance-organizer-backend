package org.example.registrationdashboardservice.repository;

import org.example.registrationdashboardservice.model.product.ProductList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductList, String> {

    ProductList findByEventId(int eventId);
}