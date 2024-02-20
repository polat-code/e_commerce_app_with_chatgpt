package com.example.ecommerce_app_with_chathpt.service;

import com.example.ecommerce_app_with_chathpt.dto.AttributeRepository;
import com.example.ecommerce_app_with_chathpt.model.Attribute;
import com.example.ecommerce_app_with_chathpt.model.Category;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AttributeService {

    private AttributeRepository attributeRepository;

    public List<Attribute> getAll(){
        return attributeRepository.findAll();
    }

    public List<Attribute> findByCategory(Category category){
        return attributeRepository.findByCategory(category);
    }

    public Attribute findByName(String key){
        return attributeRepository.findByName(key);
    }

    public Optional<Attribute> findByCategoryAndName(Category foundCategory, String key) {
        return attributeRepository.findByCategoryAndName(foundCategory,key);
    }

    public Attribute createAndSaveAttribute(Optional<Category> foundCategory, String key) {
        Attribute attribute = Attribute.builder().category(foundCategory.get()).name(key).build();
        return attributeRepository.save(attribute);
    }
}
