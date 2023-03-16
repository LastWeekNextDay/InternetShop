package lt.viko.eif.nlavkart.internetshop.gui;

import lt.viko.eif.nlavkart.internetshop.models.Account;
import lt.viko.eif.nlavkart.internetshop.util.JavaXml;
import lt.viko.eif.nlavkart.internetshop.services.activemq.Receiver;
import lt.viko.eif.nlavkart.internetshop.services.activemq.Sender;
import lt.viko.eif.nlavkart.internetshop.util.Hibernate;
import lt.viko.eif.nlavkart.internetshop.util.SchemaUtil;
import lt.viko.eif.nlavkart.internetshop.util.container.BooleanStringPair;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

class ReceiverWindowFunctionsTest {

    @Test
    void validateXMLAgainstEverything() {
        /// Assign
        String filePath = "src\\test\\java\\Account.xml";

        /// Act
        BooleanStringPair result1 = SchemaUtil.validateAgainstDTD(filePath,
                new File("src/main/java/lt/viko/eif/nlavkart/internetshop/dtd/Account.dtd").getAbsolutePath());
        BooleanStringPair result2 = SchemaUtil.validateAgainstXSD(filePath,
                new File("src/main/java/lt/viko/eif/nlavkart/internetshop/xsd/Account.xsd").getAbsolutePath());

        /// Assert
        Assert.isTrue(result1.bool(), "Validation against DTD failed.");
        Assert.isTrue(result2.bool(), "Validation against XSD failed.");
    }

    @Test
    void receiveButtonFunction() {
        /// Assign
        Receiver receiver = new Receiver("TestQueue");
        Sender sender = new Sender("TestQueue");
        try {
            sender.createConnection();
            sender.createProducer();
            receiver.createConnection();
            receiver.createConsumer();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        ReceiverWindowFunctions receiverWindowFunctions = new ReceiverWindowFunctions(receiver);
        File xmlFile = new File("src\\test\\java\\Account.xml");
        try {
            String xmlString = Files.readString(Path.of(xmlFile.getAbsolutePath()));
            sender.sendMessage(xmlString);
        } catch (JMSException | IOException e) {
            throw new RuntimeException(e);
        }

        /// Act
        Object object;
        try {
            TextMessage message = receiver.receiveMessage();
            if (message == null) {
                fail("Message is null.");
            }
            File file = new File("src\\test\\java\\lt\\viko\\eif\\nlavkart\\internetshop\\util\\temp.xml");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            Files.writeString(Path.of(file.getAbsolutePath()), message.getText());
            BooleanStringPair result = receiverWindowFunctions.validateXMLAgainstEverything(file.getAbsolutePath());
            if (!result.bool()) {
                file.delete();
                fail("Validation failed.");
            }
            Class<?> c = Class.forName("lt.viko.eif.nlavkart.internetshop.models." + result.string());
            object = new JavaXml(c).load(file.getAbsolutePath());
            file.delete();
        } catch (JMSException | IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        /// Assert
        Assert.isTrue(object.getClass() == lt.viko.eif.nlavkart.internetshop.models.Account.class,
                "Unmarshalling failed.");
    }

    @Test
    public void databaseSaveButtonFunction() {
        /// Assign
        Account account = new Account();
        account.setUsername("Test1");
        account.setPassword("Test1");
        Object unmarshalledObject = account;

        /// Act
        Hibernate hibernate = new Hibernate();
        hibernate.createSessionFactory();
        hibernate.openTransaction();
        hibernate.save((Account) unmarshalledObject);

        /// Assert
        List result = hibernate.query("FROM Account E WHERE E.username = \"Test1\"", true);
        hibernate.query("DELETE FROM Account WHERE username = \"Test1\"", false);
        hibernate.closeTransaction();
        Assert.isTrue(result.size() == 1, "Database save failed.");
    }
}