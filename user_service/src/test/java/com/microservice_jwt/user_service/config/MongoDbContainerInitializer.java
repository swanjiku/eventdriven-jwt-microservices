package com.microservice_jwt.user_service.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoDbContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(
            DockerImageName.parse("mongo:6.0") // or another version like "mongo:5.0"
    );

    static {
        mongoDBContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                context,
                "spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl()
        );
    }
}
