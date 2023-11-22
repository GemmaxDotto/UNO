import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ClientGUI extends JFrame {

    private GameManaging Game = new GameManaging();
    private UnoDeck unoDeck = new UnoDeck();
    private CardPanel cardPanel;
    private ArrayList<CardComponent> opponentCards = new ArrayList<>();
    private ArrayList<CardComponent> playerCards = new ArrayList<>();
    JPanel mainPanel;

    public ClientGUI() throws IOException {
        super("Uno Game Client");

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

    public void setCards(){
         // Creazione del CardPanel e assegnamento alla variabile di istanza cardPanel
        cardPanel = new CardPanel(Game,this);
        mainPanel.setBackground(new Color(255, 255, 153)); // Imposta lo sfondo giallo

        // Aggiungi le carte del giocatore principale al tuo mazzo
        for (UnoCard unoCard : Game.myCards) {
            unoDeck.addCards(unoCard);
            playerCards.add(new CardComponent(unoCard, Game,this)); // Aggiungi le carte del giocatore principale
        }

        // Aggiungi le carte del giocatore principale nella parte superiore del pannello
        // delle carte
        cardPanel.addCardsToTop(playerCards);

        // Aggiorna il pannello delle carte
        cardPanel.updateCards();

        // Aggiungi il CardPanel nella parte superiore del pannello principale
        mainPanel.add(cardPanel, BorderLayout.NORTH);

        // Creazione del pannello per le carte dell'avversario
        JPanel opponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Aggiungi le carte dell'avversario al pannello delle carte dell'avversario
        for (int i = 0; i < 7; i++)
            opponentCards.add(new CardComponent(new UnoCard("B", "K", true), Game,this));

        cardPanel.addCardsToTop(opponentCards);

        // Aggiungi il pannello delle carte dell'avversario nella parte inferiore del
        // pannello principale
        mainPanel.add(opponentCardsPanel, BorderLayout.SOUTH);

        // Creazione del pannello per la carta al centro
        JPanel centerCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        UnoCard centerCard = new UnoCard("5", "R", false); // Sostituisci con la tua logica per la carta al centro
        CardComponent centerCardComponent = new CardComponent(centerCard, Game,this);
        centerCardPanel.add(centerCardComponent);

        // Aggiungi il pannello della carta al centro nella parte centrale del pannello
        // principale
        mainPanel.add(centerCardPanel, BorderLayout.CENTER);

    }

    public void CardUpdateGUI() {
        cardPanel.updateCards();
    }

}
