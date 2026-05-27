package com.starhealth.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.starhealth.user.entity.User;
import com.starhealth.user.service.IUserServce;
import com.starhealth.user.vo.UserDepartmentVO;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    IUserServce service;

    
    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        return service.addUser(user);
    }

   
    @GetMapping("/get/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return service.getUserById(userId);
    }

    
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }


    @PutMapping("/update/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        return service.updateUser(userId, user);
    }

   
    @DeleteMapping("/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
        return "User deleted successfully";
    }


    @GetMapping("/get/user-dept/{userId}")
    public UserDepartmentVO getUserWithDept(@PathVariable Long userId) {
        return service.getUserWithDept(userId);
    }
}