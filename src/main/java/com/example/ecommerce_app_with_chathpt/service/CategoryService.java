package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.repository.CategoryRepository;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.model.dto.request.CategoryRequest;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<?> saveAllCategory(List<CategoryRequest> categoryList){

        for (CategoryRequest categoryRequest : categoryList) {
            Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryRequest.getName());
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                checkParentAndSaveCategory(categoryRequest, category);
            }
            else {
                Category addedCategory = new Category();
                addedCategory.setCategoryName(categoryRequest.getName());
                checkParentAndSaveCategory(categoryRequest, addedCategory);
            }
        }
        return new ResponseEntity<>("Save operation is done by successfully", HttpStatus.ACCEPTED);
    }

    private void checkParentAndSaveCategory(CategoryRequest categoryRequest, Category category) {
        if (categoryRequest.getParent() != null) {
            Optional<Category> optionalParentCategory = categoryRepository.findByCategoryName(categoryRequest.getParent());
            if (optionalParentCategory.isPresent()) {
                category.setParentCategory(optionalParentCategory.get());
            } else {
                Category newParentCategory = Category.builder().categoryName(categoryRequest.getParent()).build();
                Category responseNewParentCategory = categoryRepository.save(newParentCategory);
                category.setParentCategory(responseNewParentCategory);
            }

        }
        categoryRepository.save(category);
    }

    public void deleteByCategoryId(String categoryName) {

        if(categoryName!=null) {
            categoryRepository.deleteById(categoryRepository.findByCategoryName(categoryName).get().getId());
        }
        else
            throw new RuntimeException();
    }


    public Optional<Category> getCategoryByCategoryName(String categoryName) {

        return categoryRepository.findByCategoryName(categoryName);
    }

    public List<Category> findByParentCategoryIsNull(){

        return categoryRepository.findByParentCategoryIsNull();


    }

    public List<Category> getCategoryByParentCategory(Category category) {

        return categoryRepository.findByParentCategory(category);
    }
}
