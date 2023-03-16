package lt.viko.eif.nlavkart.internetshop.services.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import javax.jms.*;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ReceiverTest {

    @Test
    void createConnection() {
        /// Assign
        Connection connection;
        Session session;
        Destination destination;
        MessageConsumer consumer;
        MessageProducer producer;
        Message message;

        /// Act
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        /// Assert
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("TestQueue");
            consumer = session.createConsumer(destination);
            producer = session.createProducer(destination);
            producer.send(session.createTextMessage("Test"));
            message = consumer.receive(1);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(connection);
        try {
            assertEquals("Test", ((TextMessage) message).getText());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void closeConnection() {
        /// Assign
        Connection connection;
        Session session;
        Destination destination;
        MessageConsumer consumer;
        MessageProducer producer;
        Message message = null;

        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        /// Act
        try {
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        /// Assert
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("TestQueue");
            consumer = session.createConsumer(destination);
            producer = session.createProducer(destination);
            producer.send(session.createTextMessage("Test"));
            message = consumer.receive(1);
        } catch (JMSException ignored) {
        }

        if (message instanceof TextMessage) {
            fail("Message should not have sent.");
        } else {
            assertNull(message);
        }
    }

    @Test
    void createConsumer() {
        /// Assign
        Connection connection;
        Session session;
        Destination destination;
        MessageConsumer consumer;
        MessageProducer producer;
        Message message;
        String queueName = "TestQueue";

        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        /// Act
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            consumer = session.createConsumer(destination);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        /// Assert
        assertNotNull(consumer);
        try {
            producer = session.createProducer(destination);
            producer.send(session.createTextMessage("Test"));
            message = consumer.receive(1);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        try {
            assertEquals("Test", ((TextMessage) message).getText());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void receiveMessage() {
        /// Assign
        Connection connection;
        Session session;
        Destination destination;
        MessageConsumer consumer;
        MessageProducer producer;

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("TestQueue");
            consumer = session.createConsumer(destination);
            producer = session.createProducer(destination);
            producer.send(session.createTextMessage("Test"));
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        /// Act
        Message message = null;
        try {
            message = consumer.receive(1);
            //if (message instanceof TextMessage) {
            //    return (TextMessage) message;
            //}
            //return null;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }


        /// Assert
        Assert.notNull(message);
        try {
            assertEquals("Test", ((TextMessage) message).getText());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}