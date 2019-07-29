package com.qseven.rabbit.publish;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class ReceiveLogs {

    private static final String EXCHANGE_NAME = "ex-logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();   // 随机生成的一个队列名称
        // queue exchange绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("ReceiveLogs [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("ReceiveLogs [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
