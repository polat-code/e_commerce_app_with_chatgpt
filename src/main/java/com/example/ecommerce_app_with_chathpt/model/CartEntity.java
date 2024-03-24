package com.example.ecommerce_app_with_chathpt.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@TypeAlias("cartsEntities")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartEntity extends ChatEntity{


    private List<Product> cartProduct;

}
