package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.model.Product;
import com.example.ecommerce_app_with_chathpt.util.mapper.GptAttributeAndAttributeValuesJsonResponseToMapper;
import com.example.ecommerce_app_with_chathpt.util.mapper.SearchAttributeKeyValueJsonMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        //TODO AttributeValueList ile eşleşen productları best

    }



}
