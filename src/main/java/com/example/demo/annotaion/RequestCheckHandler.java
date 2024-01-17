package com.example.demo.annotaion;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2023, zaxh Group All Rights Reserved.
 * @since: 2023/12/26/14:30
 * @summary:
 */
public interface RequestCheckHandler {


    void check(String sign, Object params, String [] sortKeys);
}
