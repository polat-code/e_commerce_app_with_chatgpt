package com.example.ecommerce_app_with_chathpt.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductQuantity {

    private Product product;
    private int quantity;

}
