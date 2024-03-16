package com.example.ecommerce_app_with_chathpt.repository;

import com.example.ecommerce_app_with_chathpt.model.CartEntity;
import com.example.ecommerce_app_with_chathpt.model.ProductListEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductListRepository extends MongoRepository<ProductListEntity, String> {
}
