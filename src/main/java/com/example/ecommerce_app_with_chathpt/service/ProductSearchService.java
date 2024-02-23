package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.model.Product;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ProductSearchService {

    private AttributeService attributeService;
    private AttributeValueService attributeValueService;
    private ProductService productService;


    public void searchProduct(List<GptAttributeAndAttributeValuesJsonResponseToMapper> gptAttributeAndAttributeValuesJsonResponseToMappers, Category category){
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
        List<List<String>> matchedProductsForAllAttributeValues = new ArrayList<>();
        Map<String,Integer> matchedProductsCounter = new HashMap<>();
        for (Product product: categoryProducts){
            List<String> matchedProducts = new ArrayList<>();
            matchedProductsCounter.put(product.getId(),0);
            for (AttributeValue productAttributeValue :product.getAttributeValues()){

                for (AttributeValue attributeValue: attributeValues){
                    if (productAttributeValue.equals(attributeValue)){
                        matchedProducts.add(product.getId());
                        matchedProductsCounter.put(product.getId(),matchedProductsCounter.get(product.getId())+1);
                    }
                }

            }
            matchedProductsForAllAttributeValues.add(matchedProducts);


        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(matchedProductsCounter.entrySet());
        list.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

    }






}
