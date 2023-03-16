package lt.viko.eif.nlavkart.internetshop.gui;

import lt.viko.eif.nlavkart.internetshop.util.JavaXml;
import lt.viko.eif.nlavkart.internetshop.services.activemq.Receiver;
import lt.viko.eif.nlavkart.internetshop.util.Hibernate;
import lt.viko.eif.nlavkart.internetshop.util.SchemaUtil;
import lt.viko.eif.nlavkart.internetshop.util.container.BooleanStringPair;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class for functions of the receiver window.
 */
public class ReceiverWindowFunctions {
    /**
     * Receiver for ActiveMQ.
     */
    Receiver receiver;

    Object unmarshalledObject;

    /**
     * No-Arg constructor.
     */
    public ReceiverWindowFunctions() {
    }

    /**
     * Constructor with sender and receiver.
     *
     * @param receiver receiver for ActiveMQ
     */
    public ReceiverWindowFunctions(Receiver receiver) {
        this.receiver = receiver;
    }

    /**
     * Sets receiver.
     *
     * @param receiver receiver for ActiveMQ
     */
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    /**
     * Gets receiver.
     *
     * @return receiver for ActiveMQ
     */
    public Receiver getReceiver() {
        return receiver;
    }

    /**
     * Validates XML file against DTD and XSD.
     *
     * @param filePath path to the file
     * @return pair of boolean and string
     */
    public BooleanStringPair validateXMLAgainstEverything(String filePath) {
        BooleanStringPair result = SchemaUtil.validateAgainstDTD(filePath,
                new File("src/main/java/lt/viko/eif/nlavkart/internetshop/dtd/Account.dtd").getAbsolutePath());
        if (!result.bool()) return new BooleanStringPair(false, "XML file is not valid against DTD.");

        result = SchemaUtil.validateAgainstXSD(filePath,
                new File("src/main/java/lt/viko/eif/nlavkart/internetshop/xsd/Account.xsd").getAbsolutePath());
        if (!result.bool()) return new BooleanStringPair(false, "XML file is not valid against XSD.");

        return new BooleanStringPair(true, "Account");
    }

    /**
     * Function for receiving button.
     *
     * @return string of unmarshalled object
     */
    public String receiveButtonFunction() {
        try {
            TextMessage message = receiver.receiveMessage();
            if (message == null) {
                return "No message received";
            }
            File file = new File("src\\main\\java\\lt\\viko\\eif\\nlavkart\\internetshop\\util\\temp.xml");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            Files.writeString(Path.of(file.getAbsolutePath()), message.getText());
            BooleanStringPair result = validateXMLAgainstEverything(file.getAbsolutePath());
            if (!result.bool()) {
                file.delete();
                return "XML file is not valid";
            }
            Class<?> c = Class.forName("lt.viko.eif.nlavkart.internetshop.models." + result.string());
            Object object = new JavaXml(c).load(file.getAbsolutePath());
            file.delete();
            unmarshalledObject = object;
            return object.toString();
        } catch (JMSException | IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Function for database save button.
     *
     * @return null if failed, otherwise "Saved to database"
     */
    public String databaseSaveButtonFunction() {
        if (unmarshalledObject == null) return "No object to save";
        Hibernate hibernate = new Hibernate();
        hibernate.createSessionFactory();
        hibernate.openTransaction();
        hibernate.save(unmarshalledObject);
        hibernate.closeTransaction();
        return "Saved to database";
    }
}
