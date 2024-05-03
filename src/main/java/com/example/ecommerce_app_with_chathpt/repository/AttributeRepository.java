package com.example.ecommerce_app_with_chathpt.repository;

import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface AttributeRepository extends MongoRepository<Attribute,String> {

    List<Attribute> findByCategory(Category category);

    Optional<Attribute> findByName(String key);

    Optional<Attribute> findByCategoryAndName(Category category , String key);

    List<Attribute> findAllByCategory(Category category);

}
