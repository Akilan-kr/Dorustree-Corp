package com.example.dorustree_corp.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.dorustree_corp.Repository.MySql")
@EnableMongoRepositories(basePackages = "com.example.dorustree_corp.Repository.MongoDb")
public class MultiDbConfig {

}
