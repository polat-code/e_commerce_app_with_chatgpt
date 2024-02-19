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
@Document(indexName = "products")
public class Product {

    @Id
    private String id;

    @Field(type = FieldType.Nested)
    private List<AttributeValue> attributes;
    @Field(type = FieldType.Nested)
    private Price price;
    @Field(type = FieldType.Boolean)
    private boolean inStock;
    @Field(type = FieldType.Text)
    private String url;
    @Field(type = FieldType.Text)
    private String brand;
    @Field(type = FieldType.Text)
    private String breadCrumbs;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String thumbnailImage;
}
