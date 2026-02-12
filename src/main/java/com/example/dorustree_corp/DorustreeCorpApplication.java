package com.example.dorustree_corp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DorustreeCorpApplication {

	public static void main(String[] args) {

		SpringApplication.run(DorustreeCorpApplication.class, args);
	}

}
