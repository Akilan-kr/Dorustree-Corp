package com.example.dorustree_corp.Controller;

import com.example.dorustree_corp.Model.MongoDb.User;
import com.example.dorustree_corp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userServiceImplementation;

    @Autowired
    public UserController(UserService userServiceImplementation1) {
        this.userServiceImplementation = userServiceImplementation1;
    }

    @PostMapping("/adduser")
    public String addUser(@RequestBody User user){
        userServiceImplementation.addUser(user);
        return "user Added";
    }

    @GetMapping("/getusers")
    public List<User> getAllUsers(){
        return userServiceImplementation.getAllUsers();
    }

    @GetMapping("/getuser/{id}")
    public User getUserById(@PathVariable Long id){
        return userServiceImplementation.getUserById(id);
    }

    @PutMapping("/updateuser")
    public String updateUser(@RequestBody User user){
        userServiceImplementation.updateUser(user);
        return "updated";
    }
}
