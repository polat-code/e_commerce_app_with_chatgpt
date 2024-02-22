package com.example.ecommerce_app_with_chathpt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String id;

    @DBRef
    private List<AttributeValue> attributeValues;
    @Field
    private Double price;

    @DBRef
    private Category category;
    @Field
    private boolean inStock;
    @Field
    private String url;
    @Field
    private String brand;
    @Field
    private String breadCrumbs;
    @Field
    private String title;
    @Field
    private String thumbnailImage;

}
