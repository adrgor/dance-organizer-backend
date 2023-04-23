package org.example.registrationdashboardservice.controller;

import lombok.AllArgsConstructor;
import org.example.registrationdashboardservice.clients.EventClient;
import org.example.registrationdashboardservice.exception.InvalidEventOwnerException;
import org.example.registrationdashboardservice.model.AuthenticatedUser;
import org.example.registrationdashboardservice.model.product.ProductList;
import org.example.registrationdashboardservice.model.product.ProductListDTO;
import org.example.registrationdashboardservice.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration-dashboard/product")
@AllArgsConstructor
public class ProductController {

    private final EventClient eventClient;
    private final ProductRepository productRepository;

    @GetMapping
    public ProductList getProductsByEventId(@RequestParam("eventId") int eventId,
                                            @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int ownerId = eventClient.getEventOwnerId(eventId, jwt);

        if (ownerId == authenticatedUser.id())
            return productRepository.findByEventId(eventId);
        else throw new InvalidEventOwnerException();
    }

    @PostMapping
    public void addProduct(@RequestBody ProductListDTO productListDTO,
                           @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int eventId = productListDTO.eventId();
        int ownerId = eventClient.getEventOwnerId(eventId, jwt);

        ProductList productList = productRepository.findByEventId(eventId);

        if (productList != null) {
            productList.setProducts(productListDTO.products());
        } else {
            productList = new ProductList(productListDTO.eventId(), productListDTO.products());
        }

        if (ownerId == authenticatedUser.id())
            productRepository.save(productList);
        else throw new InvalidEventOwnerException();
    }
}