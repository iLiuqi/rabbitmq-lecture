package com.qseven.rabbit.routing;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class ReceiveLogsDirect {

    // 交换器名称
    private static final String EXCHANGE_NAME = "ex-direct_logs";
    // 路由关键字
    private static final String[] routingKeys1 = new String[]{"info"};
    private static final String[] routingKeys2 = new String[]{"warning"};
    private static final String[] routingKeys3 = new String[]{"info", "warning", "error"};

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
//		声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
//		获取匿名队列名称
        String queueName = channel.queueDeclare().getQueue();
//		根据路由关键字进行多重绑定
        for (String severity : routingKeys2) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
            System.out.println("ReceiveLogsDirect exchange:" + EXCHANGE_NAME + ", queue:" + queueName + ", BindRoutingKey:" + severity);
        }
        System.out.println("ReceiveLogsDirect [*] Waiting for messages...");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
