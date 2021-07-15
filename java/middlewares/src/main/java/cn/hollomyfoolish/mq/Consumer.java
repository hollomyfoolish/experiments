package cn.hollomyfoolish.mq;

import cn.hollomyfoolish.utils.ServerUtils;
import com.google.gson.Gson;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static final String CONNECTION_NAME = "Connection of Consumer";
    private static final Object lock = new Object();
    private String name;

    public Consumer(String name) {
        this.name = name;
    }

    public void run(){
        try(
                Connection connection = MQFactory.newConnection(this.name + " from " + ServerUtils.getHostName());
        ){
            for(Components component : Components.values()){
                startListening(component, connection);
            }
//            startListening(Components.B1AH, connection);
            System.out.println("main thread go to sleep ...");
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("system exit");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void startListeningX(Components component, Connection connection) {
        System.out.println("component " + component.name() + " is listening ...");
        try (
                Channel channel = connection.createChannel();
        ) {
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicQos(1);
            channel.basicConsume(component.name(), false, "consumer_test", consumer);
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(" [X] Received '" + message + "'");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }

//            channel.queueDeclare(component.name(), true, false, false, null);
//            channel.basicQos(1);
//            channel.basicConsume(component.name(), true, new DefaultConsumer(channel){
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                    String routingKey = envelope.getRoutingKey();
//                    String contentType = properties.getContentType();
//                    long deliveryTag = envelope.getDeliveryTag();
//                    B1Message message = new Gson().fromJson(new String(body, StandardCharsets.UTF_8), B1Message.class);
//                    System.out.println(String.format("[%s] receive a message: %s - %s - %s - %s - %s",
//                            component.name(), routingKey, deliveryTag, contentType, message.getIdx(), message.getMessage()));
////                    channel.basicAck(deliveryTag, false);
//                    System.out.println("handle message " + deliveryTag + " done");
//                }
//            });
//            System.out.println("component " + component.name() + " stop listening ...");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void startListening(Components component, Connection connection) {
        System.out.println("component " + component.name() + " is listening ...");
        try{
            final Channel channel = connection.createChannel();
            channel.queueDeclare(component.name(), true, false, false, null);
            channel.basicQos(64);
            channel.basicConsume(component.name(), false, component.name() + "_Tag", new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    long deliveryTag = envelope.getDeliveryTag();
                    B1Message message = new Gson().fromJson(new String(body, StandardCharsets.UTF_8), B1Message.class);
                    System.out.println(String.format("[%s] receive a message: %s - %s - %s - %s - %s",
                            component.name(), routingKey, deliveryTag, contentType, message.getIdx(), message.getMessage()));
                    channel.basicAck(deliveryTag, false);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Consumer("Consumer 1").run();
    }
}
