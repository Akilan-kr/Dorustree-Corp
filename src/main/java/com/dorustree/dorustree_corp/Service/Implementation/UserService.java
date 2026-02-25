package com.dorustree.dorustree_corp.Service.Implementation;

import com.dorustree.dorustree_corp.Model.MongoDb.OrderItems;
import com.dorustree.dorustree_corp.Dto.UserResponse;
import com.dorustree.dorustree_corp.Dto.UserRequest;
import com.dorustree.dorustree_corp.Dto.VendorStatsDtoResponse;
import com.dorustree.dorustree_corp.Enums.OrderStatus;
import com.dorustree.dorustree_corp.Enums.ProductDeleteStatus;
import com.dorustree.dorustree_corp.Enums.UserRoles;
import com.dorustree.dorustree_corp.Enums.UserStatusForVendor;
import com.dorustree.dorustree_corp.Mappers.UserMapper;
import com.dorustree.dorustree_corp.Model.MongoDb.OrderData;
import com.dorustree.dorustree_corp.Model.MongoDb.UserData;
import com.dorustree.dorustree_corp.Model.MySql.BlacklistToken;
import com.dorustree.dorustree_corp.Repository.MongoDb.OrderRepository;
import com.dorustree.dorustree_corp.Repository.MongoDb.UserRepository;
import com.dorustree.dorustree_corp.Repository.MySql.BlacklistTokenRepository;
import com.dorustree.dorustree_corp.Repository.MySql.ProductRepository;
import com.dorustree.dorustree_corp.Service.Interfaces.IAuthenticationFacade;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements com.dorustree.dorustree_corp.Service.Interfaces.IUserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final IAuthenticationFacade AuthenticationFacade;

    private final BlacklistTokenRepository blacklistTokenRepository;

    private final UserMapper userMapper;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder, IAuthenticationFacade AuthenticationFacade, BlacklistTokenRepository blacklistTokenRepository, UserMapper userMapper, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.AuthenticationFacade = AuthenticationFacade;
        this.blacklistTokenRepository = blacklistTokenRepository;
        this.userMapper = userMapper;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public String findByUserId() {
        try {
            String loggedInUserEmail = AuthenticationFacade.getAuthentication().getName();
            Optional<UserData> loggedInUser = userRepository.findByUserEmail(loggedInUserEmail);
            log.info("S: Get the User Data based on the login Id: {}", loggedInUserEmail);
            return loggedInUser.get().getId();
        }catch(Exception e) {
            throw new UsernameNotFoundException("User not founded", e);
        }
    }

    @Override
    public UserRoles findUserRole(@NotBlank String userName){
        Optional<UserData> userDataOptional = userRepository.findByUserEmail(userName);
        if(userDataOptional.isEmpty()){
            log.error("S: No User founded with userEmail({})", userName);
            throw new UsernameNotFoundException("no user founded with this email");
        } else
            return userDataOptional.get().getUserRole();
    }


    @Override
    public void addUser(UserRequest userRequest) {
        UserData userData = userMapper.toEntity(userRequest);
        userData.setUserPassword(encoder.encode(userData.getUserPassword()));
        log.info("S: New user is register in db");
        userRepository.save(userData);
    }

    @Override
    public void logout(BlacklistToken blacklistedToken) {
        log.info("S: User logged out the token is save in a Blacklist");
        blacklistTokenRepository.save(blacklistedToken);
    }

    @Override
    public UserResponse getUser() {
        String loggedInUser = findByUserId();
        UserData user = userRepository.findById(loggedInUser).orElseThrow(() ->
                new RuntimeException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public VendorStatsDtoResponse getVendorStats() {
        String loggedInUser = findByUserId();
        Long totalProducts = productRepository.countByVendorIdAndStatus(loggedInUser, ProductDeleteStatus.NOT_DELETED);

        List<OrderData> orders = orderRepository.findByVendorAndStatus(loggedInUser, OrderStatus.Order_Received);
        int totalQuantity = 0;
        int totalAmount = 0;

        for (OrderData order : orders) {
            for (OrderItems item : order.getOrderedItems()) {
                if (loggedInUser.equals(item.getProductVendorId())) {
                    totalQuantity += item.getProductQuantity();
                    totalAmount += item.getProductQuantity() * item.getProductPrice();
                }
            }
        }

        return new VendorStatsDtoResponse(totalProducts, totalQuantity, totalAmount);
    }

    @Override
    public List<UserData> getAllUsers() {
        log.info("S: Get all the users");
        return userRepository.findAll();
    }

    @Override
    public UserData getUserById(String id) {
        Optional<UserData> userDataOptional = userRepository.findById(id);
        if(userDataOptional.isEmpty()){
            log.error("S: No user Founded with the Id:{}", id);
            throw new UsernameNotFoundException("no user founded");
        } else {
            log.info("S: Getting the user data for the Id: {}", id);
            return userDataOptional.get();
        }

    }

    @Override
    public void updateUser(UserData userData) {
        log.info("S: Update the user Data");
        userRepository.save(userData);
    }

    @Override
    public List<UserData> getAllUsersByRole(UserRoles userrole) {
        log.info("S: Getting all the User based on the Role({})", userrole);
        return userRepository.findAllByUserRole(userrole);
    }

    @Override
    public void requestToBecameVendor() {

        UserData userData = getUserById(findByUserId());
        userData.setUserStatusForVendor(UserStatusForVendor.Status_Pending);
        updateUser(userData);
    }

    @Override
    public List<UserData> getAllRequestDetails(UserStatusForVendor userstatusforvendor) {
        return userRepository.findAllByUserStatusForVendor(userstatusforvendor);
    }

    @Override
    public void promoteUserToVendor(String userid, UserStatusForVendor userStatusForVendor) {
        UserData userData = getUserById(userid);
        userData.setUserStatusForVendor(userStatusForVendor);
        if(userStatusForVendor == UserStatusForVendor.Status_Approved){
            userData.setUserRole(UserRoles.VENDOR);
        } else
            userData.setUserRole(UserRoles.USER);
        log.info("S: User({}) is promoted to Vendor", userid);
        updateUser(userData);
    }




}
