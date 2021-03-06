package com.winson.framework.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author winson
 * @date 2021/6/28
 **/
public class ActiveMQQueueProducerNeedAckDemo {

    public static void main(String[] args) throws JMSException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                MQConfig.CONNECT_WITH_NIO);


        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        session.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("queue client receive message : " + message);
            }
        });
        Destination destination = session.createQueue(MQConfig.QUEUE_WINSON_HOME_DEV_1);
        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage("producer normal , mode is : AUTO_ACKNOWLEDGE");
        producer.send(message);

        latch.await();
        producer.close();
        session.close();
        connection.close();
    }

}
