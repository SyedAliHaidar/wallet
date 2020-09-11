package com.example.demo.controller;

import com.example.demo.manager.UserManager;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    public UserManager manager;
    @PostMapping("/postNewUser")
    public Object addUser(@RequestBody User user) {
        return manager.userAddition(user);
    }

    @PutMapping("/updateUser")
    public Object updateUser(@RequestBody User user){
        return manager.updateUser(user);
    }

    @PostMapping("/topUp")
    public Object topUp(@RequestBody Transaction transaction) {
        return manager.topUp(transaction);
    }

    @PostMapping("/pay")
    public Object pay(@RequestBody Transaction transaction) {
        return manager.pay(transaction);
    }

    @GetMapping("/showPassbook")
    public List<Object> showPassbook(@RequestBody UserId user_id) {
        int userId = user_id.getUserId();
        return manager.showPassbook(userId);
    }

    @PostMapping("/refund")
    public Object refund(@RequestBody Refund refund) {
        return manager.refund(refund);
    }
    @PostMapping("/p2p")
    public Object p2p(@RequestBody P2P p2p) {
        return manager.p2p(p2p);
    }
    @GetMapping("balanceChecking")
    public Object balanceChecking(@RequestBody UserId user_id) {
        int userId = user_id.getUserId();
        return manager.balanceChecking(userId);
    }
}
