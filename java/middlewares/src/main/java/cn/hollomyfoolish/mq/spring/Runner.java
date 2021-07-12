package cn.hollomyfoolish.mq.spring;

import java.util.concurrent.TimeUnit;

import cn.hollomyfoolish.mq.MQConst;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(){
            @Override
            public void run() {
                while(true){
                    System.out.println("Sending message...");
                    rabbitTemplate.convertAndSend(MQConst.BROAD_EX_NAME, "foo.bar.baz", "Hello from RabbitMQ!");
                    try {
                        receiver.getLatch().await(100, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}