package com.example.demo.bean;

import com.example.demo.service.UserService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/01/11/15:58
 * @summary:
 */
public class TestUserServiceBean {

    @Test
    public void userServiceBeanLoadTest() throws Exception {

        //读取配置文件
        ClassPathXmlApplicationContext conf=new ClassPathXmlApplicationContext("/WEB-INF/applicationContext.xml");
        UserService userService = (UserService) conf.getBean( "userService");
        User user = new User();
        user.setId(1);
        user.setUserName("xiaohei");
        user.setPassword("123456");

        System.out.println(userService.createUser(user));

    }
}
