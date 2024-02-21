package com.example.ecommerce_app_with_chathpt.repository;

import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AttributeValueRepository extends MongoRepository<AttributeValue,String> {

    Optional<AttributeValue> findByAttributeAndValue(Attribute attribute , String value);

    List<AttributeValue> findAllByAttribute(Attribute attribute);
}
