package com.microservices.product_services.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservices.product_services.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
