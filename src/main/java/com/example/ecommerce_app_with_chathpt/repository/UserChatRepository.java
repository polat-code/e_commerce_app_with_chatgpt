package com.example.ecommerce_app_with_chathpt.repository;

import com.example.ecommerce_app_with_chathpt.model.AttributeValue;
import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.model.UserChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatRepository extends MongoRepository<UserChat, String> {

    @Query(value = "{ '_id': ?0 }", fields = "{ 'attributeValues': 1, '_id': 0 }")
    List<AttributeValue> findAttributeValuesByChatId(String chatId);

    @Query(value = "{ '_id': ?0 }", fields = "{ 'category': 1, '_id': 0 }")
    Optional<Category> findChatCategoryByChatId(String chatId);
}
