package org.example.registrationdashboardservice.model.product;

import java.util.List;

public record ProductListDTO(Integer eventId, List<Product> products) {
}