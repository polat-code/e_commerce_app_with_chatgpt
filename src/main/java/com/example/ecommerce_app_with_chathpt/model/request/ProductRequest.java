package com.example.ecommerce_app_with_chathpt.model.request;

import com.example.ecommerce_app_with_chathpt.model.Price;
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
    private String brand;
    private String breadCrumbs;
    private String url;
    private Price price;
    private Boolean inStock;
    private String thumbnailImage;
    private List<AttributeRequest> attributeRequests;
}