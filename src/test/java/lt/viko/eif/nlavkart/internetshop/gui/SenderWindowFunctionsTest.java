package lt.viko.eif.nlavkart.internetshop.gui;

import lt.viko.eif.nlavkart.internetshop.models.Account;
import lt.viko.eif.nlavkart.internetshop.services.activemq.Receiver;
import lt.viko.eif.nlavkart.internetshop.services.activemq.Sender;
import lt.viko.eif.nlavkart.internetshop.util.JavaXml;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SenderWindowFunctionsTest {

    @Test
    void getFileFromFileDialog() {
        /// Assign
        String fileType = ".xml";

        /// Act
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(fileType);
            }

            @Override
            public String getDescription() {
                return fileType;
            }
        };
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);

        /// Assert
        if (result == JFileChooser.APPROVE_OPTION) {
            Assert.notNull(fileChooser.getSelectedFile().getAbsolutePath(), "File path is null");
            Assert.isTrue(fileChooser.getSelectedFile().getAbsolutePath().endsWith(fileType), "File path does not end with .xml");
        } else {
            Assert.isNull(fileChooser.getSelectedFile().getAbsolutePath(), "File path is not null");
        }
    }

    @Test
    void xmlButtonFunction() {
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
        SenderWindowFunctions senderWindowFunctions = new SenderWindowFunctions(sender);

        File xmlFile = new File("src\\test\\java\\Account.xml");
        String contentTest;
        try {
             contentTest = Files.readString(Path.of(xmlFile.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /// Act
        try {
            //String filePath = senderWindowFunctions.getFileFromFileDialog("xml");
            String content = contentTest;
            sender.sendMessage(content);
        } catch (JMSException ex) {
            throw new RuntimeException(ex);
        }

        /// Assert
        try {
            Message message = receiver.receiveMessage();
            if (message == null) {
                fail("Message is null");
            }
            TextMessage textMessage = (TextMessage) message;
            Assert.isTrue(textMessage.getText().equals(contentTest), "Message is not equal to the content of the file");
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void saveToXML() {
        /// Assign
        Account account = new Account();
        account.setId(1);
        account.setUsername("Test");
        account.setPassword("Test");
        // the path chosen in the file dialog is saved in this variable
        String filePath = "src\\test\\java";
        File file = new File(filePath + "\\" + account.getUsername()
                + account.getId() + ".xml");
        if (file.exists()){
            file.delete();
        }

        /// Act
        JavaXml javaXml = new JavaXml(Account.class);
        javaXml.createMarshaller();
        //JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //int result = fileChooser.showOpenDialog(null);
        //if (result == JFileChooser.APPROVE_OPTION) {
            javaXml.save(account, filePath + "\\" + account.getUsername()
                    + account.getId() + ".xml");
        //}

        /// Assert
        Assert.isTrue(file.exists(), "File does not exist");
        javaXml.createUnmarshaller();
        Account account1 = (Account) javaXml.load(filePath + "\\" + account.getUsername()
                + account.getId() + ".xml");
        file.delete();
        Assert.isTrue(account1.getId() == account.getId(), "Id is not equal");
        Assert.isTrue(account1.getUsername().equals(account.getUsername()), "Username is not equal");
        Assert.isTrue(account1.getPassword().equals(account.getPassword()), "Password is not equal");
    }
}