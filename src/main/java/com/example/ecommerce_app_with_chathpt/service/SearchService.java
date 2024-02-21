package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.util.mapper.SearchAttributeKeyValueJsonMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class SearchService {

    private CategoryService categoryService;


    private AttributeValueService attributeValueService;

    private AttributeService attributeService;


    private ChatGPTService chatGPTService;

    public ResponseEntity<Object> searchByRequest(String message){

        List<Category> getAllCategoriesByParent = categoryService.findByParentCategoryIsNull();
        Optional<Category> optionalCategory = Optional.empty();

        boolean flag=true;
        while(flag) {
            String manipulatedMessage ="These are categories of my system "+ getAllCategoriesByParent.toString()+".according to user request, return just only the best suitable categoryName.Please do not send a sentence, must only be categoryName.If any of the categories from my system doesn't match with User Request then must return only empty string. User Request = "+ message;
            String categoryResponse = chatGPTService.categoryExtraction(manipulatedMessage);
            optionalCategory = categoryService.getCategoryByCategoryName(categoryResponse);
            if (optionalCategory.isPresent()) {
                getAllCategoriesByParent = categoryService.getCategoryByParentCategory(optionalCategory.get());
                if(getAllCategoriesByParent.isEmpty()) {
                    flag = false;

                }
            }
            else {
                flag=false;
            }

        }
        // TODO Category checks
        Category category = optionalCategory.get();
        // Bütün key , valueları bul lan!


        List<Attribute> attributeList = attributeService.getAllAttributesByCategory(category);

        Map<Attribute,List<AttributeValue>> possibleAttributeValueList = new HashMap<>();

        for (Attribute attribute : attributeList) {
            List<AttributeValue> attributeValueList = attributeValueService.getAllByAttribute(attribute);

            possibleAttributeValueList.put(attribute, attributeValueList);

        }
        List<SearchAttributeKeyValueJsonMapper> attributeKeyValueJsonMapperList = new ArrayList<>();
        for (Attribute key : possibleAttributeValueList.keySet()) {
            List<AttributeValue> values = possibleAttributeValueList.get(key);

            SearchAttributeKeyValueJsonMapper attributeKeyValueJsonMapper = new SearchAttributeKeyValueJsonMapper();
            attributeKeyValueJsonMapper.setKey(key.getName());

            attributeKeyValueJsonMapper.setValues(new ArrayList<>());

            for (AttributeValue value : values) {
                attributeKeyValueJsonMapper.getValues().add(value.getValue());
            }
            attributeKeyValueJsonMapperList.add(attributeKeyValueJsonMapper);


        }

        String prompt = attributeKeyValueJsonMapperList.toString();

        String manipulatedPrompt = "Give me objects that are the best suitable from Attributes according to User Request in format that Object of Attributes is sent into array" +"User Request:"+ message + ". Attributes:"+ prompt + ".";
        System.out.println(prompt);


        return new ResponseEntity<>(prompt, HttpStatus.OK);







    }
}
