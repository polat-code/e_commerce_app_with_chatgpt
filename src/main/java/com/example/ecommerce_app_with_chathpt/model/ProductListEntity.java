package com.example.ecommerce_app_with_chathpt.model;

import com.example.ecommerce_app_with_chathpt.model.dto.response.ProductResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@TypeAlias("productListsEntities")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductListEntity extends ChatEntity{

    private List<ProductResponse> searchProducts;

}
