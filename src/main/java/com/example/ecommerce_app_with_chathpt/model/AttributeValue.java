package com.example.ecommerce_app_with_chathpt.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "product_attribute_value")
public class AttributeValue {

    @Id
    private String id;


    @Field(name = "attribute")
    private Attribute attribute;

    @Field(name = "value")
    private String value;



}