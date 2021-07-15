package cn.hollomyfoolish.mq;

import cn.hollomyfoolish.utils.ServerUtils;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Producer implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(Producer.class);
    private final String name;

    public Producer(String name) {
        this.name = name == null?"default producer" : name;
    }

    @Override
    public void run(){
        String nameTag = ServerUtils.getHostName();
        try(
                Connection connection = MQFactory.newConnection(this.name + " from " + nameTag);
                Channel channel = connection.createChannel()
        ){
            String broadcastEx = MQConst.BROAD_EX_NAME;
            channel.exchangeDeclare(broadcastEx, BuiltinExchangeType.FANOUT);
            declareQueues(channel, broadcastEx);
            int messIdx = 1;
            Scanner scanner = new Scanner(System.in);
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

                String command = scanner.nextLine();
                if("q".equals(command)){
                    break;
                }
//                try {
//                    TimeUnit.SECONDS.sleep(5);
////                    TimeUnit.MILLISECONDS.sleep(100);
//                } catch (InterruptedException e) {
//                    logger.warn("thread interrupted", e);
//                }
            }
        } catch (IOException | TimeoutException e) {
            logger.error("connection with MQ down", e);
        }
    }

    public static void main(String[] args) {
        new Producer("producer 1").run();

//        new Thread(new Producer("producer 1"), "producer 1").start();
//        new Thread(new Producer("producer 2"), "producer 2").start();
//        new Thread(new Producer("producer 3"), "producer 3").start();
//
//        synchronized (Producer.class){
//            try {
//                Producer.class.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private static void declareQueues(Channel channel, String ex) throws IOException {
        for(Components component : Components.values()){
            AMQP.Queue.DeclareOk q = channel.queueDeclare(component.name(), true, false, false, null);
            channel.queueBind(q.getQueue(), ex, component.name());
        }
    }

}
