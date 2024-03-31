package com.example.ecommerce_app_with_chathpt.repository;


import com.example.ecommerce_app_with_chathpt.model.UserCart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCartRepository extends MongoRepository<UserCart, String> {


    Optional<UserCart> findUserCartByUser_Id(String userId);
}
