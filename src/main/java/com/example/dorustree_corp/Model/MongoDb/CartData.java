package com.example.dorustree_corp.Model.MongoDb;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.HashMap;
import java.util.Map;
@Document(collection = "Carts")
@NoArgsConstructor
@Data
public class CartData {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String userId;
    private Map<String, Integer> items = new HashMap<>();

    public CartData( Map<String, Integer> items) {
        this.userId = userId;
        this.items = items;
    }
}
