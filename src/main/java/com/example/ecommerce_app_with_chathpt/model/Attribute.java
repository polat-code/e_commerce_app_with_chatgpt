package com.example.ecommerce_app_with_chathpt.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@Builder
@Document(collection = "attribute")
public class Attribute {

    @Id
    private String id;

    @Field(name = "name")
    private String name;


}