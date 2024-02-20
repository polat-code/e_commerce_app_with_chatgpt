package com.example.ecommerce_app_with_chathpt.service;


import com.example.ecommerce_app_with_chathpt.dto.AttributeValueRepository;
import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@NoArgsConstructor
@Service
@AllArgsConstructor
public class AttributeValueService {

    private AttributeValueRepository attributeValueRepository;


    public Optional<AttributeValue> findByAttributeAndValue(Attribute attribute, String value){

        return attributeValueRepository.findByAttributeAndValue(attribute,value);

    }


}
