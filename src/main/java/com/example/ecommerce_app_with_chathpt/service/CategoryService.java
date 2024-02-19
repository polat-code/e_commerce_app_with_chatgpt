package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.dto.CategoryRepository;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.model.request.CategoryRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;


    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    public void saveAllCategory(List<CategoryRequest> categoryList){

       for (int i=0 ; i < categoryList.size();i++){
           Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryList.get(i).getName());
           if(optionalCategory.isPresent()){
               Optional<Category> optionalParentCategory = categoryRepository.findByCategoryName(categoryList.get(i).getParent());
               if (optionalParentCategory.isEmpty()){
                   Category category = optionalCategory.get();
                   Category newParentCategory = Category.builder().categoryName(categoryList.get(i).getName()).build();
                   Category responseNewParentCategory = categoryRepository.save(newParentCategory)
                   category.setParentCategoryId(responseNewParentCategory);
                   categoryRepository.save(category);
               }
           }
           else {

           }


           categoryRepository.findByCategoryName(categoryList);
           var category = Category.builder().categoryName(categoryList.get(i).getName());



       }
        //categoryRepository.saveAll(categoryList);


    }

}
