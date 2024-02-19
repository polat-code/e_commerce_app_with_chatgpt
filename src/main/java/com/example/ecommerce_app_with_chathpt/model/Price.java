package com.example.elasticchat.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Price {

    private String currency;
    private Float price;
}
