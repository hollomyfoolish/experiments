package cn.hollomyfoolish.mq;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String CONNECTION_NAME = "Connection of Producer";
    private static Logger logger = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try(
                Connection connection = factory.newConnection(CONNECTION_NAME);
                Channel channel = connection.createChannel()
        ){
            String broadcastEx = "b1 broadcast";
            channel.exchangeDeclare(broadcastEx, BuiltinExchangeType.FANOUT);
            declareQueues(channel, broadcastEx);
            int messIdx = 1;
            while (true){
                try {
                    if(messIdx%2 == 0){
                        // broadcast
                        channel.basicPublish(broadcastEx, "broadcast", MessageProperties.PERSISTENT_TEXT_PLAIN, new B1Message(messIdx, "from broadcast").toBytes());
                    }else{
                        // to B1AH queue only
                        channel.basicPublish("", Components.B1AH.name(), MessageProperties.PERSISTENT_TEXT_PLAIN, new B1Message(messIdx, "from broadcast").toBytes());
                    }
                    logger.info("message published");
                } catch (IOException | ShutdownSignalException e){
                    logger.error("channel exception, waiting for recover...", e);
                }
                messIdx++;
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    logger.warn("thread interrupted", e);
                }
            }
        } catch (IOException | TimeoutException e) {
            logger.error("connection with MQ down", e);
        }
    }

    private static void declareQueues(Channel channel, String ex) throws IOException {
        for(Components component : Components.values()){
            AMQP.Queue.DeclareOk q = channel.queueDeclare(component.name(), true, false, false, null);
            channel.queueBind(q.getQueue(), ex, component.name());
        }
    }

}
