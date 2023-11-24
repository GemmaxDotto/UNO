import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ClientGUI extends JFrame {

    private GameManaging Game;
    private ArrayList<CardComponent> playerCards;
    JPanel mainPanel;
    JPanel centerCardPanel;
    JPanel cardsPanel;
    JPanel playerCardsPanel;
    JPanel opponentCardsPanel;

    public ClientGUI() throws IOException {
        super("Uno Game Client");

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        Game = new GameManaging(this);

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

        playerCards = new ArrayList<>();

        mainPanel.setBackground(new Color(255, 255, 153)); // Imposta lo sfondo giallo

        // Creazione del pannello per la carta al centro
        centerCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        UnoCard centerCard = Game.tempCard; // carta al centro
        CardComponent centerCardComponent = new CardComponent(centerCard, Game, this);
        centerCardPanel.add(centerCardComponent);
        // Aggiungi la nuova carta accanto alla carta centrale
        UnoCard newCard = new UnoCard("B", "K", true);
        CardComponent newCardComponent = new CardComponent(newCard, Game, this);
        centerCardPanel.add(newCardComponent);

        JButton pescaButton = new JButton("Pesca");
        centerCardPanel.add(pescaButton);
        pescaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                 Game.handlePesca();
            }
        });

        setupOpponentPanels();

        // Creazione del pannello per le carte del giocatore
        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Aggiungi le carte del giocatore principale nel pannello delle carte del
        // giocatore
        for (UnoCard unoCard : Game.myCards) {
            CardComponent playerCard = new CardComponent(unoCard, Game, this);
            playerCard.setPreferredSize(new Dimension(50, 80));
            playerCards.add(playerCard); // Aggiungi le carte del giocatore principale
            playerCardsPanel.add(playerCard);

        }

        // Creazione del pannello per le carte del centro (playerCards - carta centrale
        // - opponentCards)
        JPanel cardsPanel = new JPanel(new BorderLayout());
        cardsPanel.add(centerCardPanel, BorderLayout.CENTER);
        cardsPanel.add(playerCardsPanel, BorderLayout.SOUTH);

        // Aggiungi i pannelli al mainPanel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
    }

    private void setupOpponentPanels() {
        // Creazione del pannello per le carte degli avversari sopra il centro
        JPanel topOpponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Creazione del pannello per le carte degli avversari a destra del centro
        JPanel rightOpponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Creazione del pannello per le carte degli avversari a sinistra del centro
        JPanel leftOpponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        for (int i = 0; i < 7; i++) {
            CardComponent opponentCard = new CardComponent(new UnoCard("B", "K", true), Game, this);
            opponentCard.setPreferredSize(new Dimension(50, 80));
            if (Game.numeroAvv == 1)
                topOpponentCardsPanel.add(opponentCard);
            if (Game.numeroAvv == 2)
                rightOpponentCardsPanel.add(opponentCard);
            if (Game.numeroAvv == 3)
                leftOpponentCardsPanel.add(opponentCard);
        }

        // Creazione del pannello per le carte degli avversari (topOpponentCardsPanel +
        // rightOpponentCardsPanel + leftOpponentCardsPanel)
        JPanel allOpponentCardsPanel = new JPanel(new BorderLayout());
        allOpponentCardsPanel.add(topOpponentCardsPanel, BorderLayout.NORTH);
        allOpponentCardsPanel.add(rightOpponentCardsPanel, BorderLayout.EAST);
        allOpponentCardsPanel.add(leftOpponentCardsPanel, BorderLayout.WEST);

        // pannello allOpponentCardsPanel aggiunto al mainPanel
        mainPanel.add(allOpponentCardsPanel, BorderLayout.NORTH);
    }

    public void updatePlayerCards() {
        playerCardsPanel.removeAll(); // Rimuozione tutti i componenti attuali
        for (UnoCard card : Game.myCards) {
            CardComponent playerCard = new CardComponent(card, Game, this);
            playerCardsPanel.add(playerCard);
        }
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    public void updateCentralCard() {
        centerCardPanel.removeAll(); // Rimuozione tutti i componenti attuali

        UnoCard centerCard = Game.tempCard; // carta al centro
        CardComponent centerCardComponent = new CardComponent(centerCard, Game, this);
        centerCardPanel.add(centerCardComponent);

        // Aggiunta carta Pesca accanto alla carta centrale
        UnoCard newCard = new UnoCard("B", "K", true);
        CardComponent newCardComponent = new CardComponent(newCard, Game, this);
        centerCardPanel.add(newCardComponent);

        JButton pescaButton = new JButton("Pesca");
        centerCardPanel.add(pescaButton);
        pescaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Game.handlePesca();

            }
        });

        centerCardPanel.revalidate();
        centerCardPanel.repaint();
    }

}
