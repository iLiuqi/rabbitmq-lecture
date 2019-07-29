package com.qseven.rabbit.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "ex-direct_logs";
    // 路由关键字
    private static final String[] routingKeys = new String[]{"info", "warning", "error"};

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        // 发送消息
/*
        for (String severity : routingKeys) {
            String message = "Hello, my level is " + severity;
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            System.out.println("Send '[" + severity + "]':'" + message + "'");
        }
*/
        String severity = routingKeys[1];
        String message = "Hello, my level is " + severity;
        channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
        System.out.println("Send '[" + severity + "]':'" + message + "'");

        channel.close();
        connection.close();
    }

}
