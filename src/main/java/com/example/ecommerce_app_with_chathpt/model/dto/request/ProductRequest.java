package com.example.ecommerce_app_with_chathpt.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String title;
    private String categoryName;
    private String url;
    private Double price;
    private Boolean inStock;
    private String thumbnailImage;
    private List<AttributeRequest> attributes;
}