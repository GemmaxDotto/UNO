import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ClientGUI extends JFrame {

    //private GameManaging Game = new GameManaging();
    private ArrayList<CardComponent> opponentCards;
    private ArrayList<CardComponent> playerCards;
    JPanel mainPanel;
    JPanel centerCardPanel;
    JPanel cardsPanel;
    JPanel playerCardsPanel;
    JPanel opponentCardsPanel;
    Condivisa cond = new Condivisa();


    public ClientGUI() throws IOException {
        super("Uno Game Client");
        cond.createGameManaging(cond);



        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Creazione del pannello principale con BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        setCards();

        // Creazione del pannello per la chat
        JPanel chatPanel = new JPanel(new BorderLayout());

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setPreferredSize(new Dimension(300, 200));

        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        JTextField chatInputField = new JTextField();
        chatPanel.add(chatInputField, BorderLayout.SOUTH);

        // Creazione di un pannello separato per i bottoni della chat
        JPanel chatButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatInputField.getText();
                if (!message.isEmpty()) {
                    chatArea.append("you: " + message + "\n");
                    chatInputField.setText("");
                }
            }
        });
        chatButtonPanel.add(sendButton);

        JButton unoButton = new JButton("UNO");
        unoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logica per gestire l'evento UNO
                // Invia un messaggio al server o esegui altre azioni necessarie
            }
        });
        chatButtonPanel.add(unoButton);

        // Aggiungi il pannello dei bottoni della chat nella parte inferiore del
        // pannello della chat
        chatPanel.add(chatButtonPanel, BorderLayout.EAST);

        // Aggiungi il pannello della chat nella parte inferiore del pannello principale
        mainPanel.add(chatPanel, BorderLayout.SOUTH);

        // Aggiungi il pannello principale al frame
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setCards() {
        
        

        playerCards=new ArrayList<>();
        opponentCards=new ArrayList<>();
        // Creazione del CardPanel e assegnamento alla variabile di istanza cardPanel
        
        mainPanel.setBackground(new Color(255, 255, 153)); // Imposta lo sfondo giallo
    
        // Creazione del pannello per la carta al centro
        centerCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        UnoCard centerCard = cond.tempCard; // carta al centro
        CardComponent centerCardComponent = new CardComponent(centerCard, cond, this);
        centerCardPanel.add(centerCardComponent);
    
        // Creazione del pannello per le carte dell'avversario
        opponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
        // Aggiungi le carte dell'avversario al pannello delle carte dell'avversario
        for (int i = 0; i < 7; i++) {
            CardComponent opponentCard = new CardComponent(new UnoCard("B", "K", true), cond, this);
            opponentCard.setPreferredSize(new Dimension(50, 80)); // Imposta le dimensioni desiderate per le carte
            opponentCards.add(opponentCard);
            opponentCardsPanel.add(opponentCard);
        }
    
        // Creazione del pannello per le carte del giocatore
        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
        // Aggiungi le carte del giocatore principale nel pannello delle carte del giocatore
        for (UnoCard unoCard : cond.Game.myCards) {
            CardComponent playerCard = new CardComponent(unoCard, cond, this);
            playerCard.setPreferredSize(new Dimension(50, 80)); // Imposta le dimensioni desiderate per le carte
            playerCards.add(playerCard); // Aggiungi le carte del giocatore principale
            playerCardsPanel.add(playerCard);
            
        }
    
        // Creazione del pannello per le carte del centro (playerCards - carta centrale - opponentCards)
        JPanel cardsPanel = new JPanel(new BorderLayout());
        cardsPanel.add(opponentCardsPanel, BorderLayout.NORTH);
        cardsPanel.add(centerCardPanel, BorderLayout.CENTER);
        cardsPanel.add(playerCardsPanel, BorderLayout.SOUTH);
        
    
        // Aggiungi i pannelli al mainPanel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
    }

    public void updatePlayerCards() {
        playerCardsPanel.removeAll();  // Rimuovi tutti i componenti attuali
        for (UnoCard card : cond.Game.myCards) {
            CardComponent playerCard = new CardComponent(card, cond, this);
            playerCardsPanel.add(playerCard);
        }
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    public void updateCentralCard() {

        centerCardPanel.removeAll();  // Rimuovi tutti i componenti attuali
         UnoCard centerCard = cond.tempCard; // carta al centro
        CardComponent centerCardComponent = new CardComponent(centerCard, cond, this);
        centerCardPanel.add(centerCardComponent);
        centerCardPanel.revalidate();
        centerCardPanel.repaint();
    }
    


}
