package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Dto.AuthResponse;
import com.example.dorustree_corp.Enums.UserRoles;
import com.example.dorustree_corp.Dto.AuthRequest;
import com.example.dorustree_corp.Enums.UserStatusForVendor;
import com.example.dorustree_corp.Model.MongoDb.UserData;
import com.example.dorustree_corp.Model.MySql.BlacklistToken;
import com.example.dorustree_corp.Service.JwtService;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userServiceImplementation;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;



    @Autowired
    public UserController(UserService userServiceImplementation, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userServiceImplementation = userServiceImplementation;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Register new user - PUBLIC", description = "Registration for a new user")
//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserData userData){
        log.info("C: New Register for a User is called");
        userServiceImplementation.addUser(userData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Login user - PUBLIC", description = "Returns token if user Authenticated, if not return Username not found expection")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateAndGenerateGetToken(@Valid @RequestBody AuthRequest authRequest){
        log.info("C: User try to login with the email: {}", authRequest.getUserName());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            log.info("C: User with {} email is Authenticated", authRequest.getUserName());
            String token = jwtService.generateToken(authRequest.getUserName());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new AuthResponse(authRequest.getUserName(), token));
        } else {
            log.error("C: There is no user with {} email is in the Db", authRequest.getUserName());
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @Operation(summary = "Logout to all Users - ADMIN, VENDOR, USER", description = "Returns ok status 200 with logout success message")
    @PreAuthorize("hasAnyRole('USER','VENDOR','ADMIN')")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        log.info("C: User logging out");
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            BlacklistToken blacklistedToken = new BlacklistToken();
            blacklistedToken.setToken(token);
            blacklistedToken.setExpiryDate(jwtService.extractExpiration(token));
            userServiceImplementation.logout(blacklistedToken);
        }

        return ResponseEntity.ok().build();
    }



    @Operation(summary = "Get all users - ADMIN", description = "Returns a list of users")
//    @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getusers")
    public ResponseEntity<List<UserData>> getAllUsers(){
        log.info("C: Get all the users Details is called by the Admin");
        return ResponseEntity.ok(userServiceImplementation.getAllUsers());
    }

    @Operation(summary = "Get users based on their id - ADMIN", description = "Returns a User based on UserId")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getuser/{id}")
    public ResponseEntity<UserData> getUserById(@PathVariable String id){
        log.info("C: Get user by their user Id is called by Admin, for user Id:{}", id);
        return ResponseEntity.ok(userServiceImplementation.getUserById(id));
    }

    @Operation(summary = "User/Admin update the user data - ADMIN, USER", description = "Returns message updated")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/updateuser")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserData userData){
        log.info("C: User({}) call update user to update their data", userData.getId());
        userServiceImplementation.updateUser(userData);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all users based on the User Status - ADMIN", description = "Returns a list of users based on Roles")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getalluserbystatus/{userrole}")
    public ResponseEntity<List<UserData>> getAllUserByRoles(@PathVariable UserRoles userrole){
        log.info("C: Get users based on the userRole is called by the Admin: for role: {}", userrole);
        return ResponseEntity.ok(userServiceImplementation.getAllUsersByRole(userrole));
    }

    @Operation(summary = "User Request to Admin to became Vendor - USER", description = "Returns a message request sent")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/requesttobecamevendor")
    public ResponseEntity<?> requestToBecameVendor(){
        log.info("C: User send a request to became vendor");
        userServiceImplementation.requestToBecameVendor();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Get all Users Based on the request details status - ADMIN", description = "Returns a list of users based on the user status for vendor")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getallrequestdetails/{userstatusforvendor}")
    public ResponseEntity<List<UserData>> getAllRequestDetails(@PathVariable UserStatusForVendor userstatusforvendor){
        log.info("C: Get all User Request Details by Admin");
        return ResponseEntity.ok(userServiceImplementation.getAllRequestDetails(userstatusforvendor));
    }

    @Operation(summary = "Promoting the user based on UserId with userStatus - ADMIN", description = "Returns a message that user promoted or not")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promote/{userid}/{userstatusforvendor}")
    public ResponseEntity<?> promoteUserToVendor(@PathVariable String userid,@PathVariable UserStatusForVendor userstatusforvendor){
        log.info("C: Admin Trying to promote/reject the user({}) to vendor", userid);
        userServiceImplementation.promoteUserToVendor(userid, userstatusforvendor);
        return ResponseEntity.ok().build();
    }

}
