package com.example.ecommerce_app_with_chathpt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartProduct {

    @DBRef
    private Product product;
    private int quantity;
    private Date creationTime;
    private Date updateTime;
    private Double unitPrice;
    private Double totalPrice;

}
