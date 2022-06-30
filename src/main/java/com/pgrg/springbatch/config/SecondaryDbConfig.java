package com.pgrg.springbatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.pgrg.springbatch.repo.secondary"},
        mongoTemplateRef = SecondaryDbConfig.MONGO_TEMPLATE
)
public class SecondaryDbConfig {
    protected static final String MONGO_TEMPLATE = "secondaryMongoTemplate";
}
