package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.*;
import com.example.ecommerce_app_with_chathpt.repository.ChatEntityRepository;
import com.example.ecommerce_app_with_chathpt.repository.ProductListRepository;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductSearchService {

    private AttributeService attributeService;
    private AttributeValueService attributeValueService;
    private ProductService productService;
    private final ProductListRepository productListRepository;
    private final ChatEntityRepository chatEntityRepository;


    public ChatEntity searchProduct(List<GptAttributeAndAttributeValuesJsonResponseToMapper> gptAttributeAndAttributeValuesJsonResponseToMappers, Category category){
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



        List<Product> categoryProducts = productService.getAllProductsByCategory(category);
        //List<List<AttributeValue>> denemeAttributeValues = categoryProducts.stream().map(Product::getAttributeValues).toList();
        //System.out.println(denemeAttributeValues.stream().map(attributeValues1 -> attributeValues1.stream().map(AttributeValue::getId).collect(Collectors.toList())).collect(Collectors.toList()));
        //System.out.println(attributeValues.stream().map(AttributeValue::getId).collect(Collectors.toList()));
        List<List<Product>> matchedProductsForAllAttributeValues = new ArrayList<>();
        Map<Product,Integer> matchedProductsCounter = new HashMap<>();
        for (Product product: categoryProducts){
            List<Product> matchedProducts = new ArrayList<>();
            matchedProductsCounter.put(product,0);

            for (AttributeValue productAttributeValue :product.getAttributeValues()){

                for (AttributeValue attributeValue: attributeValues){

                    if (productAttributeValue.equals(attributeValue)){
                        matchedProducts.add(product);
                        matchedProductsCounter.put(product,matchedProductsCounter.get(product)+1);
                    }
                }

            }
            matchedProductsForAllAttributeValues.add(matchedProducts);


        }

        List<Map.Entry<Product, Integer>> list = new ArrayList<>(matchedProductsCounter.entrySet());
        list.sort(Map.Entry.<Product, Integer>comparingByValue().reversed());

        LinkedHashMap<Product, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Product, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return productToProductListEntity(sortedMap.keySet().stream().toList());
    }



    private ChatEntity productToProductListEntity(List<Product> products){


        ProductListEntity chatEntity = ProductListEntity.builder()
                .creationTime(Date.from(ZonedDateTime.now().toInstant()))
                .searchProducts(new ArrayList<Product>())
                .build();
        for (Product product: products){

            chatEntity.getSearchProducts().add(product);

        }

        return chatEntity;
    }






}
