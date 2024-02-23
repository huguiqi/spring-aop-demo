package com.example.demo.manager.mq;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2024, zaxh Group All Rights Reserved.
 * @since: 2024/02/21/16:21
 * @summary:
 */
public interface MQPublish {


    public void msg(String exchange,
                           MQExchangeType exchangeType,
                           String routingKey,
                           String message);

}
