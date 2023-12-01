import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ClientGUI extends JFrame {

    private ArrayList<CardComponent> playerCards;
    JPanel mainPanel;
    JPanel centerCardPanel;
    JPanel cardsPanel;
    JPanel playerCardsPanel;
    // JPanel opponentCardsPanel;
    Condivisa cond;
    JPanel topOpponentCardsPanel;
    JPanel rightOpponentCardsPanel;
    JPanel leftOpponentCardsPanel;
    CardComponent opponentCard;
    JTextArea chatArea;

    public ClientGUI() throws IOException {
        super("Uno Game Client");
        this.cond = new Condivisa(this);
        this.cond.startGameManaging();

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

        chatArea = new JTextArea();
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
                cond.Game.unoClicked = true;
                cond.Game.client.sendMessage("uno");
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
        cardsPanel = new JPanel(new BorderLayout());

        mainPanel.setBackground(new Color(255, 255, 153)); // Imposta lo sfondo giallo

        centerCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        UnoCard centerCard = cond.tempCard;
        CardComponent centerCardComponent = new CardComponent(centerCard, cond);
        centerCardPanel.add(centerCardComponent);

        // Aggiunta carta Pesca accanto alla carta centrale
        UnoCard newCard = new UnoCard("B", "K", true);
        CardComponent newCardComponent = new CardComponent(newCard, cond);
        centerCardPanel.add(newCardComponent);

        JButton pescaButton = new JButton("Pesca");
        centerCardPanel.add(pescaButton);
        pescaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cond.Game.handlePesca();
            }
        });

        JButton passoButton = new JButton("Passo");
        centerCardPanel.add(passoButton);
        passoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cond.Game.handlePasso();
            }
        });

        setupOpponentPanels();

        // Creazione del pannello per le carte del giocatore
        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Aggiungi le carte del giocatore principale nel pannello delle carte del
        // giocatore
        for (UnoCard unoCard : cond.Game.myCards) {
            CardComponent playerCard = new CardComponent(unoCard, cond);
            playerCard.setPreferredSize(new Dimension(50, 80));
            playerCards.add(playerCard); // Aggiungi le carte del giocatore principale
            playerCardsPanel.add(playerCard);

        }

        // Creazione del pannello per le carte del centro (playerCards - carta centrale
        // - opponentCards)

        cardsPanel.add(centerCardPanel, BorderLayout.CENTER);
        cardsPanel.add(playerCardsPanel, BorderLayout.SOUTH);

        // Aggiungi i pannelli al mainPanel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
    }

    private void setupOpponentPanels() {
        // Creazione del pannello per le carte degli avversari sopra il centro
        topOpponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Creazione del pannello per le carte degli avversari a destra del centro
        rightOpponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Creazione del pannello per le carte degli avversari a sinistra del centro
        leftOpponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Crea nuove istanze di CardComponent per ogni pannello
        opponentCard = new CardComponent(new UnoCard("B", "K", true), cond);

        opponentCard.setPreferredSize(new Dimension(50, 80));

        for (int i = 0; i < 7; i++) {
            // Crea nuove istanze di CardComponent per ogni pannello
            CardComponent opponentCardTop = new CardComponent(new UnoCard("B", "K", true), cond);
            CardComponent opponentCardRight = new CardComponent(new UnoCard("B", "K", true), cond);
            CardComponent opponentCardLeft = new CardComponent(new UnoCard("B", "K", true), cond);
            opponentCardTop.setPreferredSize(new Dimension(50, 80));
            opponentCardRight.setPreferredSize(new Dimension(50, 80));
            opponentCardLeft.setPreferredSize(new Dimension(50, 80));

            if (cond.Game.numeroAvv == 1) {
                topOpponentCardsPanel.add(opponentCardTop);
                cardsPanel.add(topOpponentCardsPanel, BorderLayout.NORTH);
            } else if (cond.Game.numeroAvv == 2) {
                topOpponentCardsPanel.add(opponentCardTop);
                rightOpponentCardsPanel.add(opponentCardRight);
                cardsPanel.add(opponentCardRight, BorderLayout.EAST);
                cardsPanel.add(topOpponentCardsPanel, BorderLayout.NORTH);
            } else if (cond.Game.numeroAvv == 3) {
                topOpponentCardsPanel.add(opponentCardTop);
                rightOpponentCardsPanel.add(opponentCardRight);
                leftOpponentCardsPanel.add(opponentCardLeft);
                cardsPanel.add(opponentCardRight, BorderLayout.EAST);
                cardsPanel.add(opponentCardLeft, BorderLayout.WEST);
                cardsPanel.add(topOpponentCardsPanel, BorderLayout.NORTH);
            }

        }

    }

    public void updatePlayerCards() {

        playerCardsPanel.removeAll(); // Rimuozione tutti i componenti attuali
        for (UnoCard card : cond.Game.myCards) {
            CardComponent playerCard = new CardComponent(card, cond);
            playerCardsPanel.add(playerCard);
        }
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    public void updateOpponentCards() {
        // Rimuovi tutti i componenti dai pannelli prima di aggiungerne di nuovi
        topOpponentCardsPanel.removeAll();
        rightOpponentCardsPanel.removeAll();
        leftOpponentCardsPanel.removeAll();
    
        if (cond.Game.numeroAvv == 1) {
            for (int i = 0; i < cond.Game.avvCards.get(0); i++) {
                CardComponent opponentCardTop = new CardComponent(new UnoCard("B", "K", true), cond);
                opponentCardTop.setPreferredSize(new Dimension(50, 80));
                topOpponentCardsPanel.add(opponentCardTop);
            }
            cardsPanel.add(topOpponentCardsPanel, BorderLayout.NORTH);
            topOpponentCardsPanel.revalidate();
            topOpponentCardsPanel.repaint();
        }
    
        if (cond.Game.numeroAvv == 2) {
            for (int i = 0; i < cond.Game.avvCards.get(1); i++) {
                CardComponent opponentCardRight = new CardComponent(new UnoCard("B", "K", true), cond);
                opponentCardRight.setPreferredSize(new Dimension(50, 80));
                rightOpponentCardsPanel.add(opponentCardRight);
            }
            cardsPanel.add(rightOpponentCardsPanel, BorderLayout.EAST);
            rightOpponentCardsPanel.revalidate();
            rightOpponentCardsPanel.repaint();
        }
    
        if (cond.Game.numeroAvv == 3) {
            for (int i = 0; i < cond.Game.avvCards.get(2); i++) {
                CardComponent opponentCardLeft = new CardComponent(new UnoCard("B", "K", true), cond);
                opponentCardLeft.setPreferredSize(new Dimension(50, 80));
                leftOpponentCardsPanel.add(opponentCardLeft);
            }
            cardsPanel.add(leftOpponentCardsPanel, BorderLayout.WEST);
            leftOpponentCardsPanel.revalidate();
            leftOpponentCardsPanel.repaint();
        }
    }
    

    public void updateCentralCard() {
        centerCardPanel.removeAll(); // Rimuozione tutti i componenti attuali

        UnoCard centerCard = cond.tempCard; // carta al centro
        CardComponent centerCardComponent = new CardComponent(centerCard, cond);

        centerCardPanel.add(centerCardComponent);

        // Aggiunta carta Pesca accanto alla carta centrale
        UnoCard newCard = new UnoCard("B", "K", true);
        CardComponent newCardComponent = new CardComponent(newCard, cond);
        centerCardPanel.add(newCardComponent);

        JButton pescaButton = new JButton("Pesca");
        centerCardPanel.add(pescaButton);
        pescaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cond.Game.handlePesca();
                System.out.println("pesca");

            }
        });

         JButton passoButton = new JButton("Passo");
        centerCardPanel.add(passoButton);
        passoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cond.Game.handlePasso();
            }
        });

        centerCardPanel.revalidate();
        centerCardPanel.repaint();
    }

    public void aggiornaTurno(String nomeGiocatore) {
        chatArea.append("Turno di " + nomeGiocatore + "\n");
    }

    public void handleCambioColore() {

        ChooseColorDialog dialog = new ChooseColorDialog(this);
        dialog.setVisible(true);
        String selectedColor = dialog.getSelectedColor();
        if (selectedColor != null) {
            // Invia il colore al server o esegui altre azioni necessarie
            // Puoi anche aggiornare l'interfaccia grafica in base alla scelta del colore
            JOptionPane.showMessageDialog(this, "Hai scelto il colore: " + selectedColor);
            cond.Game.client.sendMessage("colore;" + selectedColor.charAt(0));
        } else {
            JOptionPane.showMessageDialog(this, "Nessun colore selezionato");
        }
    }

}
