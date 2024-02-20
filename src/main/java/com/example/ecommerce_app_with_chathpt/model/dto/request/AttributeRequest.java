package com.example.ecommerce_app_with_chathpt.model.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeRequest {

    private String key;
    private String value;
}
