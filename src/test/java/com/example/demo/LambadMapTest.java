package com.example.demo;

import com.example.demo.bean.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/03/12/15:56
 * @summary:
 */
public class LambadMapTest {


    @Test
    public void testToMap(){
        List<User> users = getUsers();
        Map<String,String> sendMap = users.stream().collect(Collectors.toMap(User::getPhone, user -> givenUserNameContent(user),(existingValue, newValue) -> existingValue));

        System.out.println(sendMap);
    }

    private String givenUserNameContent(User user) {
        return user.getUserName()+"，我是你爸爸！！";
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if (i<3){
                users.add(User.builder().id(i).userName("小黑"+(i+1)).phone("1312222065"+i).build());
            }else {
                users.add(User.builder().id(i).userName("小黑"+(i+1)).phone("12122220654").build());
            }
        }
        return users;
    }


}
