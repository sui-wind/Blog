package com.tlc.blog.service;

import com.tlc.blog.dao.UserRepository;
import com.tlc.blog.po.User;
import com.tlc.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String userName, String password) {
        User user = userRepository.findByUserNameAndPassword(userName, MD5Utils.code(password));
        return user;
    }
}
