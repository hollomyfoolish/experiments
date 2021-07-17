package cn.hollomyfoolish.mq;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class MQFactory {
    private static final Logger logger = LoggerFactory.getLogger(MQFactory.class);

    public static Connection newConnection() throws IOException, TimeoutException {
        return newConnection(MQConst.VIRTUAL_HOST, null);
    }

    public static Connection newConnection(String name) throws IOException, TimeoutException {
        return newConnection(MQConst.VIRTUAL_HOST, name);
    }

    public static Connection newClusterConnection(String name) throws IOException, TimeoutException {
        int idx = new Random().nextInt(MQConst.clusters.size());
        Address address = MQConst.clusters.get(idx);
        logger.info("thread {} choose node {}:{}", Thread.currentThread().getName(), address.getHost(), address.getPort());

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(address.getHost());
        factory.setPort(address.getPort());
        factory.setUsername(MQConst.USER);
        factory.setPassword(MQConst.PASSWORD);
        factory.setVirtualHost(MQConst.VIRTUAL_HOST);
        return factory.newConnection(name);
    }

    public static Connection newConnection(String virtualHost, String name) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(MQConst.USER);
        factory.setPassword(MQConst.PASSWORD);
        factory.setVirtualHost(virtualHost);
        return factory.newConnection(MQConst.clusters, name);
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
