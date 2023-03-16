package lt.viko.eif.nlavkart.internetshop.gui;

import lt.viko.eif.nlavkart.internetshop.models.Account;
import lt.viko.eif.nlavkart.internetshop.services.activemq.Receiver;

import javax.jms.JMSException;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Receiver window.
 */
public class ReceiverWindow {
    /**
     * Main panel of the window.
     */
    private JPanel panel1;
    /**
     * Text area to display received XML file or other messages.
     */
    private JTextArea textArea1;
    /**
     * Button to receive XML file.
     */
    private JButton receiveButton;
    private JButton buttonDatabaseSave;
    private JLabel ObjectLabel;

    /**
     * Object of ReceiverWindowFunctions class.
     */
    private final ReceiverWindowFunctions receiverWindowFunctions;

    /**
     * Constructor of ReceiverWindow class.
     */
    public ReceiverWindow() {
        JFrame frame = new JFrame("Task 1");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);


        Receiver receiver = new Receiver("XMLQueue");

        try {
            receiver.createConnection();
            receiver.createConsumer();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        receiverWindowFunctions = new ReceiverWindowFunctions(receiver);

        receiveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String output = receiverWindowFunctions.receiveButtonFunction();
                if (output != null) {
                    textArea1.setText(output);
                    Account account = (Account) receiverWindowFunctions.unmarshalledObject;
                    if (account != null) ObjectLabel.setText(account.getUsername());
                }
            }
        });

        buttonDatabaseSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (receiverWindowFunctions.unmarshalledObject != null) {
                    textArea1.setText(receiverWindowFunctions.databaseSaveButtonFunction());
                } else {
                    textArea1.setText("Nothing to save");
                }
            }
        });
    }
}
