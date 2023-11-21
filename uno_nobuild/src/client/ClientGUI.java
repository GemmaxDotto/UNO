import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.w3c.dom.events.MouseEvent;

public class ClientGUI extends JFrame {

    private GameManaging Game;
    private UnoDeck unoDeck = new UnoDeck();
    private CardPanel cardPanel;

    public ClientGUI() throws IOException {
        super("Uno Game Client");

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }

        Game = new GameManaging();

        /// Creazione del pannello per il tavolo da gioco
        JPanel gameTablePanel = new JPanel(new BorderLayout());
        gameTablePanel.setBackground(new Color(255, 255, 153)); // Giallo chiaro

        // Creazione del CardPanel e assegnamento alla variabile di istanza cardPanel
        cardPanel = new CardPanel(unoDeck);
        cardPanel.setBackground(new Color(255, 255, 153)); // Imposta lo sfondo giallo

        // Creazione di carte per il giocatore principale (da personalizzare)
        ArrayList<CardComponent> playerCards = new ArrayList<>();

        // Aggiungi le carte del giocatore principale al tuo mazzo
        for (UnoCard unoCard : Game.myCards) {
            unoDeck.addCards(unoCard);
            playerCards.add(new CardComponent(unoCard,Game)); // Aggiungi le carte del giocatore principale
            unoCard.setIconFromImage(unoCard.getImagePath());
            System.out.println("Percorso immagine: " + unoCard.getImagePath());
        }

        // Aggiungi le carte del giocatore principale nella parte inferiore del pannello
        // delle carte
        cardPanel.addCardsToBottom(playerCards);

        // Aggiorna il pannello delle carte
        cardPanel.updateCards();



        // Aggiungi il CardPanel al pannello del tavolo da gioco nella parte superiore
        gameTablePanel.add(cardPanel, BorderLayout.SOUTH);

        // Crea il bottone "Aggiungi Carta"
        JButton addButton = new JButton("Aggiungi Carta");
        // Aggiungi un listener per gestire l'evento di clic sul pulsante
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aggiungi qui il codice da eseguire quando il pulsante viene cliccato
                //unoDeck.addCards(new UnoCard(1, "R", false));
                cardPanel.updateCards(); // Aggiorna il pannello delle carte
            }
        });
        addButton.setPreferredSize(new Dimension(10, 30)); // Imposta le dimensioni desiderate
       // CardComponent CenterCard=new CardComponent(Game.getCenterCard());
        
        // Aggiungi il pulsante al centro nella parte inferiore del pannello
        //gameTablePanel.add(CenterCard, BorderLayout.CENTER);
        // Aggiungi il pulsante al centro nella parte inferiore del pannello
        gameTablePanel.add(addButton, BorderLayout.BEFORE_FIRST_LINE);

         add(gameTablePanel, BorderLayout.CENTER);
        // Aggiungi il pannello del tavolo da gioco al tuo JFrame
       

        // Creazione del pannello per la chat
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane);

        JTextField chatInputField = new JTextField();
        chatPanel.add(chatInputField);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatArea.append("you:" + chatInputField.getText() + "\n");
                chatInputField.setText("");
            }
        });
        chatPanel.add(sendButton);

        JButton unoButton = new JButton("UNO");
        chatPanel.add(unoButton);

        // Creazione di carte per l'avversario (da personalizzare)
        ArrayList<CardComponent> opponentCards = new ArrayList<>();
        //opponentCards.add(new CardComponent(new UnoCard(0, "V", false))); // Aggiungi le carte dell'avversario


        // Aggiungi le carte dell'avversario nella parte superiore del pannello delle
        // carte
        cardPanel.addCardsToTop(opponentCards);

        // Creazione del layout principale
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(gameTablePanel)
                .addComponent(chatPanel));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(gameTablePanel)
                .addComponent(chatPanel));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 900); // Imposta le dimensioni della finestra
        setLocationRelativeTo(null); // Centra la finestra
        pack();
        setVisible(true);
    }

    // Listener per il clic delle carte


}
