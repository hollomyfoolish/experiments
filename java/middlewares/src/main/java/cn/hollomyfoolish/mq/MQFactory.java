package cn.hollomyfoolish.mq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MQFactory {

    public static Connection newConnection() throws IOException, TimeoutException {
        return newConnection(MQConst.VIRTUAL_HOST, null);
    }

    public static Connection newConnection(String name) throws IOException, TimeoutException {
        return newConnection(MQConst.VIRTUAL_HOST, name);
    }

    public static Connection newConnection(String virtualHost, String name) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MQConst.HOST);
        factory.setPort(MQConst.PORT);
        factory.setUsername(MQConst.USER);
        factory.setPassword(MQConst.PASSWORD);
        factory.setVirtualHost(virtualHost);
        return factory.newConnection(name);
    }

    public static ConnectionFactory defaultFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MQConst.HOST);
        factory.setPort(MQConst.PORT);
        factory.setUsername(MQConst.USER);
        factory.setPassword(MQConst.PASSWORD);
        factory.setVirtualHost(MQConst.VIRTUAL_HOST);

        return factory;
    }

    public static ConnectionFactory defaultSpringFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MQConst.HOST);
        factory.setPort(MQConst.PORT);
        factory.setUsername(MQConst.USER);
        factory.setPassword(MQConst.PASSWORD);
        factory.setVirtualHost(MQConst.SPRING_VIRTUAL_HOST);

        return factory;
    }
}
