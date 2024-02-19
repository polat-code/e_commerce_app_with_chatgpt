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

    @Field(name = "category_name")
    private String categoryName;

    @DBRef
    private int parentCategoryId;



}
