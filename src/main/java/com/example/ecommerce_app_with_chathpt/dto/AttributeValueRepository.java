package com.example.ecommerce_app_with_chathpt.dto;

import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttributeValueRepository extends MongoRepository<AttributeValue,String> {
}
