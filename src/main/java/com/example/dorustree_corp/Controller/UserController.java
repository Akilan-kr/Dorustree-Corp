package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Enums.UserRoles;
import com.example.dorustree_corp.Dto.AuthRequest;
import com.example.dorustree_corp.Model.MongoDb.UserData;
import com.example.dorustree_corp.Service.JwtService;
import com.example.dorustree_corp.Service.Interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/adduser")
    public String addUser(@Valid @RequestBody UserData userData){
        userServiceImplementation.addUser(userData);
        return "user Added";
    }

    @PostMapping("/login")
    public String authenticateAndGenerateGetToken(@Valid @RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUserName());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getusers")
    public List<UserData> getAllUsers(){
        return userServiceImplementation.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getuser/{id}")
    public UserData getUserById(@PathVariable String id){
        return userServiceImplementation.getUserById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateuser")
    public String updateUser(@Valid @RequestBody UserData userData){
        userServiceImplementation.updateUser(userData);
        return "updated";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getalluserbystatus/{userrole}")
    public List<UserData> getAllUserByRoles(@PathVariable UserRoles userrole){
        return userServiceImplementation.getAllUsersByRole(userrole);
    }


}
