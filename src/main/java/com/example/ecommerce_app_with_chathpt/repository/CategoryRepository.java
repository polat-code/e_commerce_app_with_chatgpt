package com.example.ecommerce_app_with_chathpt.repository;

import com.example.ecommerce_app_with_chathpt.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository  extends MongoRepository<Category,String> {



    Optional<Category> findByCategoryName(String categoryName);


    List<Category> findByParentCategoryIsNull();

    List<Category> findByParentCategory(Category category);
}
