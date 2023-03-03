package com.neu.cs6650.twinder_server_sb;

import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMQService {

    private final static String EXCHANGE_NAME = "rabbitMQ";
    private Connection connection;
    private Channel channel;
    public RabbitMQService() throws TimeoutException, IOException {
        init();
    }

    private void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        //localhost
//        factory.setHost("localhost");
        factory.setHost("35.80.121.164");
        factory.setPort(5672);
        this.connection = factory.newConnection();
        this.channel = this.connection.createChannel();
//        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    }

    public void send(SwipeDetailsForConsumer swipeDetailsForConsumer) throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        Gson gson = new Gson();
        String message = gson.toJson(swipeDetailsForConsumer);
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
//        System.out.println(" [x] Sent '" + message + "'");
    }
}
