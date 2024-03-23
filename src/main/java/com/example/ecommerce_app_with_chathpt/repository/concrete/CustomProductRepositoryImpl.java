package com.example.ecommerce_app_with_chathpt.repository.concrete;
import com.example.ecommerce_app_with_chathpt.model.Product;
import com.example.ecommerce_app_with_chathpt.repository.CustomProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomProductRepositoryImpl implements CustomProductRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Product> findProductsByCategoryAndAttributeValues(String categoryId, List<String> attributeValueIds) {
        Query query = new Query();
        ObjectId categoryObjectId = new ObjectId(categoryId);
        List<ObjectId> attributeValueObjectIds = attributeValueIds.stream().map(ObjectId::new).toList();
        query.addCriteria(Criteria.where("category.$id").is(categoryObjectId)
                .andOperator(Criteria.where("attributeValues.$id").in(attributeValueObjectIds)));

        return mongoTemplate.find(query, Product.class);
    }
}
