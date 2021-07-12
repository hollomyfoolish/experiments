package cn.hollomyfoolish.mq.client;

import cn.hollomyfoolish.mq.Components;
import cn.hollomyfoolish.mq.MQFactory;
import cn.hollomyfoolish.utils.ServerUtils;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class LoadbalanceConsumer {
    private static final Logger logger = LoggerFactory.getLogger(LoadbalanceConsumer.class);

    public void start(){
        String tag = "LB from " + ServerUtils.getHostName();
        try(
            Connection connection = MQFactory.newConnection(tag);
            Channel channel = connection.createChannel()
        ){
            channel.basicQos(16);
            channel.basicConsume(Components.B1AH.name(), false, tag, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    logger.info("hanlde message: " + new String(body, StandardCharsets.UTF_8));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoadbalanceConsumer().start();
    }

}
