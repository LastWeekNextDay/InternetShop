package lt.viko.eif.nlavkart.internetshop.gui;

import lt.viko.eif.nlavkart.internetshop.models.Account;
import lt.viko.eif.nlavkart.internetshop.services.activemq.Sender;

import javax.jms.JMSException;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Sender window.
 */
public class SenderWindow {
    /**
     * Main panel of the window.
     */
    private JPanel panel1;
    /**
     * Button to send XML file.
     */
    private JButton sendXMLButton;
    /**
     * Text area to display messages.
     */
    private JTextArea textArea1;
    /**
     * List to display accounts.
     */
    private JList list1;
    /**
     * Button to get accounts from database.
     */
    private JButton getAccountsFromDatabaseButton;
    /**
     * Button to print accounts to XML file.
     */
    private JButton printToXMLButton;

    /**
     * Object of SenderWindowFunctions class.
     */
    private final SenderWindowFunctions senderWindowFunctions;
    /**
     * List of accounts.
     */
    private List<Account> accounts;

    /**
     * Constructor of SenderWindow class.
     */
    public SenderWindow() {
        JFrame frame = new JFrame("Task 1");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 300);

        Sender sender = new Sender("XMLQueue");

        try {
            sender.createConnection();
            sender.createProducer();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        senderWindowFunctions = new SenderWindowFunctions(sender);

        sendXMLButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                senderWindowFunctions.xmlButtonFunction();
                textArea1.setText("XML file sent.");
            }
        });

        getAccountsFromDatabaseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                accounts = senderWindowFunctions.getAccountsFromDatabaseButtonFunction();
                if (accounts == null) {
                    textArea1.setText("No accounts in database.");
                    return;
                }
                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (Account account : accounts) {
                    String input = account.getId() + ": " + account.getUsername();
                    listModel.addElement(input);
                }
                list1.setModel(listModel);
            }
        });

        printToXMLButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int hold = list1.getSelectedIndex();
                if (hold == -1) {
                    textArea1.setText("No account selected.");
                    return;
                }
                Account i = accounts.get(hold);
                SenderWindowFunctions.saveToXML(i);
            }
        });
    }
}
