package lt.viko.eif.nlavkart.internetshop.services.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Receiver class for ActiveMQ
 */
public class Receiver {
    /**
     * Name of the queue.
     */
    private String queueName;
    /**
     * Connection to the queue.
     */
    private Connection connection;
    /**
     * Session for the connection.
     */
    private Session session;
    /**
     * Destination for the session.
     */
    private Destination destination;
    /**
     * Consumer for the session.
     */
    private MessageConsumer consumer;

    /**
     * No-Arg constructor.
     */
    public Receiver() {
    }

    /**
     * Constructor with queue name.
     *
     * @param queueName name of the queue
     */
    public Receiver(String queueName) {
        this.queueName = queueName;
    }

    /**
     * Gets queue name.
     *
     * @return queue name
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * Sets queue name.
     *
     * @param queueName name of the queue
     */
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    /**
     * Gets connection.
     *
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Sets connection.
     *
     * @param connection connection to the queue
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets session.
     *
     * @return session
     */
    public Session getSession() {
        return session;
    }

    /**
     * Sets session.
     *
     * @param session session for the connection
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * Gets destination.
     *
     * @return destination
     */
    public Destination getDestination() {
        return destination;
    }

    /**
     * Sets destination.
     *
     * @param destination destination for the session
     */
    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    /**
     * Gets consumer.
     *
     * @return consumer
     */
    public MessageConsumer getConsumer() {
        return consumer;
    }

    /**
     * Sets consumer.
     *
     * @param consumer consumer for the session
     */
    public void setConsumer(MessageConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Creates connection to the queue.
     *
     * @throws JMSException if connection fails
     */
    public void createConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        connection = connectionFactory.createConnection();
        connection.start();
    }

    /**
     * Closes connection.
     *
     * @throws JMSException if connection fails
     */
    public void closeConnection() throws JMSException {
        connection.close();
    }

    /**
     * Creates consumer.
     *
     * @throws JMSException if connection fails
     */
    public void createConsumer() throws JMSException {
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue(queueName);
        consumer = session.createConsumer(destination);
    }

    /**
     * Receives message from the queue.
     *
     * @return TextMessage
     * @throws JMSException if connection fails
     */
    public TextMessage receiveMessage() throws JMSException {
        Message message = consumer.receive(1);
        if (message instanceof TextMessage) {
            return (TextMessage) message;
        }
        return null;
    }
}
