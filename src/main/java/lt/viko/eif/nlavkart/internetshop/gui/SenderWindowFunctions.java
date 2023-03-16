package lt.viko.eif.nlavkart.internetshop.gui;

import lt.viko.eif.nlavkart.internetshop.models.Account;
import lt.viko.eif.nlavkart.internetshop.util.JavaXml;
import lt.viko.eif.nlavkart.internetshop.services.activemq.Sender;
import lt.viko.eif.nlavkart.internetshop.util.Hibernate;

import javax.jms.JMSException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Class for functions of the sender window.
 */
public class SenderWindowFunctions {
    /**
     * Sender for ActiveMQ.
     */
    Sender sender;

    /**
     * No-Arg constructor.
     */
    public SenderWindowFunctions() {
    }

    /**
     * Constructor with sender and receiver.
     *
     * @param sender sender for ActiveMQ
     */
    public SenderWindowFunctions(Sender sender) {
        this.sender = sender;
    }

    /**
     * Sets sender.
     *
     * @param sender sender for ActiveMQ
     */
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    /**
     * Gets sender.
     *
     * @return sender for ActiveMQ
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * Gets file from file dialog.
     *
     * @param fileType type of the file
     * @return path to the file
     */
    public String getFileFromFileDialog(String fileType) {
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
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    /**
     * Function for the XML button.
     */
    public void xmlButtonFunction() {
        try {
            String filePath = getFileFromFileDialog("xml");
            String content = Files.readString(Path.of(filePath));
            sender.sendMessage(content);
        } catch (IOException | JMSException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Function for the database button.
     */

    public List<Account> getAccountsFromDatabaseButtonFunction() {
        Hibernate hibernate = new Hibernate();
        hibernate.createSessionFactory();
        hibernate.openTransaction();
        List<Account> accounts = hibernate.query("from Account", true);
        hibernate.closeTransaction();
        return accounts;
    }

    /**
     * Save account to XML.
     *
     * @param account
     */
    public static void saveToXML(Account account) {
        JavaXml javaXml = new JavaXml(Account.class);
        javaXml.createMarshaller();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            javaXml.save(account, fileChooser.getSelectedFile().getAbsolutePath() + "\\" + account.getUsername()
                    + account.getId() + ".xml");
        }
    }

}
