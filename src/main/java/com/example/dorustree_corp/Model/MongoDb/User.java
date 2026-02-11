package com.example.dorustree_corp.Model.MongoDb;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userRole;
    private String userAddress;
    private String userPhone;

}
