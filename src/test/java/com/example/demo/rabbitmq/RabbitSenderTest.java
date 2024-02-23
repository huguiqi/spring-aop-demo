package com.example.demo.rabbitmq;

import com.example.demo.manager.mq.MQExchangeType;
import com.example.demo.manager.mq.MQPublish;
import com.example.demo.manager.mq.rabbitmq.RabbitMQConsumer;
import com.example.demo.manager.mq.rabbitmq.RabbitMQPublish;
import com.example.demo.manager.mq.rabbitmq.RabbitmqConnectionPool;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/02/21/16:06
 * @summary:
 */
public class RabbitSenderTest {


    public static void main(String[] args) throws InterruptedException {

        RabbitmqConnectionPool.initRabbitmqConnectionPool("192.168.1.199", 5672, "root", "root", "host.zaxh.pass", 2);

        MQPublish producer = new RabbitMQPublish();
        for (int i = 0; i < 10; i++) {
            producer.msg("shnw.snbt.exchange.tips", MQExchangeType.DIRECT, "shnw.snbt.key.tips.approvel", "hello,fuck you!!,第" + i + "次");
        }


        RabbitmqConnectionPool.close();


    }

}
