import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ClientGUI extends JFrame {

    // private wait waiting;
    GameManaging Game;
    private UnoDeck unoDeck=new UnoDeck();
    private CardPanel cardPanel;

    public ClientGUI() throws IOException {
        super("Uno Game Client");

        

        Game=new GameManaging();

        //while(Game.myCards==null){}

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }

         // Aggiungi alcune carte di UNO per esempio
        for (UnoCard unoCard : Game.myCards) {
            unoDeck.addCards(unoCard);
        }

        cardPanel = new CardPanel(unoDeck);

        // Aggiungi il CardPanel al tuo frame
        add(cardPanel, BorderLayout.CENTER);

        // Aggiungi un pulsante per simulare l'aggiunta di una carta (da modificare secondo le tue esigenze)
        JButton addButton = new JButton("Aggiungi Carta");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Simula l'aggiunta di una carta al mazzo
                //unoDeck.addCards(new UnoCard(UnoCard.Color.RED, UnoCard.Value.ONE));
                unoDeck.addCards(new UnoCard(1, "R", false));

                // Aggiorna il pannello delle carte
                cardPanel.updateCards();
            }
        });
        add(addButton, BorderLayout.SOUTH); 
        
        

        // Creazione del pannello per il tavolo da gioco
        JPanel gameTablePanel = new JPanel();
        gameTablePanel.setBackground(new Color(255, 255, 153)); // Giallo chiaro

        // Creazione del pannello per la chat
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        // Creazione della text area per scrivere nella chat
        JTextField chatInputField = new JTextField();

        // Creazione del pulsante "Send" per inviare messaggi nella chat
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aggiungi il testo della text field alla chat
                chatArea.append("you:" + chatInputField.getText() + "\n");
                // Pulisci la text field dopo l'invio
                chatInputField.setText("");
            }
        });

        // Creazione del pannello per il pulsante "UNO"
        JButton unoButton = new JButton("UNO");

        // Creazione del layout principale
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        // Configurazione del layout
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Aggiungi il pannello delle carte al layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(gameTablePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addComponent(chatScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(chatInputField)
                                .addComponent(sendButton))
                        .addComponent(unoButton)
      
        ));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(gameTablePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(chatScrollPane, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(chatInputField)
                        .addComponent(sendButton))
                .addComponent(unoButton));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 900); // Imposta le dimensioni della finestra
        setLocationRelativeTo(null); // Centra la finestra


        pack();
        setVisible(true);

        

    }

  


}
