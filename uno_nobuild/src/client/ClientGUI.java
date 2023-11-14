
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClientGUI extends JFrame {

    private JTextArea messageArea;
    private JTextField inputField;
    private JButton unoButton;
    TCPClient client=new TCPClient("localhost",12346);

    public ClientGUI() {
        super("Uno Client");

        // Configura l'interfaccia grafica
        setupUI();

        // Configura il client Uno
        setupUnoClient();
    }

    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        unoButton = new JButton("UNO");
        unoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendUno();
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(unoButton, BorderLayout.WEST);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);
    }

    private void setupUnoClient() {
        // Configura la logica del client Uno
        // Implementa la connessione al server, la ricezione e l'invio dei messaggi,
        // ecc.
    }

    private void sendMessage() {
        // Logica per inviare il messaggio al server
        String message = inputField.getText();
        // Invia il messaggio al server e aggiorna la UI
        messageArea.append("You: " + message + "\n");
        inputField.setText("");
    }

    private void sendUno() {
        // Logica per inviare l'azione Uno al server
        String unoMessage = "UNO!";
        // Invia l'azione Uno al server e aggiorna la UI
        messageArea.append("You shouted UNO!\n");

        client.sendMessage(unoMessage);
    }
}
