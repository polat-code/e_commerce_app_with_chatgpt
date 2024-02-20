package com.example.ecommerce_app_with_chathpt.dto;

import com.example.ecommerce_app_with_chathpt.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository  extends MongoRepository<Category,String> {



    Optional<Category> findByCategoryName(String categoryName);


}
