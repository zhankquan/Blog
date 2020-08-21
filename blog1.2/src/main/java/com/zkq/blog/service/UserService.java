package com.zkq.blog.service;

import com.zkq.blog.po.User;

public interface UserService {

    User checkUser(String username,String password);
}
