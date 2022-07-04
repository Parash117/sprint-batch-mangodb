package com.pgrg.springbatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.pgrg.springbatch.repo.primary"},
        mongoTemplateRef = PrimaryDbConfig.MONGO_TEMPLATE
)
public class PrimaryDbConfig {
    protected static final String MONGO_TEMPLATE = "primaryMongoTemplate";
}
