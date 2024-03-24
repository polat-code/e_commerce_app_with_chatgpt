package com.example.ecommerce_app_with_chathpt.model.dto.response;

import com.example.ecommerce_app_with_chathpt.model.Product;
import com.example.ecommerce_app_with_chathpt.model.dto.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse{
    private String messageType;
    private List<ProductResponse> productList;
    private Double totalPrice;
    private Map<Product,Integer> cart;

}