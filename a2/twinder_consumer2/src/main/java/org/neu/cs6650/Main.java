package org.neu.cs6650;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.*;

public class Main {
    final static private int NUMTHREADS = 50;
//    final static String LOCAL_BASE_PATH = "localhost";
    final static String EC2_BASE_PATH = "35.80.121.164";

    final static String EXCHANGE_NAME = "rabbitMQ";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Gson gson = new Gson();
        ConcurrentHashMap<String, SwipeStatistics> map = new ConcurrentHashMap<>();
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(EC2_BASE_PATH);
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

//        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        CountDownLatch completed = new CountDownLatch(NUMTHREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMTHREADS);

        for (int i = 0; i < NUMTHREADS; i++) {
            executorService.submit(() -> {
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String swipeDetail = new String(body);
                        SwipeDetailsForConsumer swipeDetailsForConsumer = gson.fromJson(swipeDetail, SwipeDetailsForConsumer.class);
                        String swiper = swipeDetailsForConsumer.getSwiper();
                        String swipee = swipeDetailsForConsumer.getSwipee();
                        String leftOrRight = swipeDetailsForConsumer.getLeftOrRight();
                        map.putIfAbsent(swiper, new SwipeStatistics());

                        if (leftOrRight.equals("right")) {
                            //list of 100 maximum users who the user has swiped right on
                            if(map.get(swiper).getLikesList().size() < 100) {
                                map.get(swiper).getLikesList().add(swipee);
                            }
                        }
                        System.out.println(swiper + ":" + map.get(swiper));
                    }
                };
                try {
                    channel.basicConsume(queueName,true, consumer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                completed.countDown();
            });
        }
        completed.await();
    }
}
