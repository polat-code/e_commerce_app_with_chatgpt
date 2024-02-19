package com.example.elasticchat.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@Builder
@Document(indexName = "product_attribute_value")
public class AttributeValue {

    @Id
    private String id;


    @Field(type = FieldType.Nested, name = "attribute")
    private Attribute attribute;

    @Field(type = FieldType.Text, name = "value")
    private String value;



}