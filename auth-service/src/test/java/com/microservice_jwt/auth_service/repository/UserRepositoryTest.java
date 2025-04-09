package com.microservice_jwt.auth_service.repository;

import com.microservice_jwt.auth_service.config.MongoDbContainerInitializer;
import com.microservice_jwt.auth_service.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@ContextConfiguration(initializers = MongoDbContainerInitializer.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_ShouldReturnUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testUser");

        assertTrue(foundUser.isPresent());
    }
}

