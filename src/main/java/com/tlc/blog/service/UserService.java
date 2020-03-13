package com.tlc.blog.service;

import com.tlc.blog.po.User;

public interface UserService {

    User checkUser(String userName, String password);

}
