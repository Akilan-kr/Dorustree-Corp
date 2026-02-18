package com.example.dorustree_corp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableMethodSecurity
@EnableAsync
@EnableCaching
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DorustreeCorpApplication {

	public static void main(String[] args) {

		SpringApplication.run(DorustreeCorpApplication.class, args);
	}

}
