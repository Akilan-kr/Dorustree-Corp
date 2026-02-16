package com.example.dorustree_corp.Model.MongoDb;


import com.example.dorustree_corp.Enums.UserRoles;
import com.example.dorustree_corp.Enums.UserStatusForVendor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserData {
    @MongoId(FieldType.OBJECT_ID)
    private String Id;
    @NotBlank(message = "Username cannot be null or empty")
    private String userName;
    @NotBlank(message = "UserEmail cannot be null or empty")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email format is invalid (e.g., example@domain.com)"
    )
//    @Email(message = "Please provide a valid email address") //not better becuause it sometime allow email@domain   without .com
    private String userEmail;
    @NotBlank(message = "UserPassword cannot be null or empty")
    private String userPassword;
    private UserRoles userRole = UserRoles.USER;
    private UserStatusForVendor userStatusForVendor = UserStatusForVendor.Status_None;
    @NotBlank(message = "userAddress cannot be null or blank")
    private String userAddress;
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String userPhone;

    public UserData(String userName, String userEmail, String userPassword, String userAddress, String userPhone) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAddress = userAddress;
        this.userPhone = userPhone;
    }
}
