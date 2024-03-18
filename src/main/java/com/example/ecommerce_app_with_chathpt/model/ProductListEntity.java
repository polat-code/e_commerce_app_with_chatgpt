package com.example.ecommerce_app_with_chathpt.model;

import com.example.ecommerce_app_with_chathpt.model.dto.ProductResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@TypeAlias("productLists")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductListEntity extends ChatEntity{

    @DBRef
    private List<ProductResponse> searchProducts;
}
