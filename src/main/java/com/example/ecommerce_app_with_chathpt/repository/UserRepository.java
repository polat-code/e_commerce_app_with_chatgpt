package com.example.ecommerce_app_with_chathpt.repository;

import com.example.ecommerce_app_with_chathpt.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
