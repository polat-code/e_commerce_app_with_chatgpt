package com.example.ecommerce_app_with_chathpt.model.dto.response;

import com.example.ecommerce_app_with_chathpt.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String productId;
    private Double price;

    private Category category;

    private boolean inStock;

    private String url;

    private String brand;

    private String title;



}
