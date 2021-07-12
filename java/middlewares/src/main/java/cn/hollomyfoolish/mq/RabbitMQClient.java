package cn.hollomyfoolish.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQClient {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        // "guest"/"guest" by default, limited to localhost connections

        String userName = "admin";
        String password = "admin";
        String virtualHost = "/";
        String hostName = "localhost";
        int portNumber = 5672;

        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(hostName);
        factory.setPort(portNumber);

        String connName = "test-connection";
        try (
            Connection conn = factory.newConnection(connName);
            Channel channel = conn.createChannel()
        ) {
            System.out.println("mq connected");
            Thread.sleep(100000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
