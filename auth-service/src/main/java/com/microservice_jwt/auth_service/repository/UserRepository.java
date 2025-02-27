package com.microservice_jwt.auth_service.repository;

import com.microservice_jwt.auth_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    // Allow search by either username or email
    Optional<User> findByUsernameOrEmail(String username, String email);
}
