package cn.hollomyfoolish.mq.spring.config;

import cn.hollomyfoolish.mq.Components;
import cn.hollomyfoolish.mq.MQConst;
import cn.hollomyfoolish.mq.MQFactory;
import cn.hollomyfoolish.mq.spring.Receiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RabbitMQ {
    static final String queueName = Components.B1AH.name();
    @Autowired
    private FanoutExchange exchange;

//    @Override
//    public void initialize(ConfigurableApplicationContext context) {
//        ConfigurableListableBeanFactory factory = context.getBeanFactory();
//        for(Components component : Components.values()){
//            Queue q = new Queue(component.name(), true, false, false);
//            Binding b = BindingBuilder.bind(q).to(exchange);
//            factory.registerSingleton("QUEUE_" + component.name(), q);
//            factory.registerSingleton("BINDING_" + component.name(), b);
//        }
//    }

    @Bean
    Queue qB1AH(){
        return new Queue(Components.B1AH.name(), true, false, false);
    }

    @Bean
    Queue qWebClient(){
        return new Queue(Components.WebClient.name(), true, false, false);
    }

    @Bean
    Queue qJobService(){
        return new Queue(Components.JobService.name(), true, false, false);
    }

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(MQConst.BROAD_EX_NAME, true, false);
    }

    @Bean
    Binding bB1AH(FanoutExchange exchange, Queue qB1AH){
        return BindingBuilder.bind(qB1AH).to(exchange);
    }

    @Bean
    Binding bWebClient(FanoutExchange exchange, Queue qWebClient){
        return BindingBuilder.bind(qWebClient).to(exchange);
    }

    @Bean
    Binding bJobService(FanoutExchange exchange, Queue qJobService){
        return BindingBuilder.bind(qJobService).to(exchange);
    }

    @Bean
    ConnectionFactory connectionFactory(){
        return new CachingConnectionFactory(MQFactory.defaultSpringFactory());
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
