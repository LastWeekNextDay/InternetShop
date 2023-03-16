package lt.viko.eif.nlavkart.internetshop.services.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Sender class for ActiveMQ
 */
public class Sender {
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
     * Producer for the session.
     */
    private MessageProducer producer;

    /**
     * No-Arg constructor.
     */
    public Sender() {
    }

    /**
     * Constructor with queue name.
     *
     * @param queueName name of the queue
     */
    public Sender(String queueName) {
        this.queueName = queueName;
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
     * Closes connection to the queue.
     *
     * @throws JMSException if connection fails
     */

    public void closeConnection() throws JMSException {
        connection.close();
    }

    /**
     * Creates producer for the queue.
     *
     * @throws JMSException if connection fails
     */
    public void createProducer() throws JMSException {
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue(queueName);
        producer = session.createProducer(destination);
    }

    /**
     * Sends message to the queue.
     *
     * @param message message to be sent
     * @throws JMSException if connection fails
     */
    public void sendMessage(String message) throws JMSException {
        TextMessage sessionTextMessage = session.createTextMessage(message);
        this.producer.send(sessionTextMessage);
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
     * @param queueName queue name
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
     * @param connection connection
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
     * @param session session
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
     * @param destination destination
     */
    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    /**
     * Gets producer.
     *
     * @return producer
     */

    public MessageProducer getProducer() {
        return producer;
    }

    /**
     * Sets producer.
     *
     * @param producer producer
     */
    public void setProducer(MessageProducer producer) {
        this.producer = producer;
    }
}
