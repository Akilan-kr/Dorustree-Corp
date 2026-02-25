package com.dorustree.dorustree_corp.Mappers;

import com.dorustree.dorustree_corp.Dto.UserResponse;
import com.dorustree.dorustree_corp.Dto.UserRequest;
import com.dorustree.dorustree_corp.Model.MongoDb.UserData;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserData toEntity(UserRequest request){
        return UserData.builder()
                .userName(request.userName())
                .userPassword(request.userPassword())
                .userEmail(request.userEmail())
                .userAddress(request.userAddress())
                .userPhone(request.userPhone())
                .build();

    }

    public UserResponse toResponse(UserData userData){
        return new UserResponse(
                userData.getId(),
                userData.getUserName(),
                userData.getUserEmail(),
                userData.getUserRole(),
                userData.getUserStatusForVendor(),
                userData.getUserAddress(),
                userData.getUserPhone(),
                userData.getCreatedAt()

        );
    }
}
