package com.example.demo.manager.mq.rabbitmq;

import com.example.demo.manager.mq.MQExchangeType;
import com.example.demo.manager.mq.MQPublish;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/02/21/15:59
 * @summary:
 */
public class RabbitMQPublish  implements MQPublish {


    @Override
    public void msg(String exchange, MQExchangeType exchangeType, String routingKey, String message) {
        try {
            //从连接池获取连接
            RabbitmqConnection rabbitmqConnection = RabbitmqConnectionPool.getConnection();
            Connection connection = rabbitmqConnection.getConnection();
            //创建消息通道
            Channel channel = connection.createChannel();
            // 声明exchange中的消息为可持久化，不自动删除
            channel.exchangeDeclare(exchange, exchangeType.convertRabbitMQ(), true, false, null);

            // 发布消息
            channel.basicPublish(exchange, routingKey, null, message.getBytes());
            System.out.println("Publish msg:" + message);
            channel.close();
            RabbitmqConnectionPool.returnConnection(rabbitmqConnection);
        } catch (InterruptedException | IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }
}
