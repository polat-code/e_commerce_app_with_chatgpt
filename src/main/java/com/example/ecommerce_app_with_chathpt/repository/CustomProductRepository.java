package com.example.ecommerce_app_with_chathpt.repository;

import com.example.ecommerce_app_with_chathpt.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomProductRepository {
    List<Product> findProductsByCategoryAndAttributeValues(String categoryId, List<String> attributeValueIds);
}
