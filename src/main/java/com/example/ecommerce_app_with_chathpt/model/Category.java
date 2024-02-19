package com.example.elasticchat.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Builder
@Document(indexName = "category")
public class Category {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "category")
    private String category;

    // You can add the necessary Elasticsearch annotations

    @Field(type = FieldType.Integer, name = "parent_category_id")
    private int parentCategoryId;


    // Constructors, Getters, and Setters
}
