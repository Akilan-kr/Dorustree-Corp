package com.dorustree.dorustree_corp.Model.MongoDb;

import com.dorustree.dorustree_corp.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "Order")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class OrderData {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String orderedUserId;
    private List<OrderItems> orderedItems;
    private Integer totalPrice;
    private OrderStatus orderStatus;

//    public OrderData(List<OrderItems> orderedItems) {
//        this.orderedItems = orderedItems;
//    }

}
