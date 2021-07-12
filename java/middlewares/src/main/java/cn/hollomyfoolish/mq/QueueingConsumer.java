package cn.hollomyfoolish.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueingConsumer extends DefaultConsumer{

    public static class Delivery{
        private BasicProperties properties;
        private byte[] body;
        private Envelope envelope;
        public BasicProperties getProperties() {
            return properties;
        }
        public void setProperties(BasicProperties properties) {
            this.properties = properties;
        }
        public byte[] getBody() {
            return body;
        }
        public void setBody(byte[] body) {
            this.body = body;
        }
        public Envelope getEnvelope() {
            return envelope;
        }
        public void setEnvelope(Envelope envelope) {
            this.envelope = envelope;
        }

    }
    
    private LinkedBlockingQueue<Delivery> queue;
    public QueueingConsumer(Channel channel) {
        super(channel);
        queue = new LinkedBlockingQueue<Delivery>();
    }
    public QueueingConsumer(Channel channel,int size) {
        super(channel);
        queue = new LinkedBlockingQueue<Delivery>(size);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        Delivery delivery = new Delivery();
        delivery.setBody(body);
        delivery.setProperties(properties);
        delivery.setEnvelope(envelope);
        try {
            queue.put(delivery);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Delivery nextDelivery()throws InterruptedException, ShutdownSignalException, ConsumerCancelledException{
        return queue.take();
    }
    public Delivery nextDelivery(long timeout)throws InterruptedException, ShutdownSignalException, ConsumerCancelledException{
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }
}