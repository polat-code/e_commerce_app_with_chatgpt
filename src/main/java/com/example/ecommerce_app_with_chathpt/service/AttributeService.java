package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.repository.AttributeRepository;
import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AttributeService {

    private AttributeRepository attributeRepository;

    public List<Attribute> getAll(){
        return attributeRepository.findAll();
    }

    public List<Attribute> getAllAttributesByCategory(Category category){
        return attributeRepository.findAllByCategory(category);
    }

    public List<Attribute> findByCategory(Category category){
        return attributeRepository.findByCategory(category);
    }

    public Optional<Attribute> findByName(String key){
        return attributeRepository.findByName(key);
    }

    public Optional<Attribute> findByCategoryAndName(Category foundCategory, String key) {
        return attributeRepository.findByCategoryAndName(foundCategory,key);
    }

    public Attribute createAndSaveAttribute(Optional<Category> foundCategory, String key) {
        Attribute attribute = Attribute.builder().category(foundCategory.get()).name(key).build();
        return attributeRepository.save(attribute);
    }

    public List<Attribute> getAttributeListByListOfAttributeName(Category category,List<String> attributeNames) {
        List<Attribute> attributeList = new ArrayList<>();

        for (String attributeName : attributeNames) {

            Optional<Attribute> optionalAttribute2 = findByCategoryAndName(category, attributeName.replace("\"",""));

            optionalAttribute2.ifPresent(attribute -> attributeList.add(attribute));

        }

        return attributeList;
    }
}
