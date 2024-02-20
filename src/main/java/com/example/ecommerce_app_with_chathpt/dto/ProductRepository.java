package com.example.ecommerce_app_with_chathpt.dto;

import com.example.ecommerce_app_with_chathpt.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product,String> {

}
