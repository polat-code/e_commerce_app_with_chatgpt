package com.example.ecommerce_app_with_chathpt.dto;

import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AttributeValueRepository extends MongoRepository<AttributeValue,String> {

    Optional<AttributeValue> findByAttributeAndValue(Attribute attribute , String value);
}
