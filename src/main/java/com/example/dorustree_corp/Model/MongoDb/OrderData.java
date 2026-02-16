package com.example.dorustree_corp.Model.MongoDb;

import com.example.dorustree_corp.Enums.OrderStatus;
import com.example.dorustree_corp.Model.OrderItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "Order")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class OrderData {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String orderedUserId;
    private List<OrderItems> orderedItems;
    private Integer totalPrice;
    private OrderStatus orderStatus;

    public OrderData(List<OrderItems> orderedItems) {
        this.orderedItems = orderedItems;
    }


}
