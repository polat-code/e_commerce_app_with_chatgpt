package com.example.ecommerce_app_with_chathpt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;

@Data
@Builder
@Document(collection = "userCarts")
@NoArgsConstructor
@AllArgsConstructor
public class UserCart {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private List<Product> cartProducts;

    @Field(targetType = FieldType.DOUBLE)
    private Double totalPrice;



}
