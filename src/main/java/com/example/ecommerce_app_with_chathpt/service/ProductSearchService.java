package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.*;
import com.example.ecommerce_app_with_chathpt.model.dto.ProductResponse;
import com.example.ecommerce_app_with_chathpt.repository.ChatEntityRepository;
import com.example.ecommerce_app_with_chathpt.repository.ProductListRepository;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductSearchService {

    private AttributeService attributeService;
    private AttributeValueService attributeValueService;
    private ProductService productService;


    public ChatEntity searchProduct(Category category,List<AttributeValue> attributeValues){



        List<Product>  foundProducts = productService.getAllProductsByCategoryAndAttributeValue(category.getId(),
                attributeValues.stream().map(AttributeValue::getId).collect(Collectors.toList()));
        System.out.println(productToProductListEntity(foundProducts));
        return productToProductListEntity(foundProducts);
    }


    public List<AttributeValue> mapAttributeValues(List<GptAttributeAndAttributeValuesJsonResponseToMapper> gptAttributeAndAttributeValuesJsonResponseToMappers,Category category){
        List<AttributeValue> attributeValues = new ArrayList<>();
        for (GptAttributeAndAttributeValuesJsonResponseToMapper gptAttributeAndAttributeValuesJsonResponseToMapper : gptAttributeAndAttributeValuesJsonResponseToMappers ){
            String key = gptAttributeAndAttributeValuesJsonResponseToMapper.getKey();

            List<String> values = gptAttributeAndAttributeValuesJsonResponseToMapper.getValues();
            for (String value: values){
                Optional<Attribute> optionalAttribute = attributeService.findByCategoryAndName(category, key);
                if (optionalAttribute.isPresent()) {
                    Optional<AttributeValue> attributeValue = attributeValueService.findByAttributeAndValue(optionalAttribute.get(),value);
                    attributeValue.ifPresent(attributeValues::add);
                }
            }
        }
        return attributeValues;
    }


    private ChatEntity productToProductListEntity(List<Product> products){

        ProductListEntity chatEntity = ProductListEntity.builder()
                .creationTime(Date.from(ZonedDateTime.now().toInstant()))
                .searchProducts(products.stream().map((product -> ProductResponse.builder()
                                .title(product.getTitle())
                                .brand(product.getBrand())
                                .url(product.getThumbnailImage())
                                .inStock(product.isInStock())
                                .price(product.getPrice())
                                .productId(product.getId())
                                .category(product.getCategory())
                        .build()))
                        .collect(Collectors.toList()))
                .returnType("productList")
                .build();
        return chatEntity;
    }






}
