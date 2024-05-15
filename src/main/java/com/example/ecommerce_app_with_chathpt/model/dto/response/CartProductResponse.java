package com.example.ecommerce_app_with_chathpt.model.dto.response;

import com.example.ecommerce_app_with_chathpt.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductResponse {

    private String id;
    private String brand;
    private String title;
    private Category category;
    private double unitPrice;
    private int quantity;
    private double totalPrice;


}
