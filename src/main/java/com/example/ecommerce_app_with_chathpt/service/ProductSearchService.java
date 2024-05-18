package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.*;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ProductResponse;
import com.example.ecommerce_app_with_chathpt.model.dto.response.ChatResponse;
import com.example.ecommerce_app_with_chathpt.model.enums.MessageType;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductSearchService {

    private AttributeService attributeService;
    private AttributeValueService attributeValueService;
    private ProductService productService;


    public ChatResponse searchProduct(Category category,List<AttributeValue> attributeValues){

        List<Product>  foundProducts = productService.getAllProductsByCategoryAndAttributeValue(category.getId(),
                attributeValues.stream().map(AttributeValue::getId).collect(Collectors.toList()));

        return productToChatResponse(foundProducts);
    }



    private ChatResponse productToChatResponse(List<Product> products){

        return ChatResponse.<List<ProductResponse>>builder()
                .messageType(MessageType.productList)
                .productList(products.stream().map((product -> ProductResponse.builder()
                        .title(product.getTitle())
                        .brand(product.getBrand())
                        .url(product.getThumbnailImage())
                        .inStock(product.isInStock())
                        .price(product.getPrice())
                        .productId(product.getId())
                        .category(product.getCategory())
                        .build()))
                .collect(Collectors.toList())).build();
    }


    public ChatResponse searchProductWithCategory(Category category) {

        List<Product>  foundProducts = productService.getAllProductsByCategory(category);
        return productToChatResponse(foundProducts);
    }
}
