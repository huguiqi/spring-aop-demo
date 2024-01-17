package com.example.demo.rest;

import com.example.demo.annotaion.CheckSign;
import com.example.demo.annotaion.aspect.SNTYSignHandler;
import com.example.demo.bean.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/01/11/16:25
 * @summary:
 */
//@RestController
@Controller
public class UserRestController{

    Logger logger = LoggerFactory.getLogger(UserRestController.class);

    private  UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    @CheckSign(type = SNTYSignHandler.class,orderKeys= {"userName","userId","password"})
    public @ResponseBody Map<String,Object> test(Map<String,Object> params){
        logger.debug("start restController test.......");
//        User user = new User();
//        user.setId(11);
//        user.setUserName("小黑");
//        user.setPassword("密码");
//        userService.createUser(user);

        Map<String,Object> map = new HashMap<>();
        map.put("userName", "xiaohei");
        map.put("password", "xiaohei");
        return params;
    }

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(){

        return "/index";
    }

}
