package com.example.ecommerce_app_with_chathpt.dto;

import com.example.ecommerce_app_with_chathpt.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository  extends MongoRepository<Category,String> {



    Optional<Category> findByCategoryName(String categoryName);


}
