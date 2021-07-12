package cn.hollomyfoolish.mq.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MessagingRabbitmqApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext app = SpringApplication.run(MessagingRabbitmqApplication.class, args);
        Object sleeper = new Object();
        synchronized (sleeper){
            sleeper.wait();
        }
        app.close();
    }

}
