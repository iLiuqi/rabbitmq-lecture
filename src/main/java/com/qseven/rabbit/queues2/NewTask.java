package com.qseven.rabbit.queues2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class NewTask {

    private final static String QUEUE_NAME = "new_task_2";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

//            Scanner scanner = new Scanner(System.in);
//            System.out.println("请输入要发送的字符串：");
//            final String message = scanner.nextLine();

            String dot = ".";
            String message;
            for (int i = 0; i < 10; i++) {
                // 发送消息到队列中
                message = "NewTask 发布的第 " + (i + 1) + " 个消息" + dot;
                System.out.println("Send Sent : " + message);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                dot = dot.concat(".");
            }
        }
    }

}
