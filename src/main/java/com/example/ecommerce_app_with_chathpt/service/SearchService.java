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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchService {

    private CategoryService categoryService;


    private AttributeValueService attributeValueService;

    private AttributeService attributeService;


    private ChatGPTService chatGPTService;

    public ResponseEntity<Object> searchByRequest(String message){


        List<String> getAllCategoriesByParent = categoryService.findByParentCategoryIsNull().stream()
                .map(Category::toString)
                .collect(Collectors.toList());
        Optional<Category> optionalCategory = Optional.empty();

        //TODO Eğer bi alt seviyesinde ilişkili kategori bulamazsa o kategori kalamsını sağlayan bir yapı oluştur
        boolean flag=true;
        while(flag) {
            String manipulatedMessage ="These are categories of my system "+ getAllCategoriesByParent+".according to user request, return just only the best suitable categoryName.Please do not send a sentence, must only be categoryName. User Request = "+ message;
            String categoryResponse = chatGPTService.sendRequestToChatGPT(manipulatedMessage);
            optionalCategory = categoryService.getCategoryByCategoryName(categoryResponse);
            if (optionalCategory.isPresent()) {
                getAllCategoriesByParent = categoryService.getCategoryByParentCategory(optionalCategory.get()).stream()
                        .map(Category::toString)
                        .collect(Collectors.toList());
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
        // Bütün key , valueları bul!


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

        String manipulatedPrompt = "I have a user request and I have the features of a category." +
                " I need to extract the features of this category according to the user's request." +
                " Do not return to me features that are not requested by the user." +
                " You just have to return me the features the user requested in the format I sent them.must return only array" +
                "For example, User Request: I want to buy for a Nail Care Products,Features:{" +
                "    \"features\": [" +
                "        {\"key\": \"Item Weight\", \"values\": [\"23 pounds\"]}," +
                "        {\"key\": \"Country of Origin\", \"values\": [\"China\"]}" +
                "    ]" +
                "}" +
                "Response: []" +
                "For example, User Request: I want to buy for a 23 pound and American Nail Care Products,Features:{" +
                "    \"features\": [" +
                "        {\"key\": \"Item Weight\", \"values\": [\"23 pounds\"]}," +
                "        {\"key\": \"Country of Origin\", \"values\": [\"China\"]}" +
                "    ]" +
                "}" +
                "Response:  [{\"key\": \"Item Weight\", \"values\": [\"23 pounds\"]},\"]"+
                "If there are no features, send me an empty string." +
                "Notice that returned features are from my features." +"User Request:"+ message + ". Features:"+ prompt + ".";
        String attributeResponseFromChatGPT = chatGPTService.sendRequestToChatGPT(manipulatedPrompt);
        System.out.println(attributeResponseFromChatGPT);


        return new ResponseEntity<>(prompt, HttpStatus.OK);







    }
}
