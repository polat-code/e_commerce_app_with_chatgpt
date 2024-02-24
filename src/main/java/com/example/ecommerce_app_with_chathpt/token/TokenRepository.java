package com.example.ecommerce_app_with_chathpt.token;

import com.example.ecommerce_app_with_chathpt.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    Optional<Token> findByToken(String token);

    List<Token> findAllValidTokenByUser(User user);
}
