package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryDeterminationService {

    private CategoryService categoryService;
    private ChatGPTService chatGPTService;




    public Category determineCategory(String message){
        Set<String> allCategoriesByParent = new HashSet<>(categoryService.findByParentCategoryIsNull().stream()
                .map(category -> category.toString().replace("\"", "")) // Remove quotation marks
                .collect(Collectors.toList()));
        Optional<Category> optionalCategory = Optional.empty();
        boolean flag = true;
        while (flag) {
            String manipulatedMessage = "These are categories of my system " + allCategoriesByParent +
                    ".according to user request, return just only the best suitable categoryName.Please do not send a sentence, must only be categoryName" +
                    "[{'input': 'I'm looking for smartphones.', 'output': Electronics}," +
                    "{'input': 'Show me running shoes.', 'output': Sportswear}," +
                    "{'input': 'I need a new bookshelf.', 'output': Furniture}," +
                    "{'input': 'Where can I find kitchen utensils?', 'output': Kitchen & Dining}," +
                    "{'input': 'I want to buy a dress.', 'output': Clothing}]";
            System.out.println(allCategoriesByParent);
            String categoryResponse = chatGPTService.sendRequestToChatGPT(message, manipulatedMessage);
            System.out.println(categoryResponse);
            String fixedCategoryResponse = categoryResponse.replace("\"", "");


            if (!allCategoriesByParent.contains(fixedCategoryResponse)){
                optionalCategory = categoryService.getCategoryByCategoryNameAndParentCategoryName(fixedCategoryResponse, optionalCategory.get());
                break;
            }

            if (optionalCategory.isPresent()) {
                optionalCategory = categoryService.getCategoryByCategoryNameAndParentCategoryName(fixedCategoryResponse, optionalCategory.get());
            } else {
                optionalCategory = categoryService.getCategoryByCategoryName(fixedCategoryResponse);
            }

            if (optionalCategory.isPresent()) {
                allCategoriesByParent = new HashSet<>(categoryService.getCategoryByParentCategory(optionalCategory.get()).stream()
                        .map(category -> category.toString().replace("\"", "")) // Remove quotation marks
                        .collect(Collectors.toList()));
                if (allCategoriesByParent.isEmpty()) {
                    flag = false;
                }
            } else {
                flag = false;
            }
        }

        if (optionalCategory.isEmpty()) {
            throw new RuntimeException("Category is not found ");
        }
        return optionalCategory.get();

    }
}
