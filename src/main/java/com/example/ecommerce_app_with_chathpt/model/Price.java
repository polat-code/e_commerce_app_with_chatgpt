package com.example.ecommerce_app_with_chathpt.model;




import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Price {

    private String currency;
    private Float price;

}
