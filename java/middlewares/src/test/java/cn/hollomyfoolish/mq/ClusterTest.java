package cn.hollomyfoolish.mq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ClusterTest {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(6672);
        factory.setUsername(MQConst.USER);
        factory.setPassword(MQConst.PASSWORD);
        factory.setVirtualHost(MQConst.VIRTUAL_HOST);

        try(
            Connection connection = factory.newConnection("connection to load balance")
        ){
            new Producer("load balance producer").runWithConnection(connection);
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

}
