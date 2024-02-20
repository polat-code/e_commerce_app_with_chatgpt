package com.example.ecommerce_app_with_chathpt.service;


import com.example.ecommerce_app_with_chathpt.repository.AttributeValueRepository;
import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AttributeValueService {

    private AttributeValueRepository attributeValueRepository;


    public Optional<AttributeValue> findByAttributeAndValue(Attribute attribute, String value){

        return attributeValueRepository.findByAttributeAndValue(attribute,value);

    }


    public AttributeValue createAndSaveAttributeValueObject(Attribute attribute,String keyValue) {

        AttributeValue attributeValue = AttributeValue.builder()
                .attribute(attribute)
                .value(keyValue)
                .build();
        return attributeValueRepository.save(attributeValue);
    }
}
