package com.example.demo.service;

import com.example.demo.bean.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/01/11/15:54
 * @summary:
 */
public class UserService {


    public String createUser(User user) {
        return user.getUserName()+"----"+user.getPassword();
    }
}
