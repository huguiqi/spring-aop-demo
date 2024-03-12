package com.example.demo.rabbitmq;

import com.example.demo.manager.mq.MQConsumer;
import com.example.demo.manager.mq.MQExchangeType;
import com.example.demo.manager.mq.MQPublish;
import com.example.demo.manager.mq.rabbitmq.RabbitMQConsumer;
import com.example.demo.manager.mq.rabbitmq.RabbitMQPublish;
import com.example.demo.manager.mq.rabbitmq.RabbitmqConnectionPool;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/02/21/16:06
 * @summary:
 */
public class RabbitConnectionTest {


    public static void main(String[] args) throws InterruptedException {

        RabbitmqConnectionPool.initRabbitmqConnectionPool("192.168.1.199", 5672, "root", "root", "host.zaxh.pass",2);

        //MQPublish producer = new RabbitMQPublish();

        //producer.msg("shnw.snbt.exchange.tips", MQExchangeType.DIRECT,"shnw.snbt.key.tips.approvel","hello,fuck you!!");

        RabbitMQConsumer consumer1 = new RabbitMQConsumer("shnw.snbt.exchange.tips", "shnw.snbt.queue.tips.approvel","shnw.snbt.key.tips.approvel");
        //Thread thread1 = new Thread(consumer1,"线程1");
        //thread1.setDaemon(true);
        //thread1.start();

        //RabbitMQConsumer consumer2 = new RabbitMQConsumer( "shnw.snbt.exchange.tips", "shnw.snbt.queue.tips.approvel","shnw.snbt.key.tips.approvel");
        //Thread thread2 = new Thread(consumer2,"线程2");

        //thread2.setDaemon(true);
        //thread2.start();

        //thread1.join();
        //thread2.join();



        ExecutorService executor = new ThreadPoolExecutor(
                5,
                5,
                3000L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),new ThreadPoolExecutor.AbortPolicy());

        executor.submit(consumer1);

//        RabbitmqConnectionPool.close();



    }

}
