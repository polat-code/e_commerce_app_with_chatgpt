package com.example.ecommerce_app_with_chathpt.model.dto.response;

import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.dto.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ProductListResponse {

    private String id;
    private Date creationTime;
    private String returnType;
    private List<ProductResponse> searchProducts;
}
