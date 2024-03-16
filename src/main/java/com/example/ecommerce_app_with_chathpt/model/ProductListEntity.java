package com.example.ecommerce_app_with_chathpt.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@EqualsAndHashCode(callSuper = true)
@TypeAlias("productLists")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductListEntity extends ChatEntity{

    private String messageContent;
}
