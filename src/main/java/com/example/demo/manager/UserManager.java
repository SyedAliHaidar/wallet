package com.example.demo.manager;

import com.example.demo.model.P2P;
import com.example.demo.model.Refund;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;

import java.util.List;

public interface UserManager {
    public Object userAddition(User user) ;
    public Object updateUser(User user) ;
    public Object topUp(Transaction transaction);
    public Object pay(Transaction transaction);
    public List<Object> showPassbook(Integer userId);
    public Object refund(Refund refund);
    public Object balanceChecking(Integer userId);
    public Object p2p(P2P p2p);
}
