package com.example.demo.manager.mq;

import com.rabbitmq.client.BuiltinExchangeType;

public enum MQExchangeType {

    DIRECT("direct"),
    FANOUT("fanout"),
    TOPIC("topic"),
    HEADERS("headers");

    private final String type;

    MQExchangeType(String type) {
        this.type = type;
    }


    public BuiltinExchangeType convertRabbitMQ() {
        return BuiltinExchangeType.valueOf(this.name());
    }
}
