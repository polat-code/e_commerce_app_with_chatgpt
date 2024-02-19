package com.example.ecommerce_app_with_chathpt.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "category")
public class Category {

    @Id
    private String id;

    @DBRef
    private String category;

    // You can add the necessary Elasticsearch annotations

    @Field(name = "parent_category_id")
    private int parentCategoryId;


    // Constructors, Getters, and Setters
}
