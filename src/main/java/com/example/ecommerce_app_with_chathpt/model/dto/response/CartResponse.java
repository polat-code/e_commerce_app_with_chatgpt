package com.example.ecommerce_app_with_chathpt.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private String cartId;
    private double totalPrice;
    private List<CartProductResponse> cartProducts;

}
