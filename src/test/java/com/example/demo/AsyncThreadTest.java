package com.example.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/03/12/15:24
 * @summary:
 */
public class AsyncThreadTest {


    private static final ExecutorService EXECUTOR =  new ThreadPoolExecutor(
            5,
            5,
            3000L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        EXECUTOR.submit(()->{
            //Thread.currentThread().setName("线程A");
            //while (true){
                System.out.println("开始请求接口,Thread-Name--"+Thread.currentThread().getName());
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("完成接口请求,Thread-Name--"+Thread.currentThread().getName());
            //}
        });
    }

}
