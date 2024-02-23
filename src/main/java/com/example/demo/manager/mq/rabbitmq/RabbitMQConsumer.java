package com.example.demo.manager.mq.rabbitmq;

import com.example.demo.manager.mq.MQConsumer;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/02/21/16:36
 * @summary:
 */
public class RabbitMQConsumer implements MQConsumer,Runnable {

    private String exchange,  queueName,  routingKey;



    public RabbitMQConsumer(String exchange, String queueName, String routingKey) {
        this.exchange = exchange;
        this.queueName = queueName;
        this.routingKey = routingKey;
    }

    @Override
    public void run() {
        while(true) {
            // 消费消息

            try {
                //从连接池获取
                RabbitmqConnection rabbitmqConnection = RabbitmqConnectionPool.getConnection();
                Connection connection = rabbitmqConnection.getConnection();
                //创建消息信道
                final Channel channel = connection.createChannel();

                //消息队列
                channel.queueDeclare(this.queueName, true, false, false, null);
                //绑定队列到交换机
                channel.queueBind(this.queueName, this.exchange, this.routingKey);

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String message = new String(body, "UTF-8");
                        System.out.println(Thread.currentThread().getName()+" Consumer msg:" + message);

                        // 获取Rabbitmq消息，并保存到DB
                        // 说明：这里仅作为示例，如果有多种类型的消息，可以根据消息判定，简单的用 if...else 处理，复杂的用工厂 + 策略模式
//                    notifyService.saveArticleNotify(JsonUtil.toObj(message, UserFootDO.class), NotifyTypeEnum.PRAISE);

                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                };
                // 取消自动ack
                channel.basicConsume(this.queueName, false, consumer);
                RabbitmqConnectionPool.returnConnection(rabbitmqConnection);

//            channel.close();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }



    }

}
