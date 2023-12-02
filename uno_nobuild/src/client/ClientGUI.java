import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.text.DefaultCaret;

public class ClientGUI extends JFrame {

    // Variabili per la gestione delle carte e delle componenti dell'interfaccia
    // grafica
    private ArrayList<CardComponent> playerCards;
    JPanel mainPanel;
    JPanel centerCardPanel;
    JPanel cardsPanel;
    JPanel playerCardsPanel;
    Condivisa cond;
    JPanel topOpponentCardsPanel;
    JPanel rightOpponentCardsPanel;
    JPanel leftOpponentCardsPanel;
    CardComponent opponentCard;
    JTextArea chatArea;

    /**
     * Costruttore che inizializza e imposta l'interfaccia grafica del gioco Uno.
     * 
     * @throws IOException In caso di eccezione durante la creazione
     *                     dell'interfaccia grafica
     */
    public ClientGUI() throws IOException {
        super("Uno Game Client");
        this.cond = new Condivisa(this);
        this.cond.startGameManaging();

        // Impostazione dell'aspetto grafico
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creazione del pannello principale con BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Inizializzazione e impostazione dei pannelli per le carte e la chat
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


        // Aggiunta del pannello della chat al pannello principale
        mainPanel.add(chatPanel, BorderLayout.SOUTH);

        // Aggiunta del pannello principale al frame
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Imposta e organizza i pannelli per le carte all'interno dell'interfaccia
     * grafica.
     */
    public void setCards() {
        // Inizializzazione della lista delle carte del giocatore
        playerCards = new ArrayList<>();
        cardsPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 153)); // Imposta lo sfondo giallo

        // Creazione del pannello per la carta centrale e i bottoni 'Pesca' e 'Passo'
        centerCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Aggiunta della carta centrale al pannello
        UnoCard centerCard = cond.tempCard;
        CardComponent centerCardComponent = new CardComponent(centerCard, cond);
        centerCardPanel.add(centerCardComponent);

        // Aggiunta di una nuova carta accanto alla carta centrale
        UnoCard newCard = new UnoCard("B", "K", true);
        CardComponent newCardComponent = new CardComponent(newCard, cond);
        centerCardPanel.add(newCardComponent);

        // Aggiunta dei bottoni 'Pesca' e 'Passo' al pannello
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

        // Configurazione dei pannelli degli avversari
        setupOpponentPanels();

        // Creazione del pannello per le carte del giocatore
        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Aggiunta delle carte del giocatore al pannello
        for (UnoCard unoCard : cond.Game.myCards) {
            CardComponent playerCard = new CardComponent(unoCard, cond);
            playerCard.setPreferredSize(new Dimension(50, 80));
            playerCards.add(playerCard); // Aggiunta delle carte del giocatore
            playerCardsPanel.add(playerCard);
        }

        // Creazione del pannello per le carte del centro
        cardsPanel.add(centerCardPanel, BorderLayout.CENTER);
        cardsPanel.add(playerCardsPanel, BorderLayout.SOUTH);

        // Aggiunta dei pannelli al mainPanel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
    }

    /**
     * Configura i pannelli per visualizzare le carte degli avversari.
     */
    private void setupOpponentPanels() {
        // Creazione dei pannelli per le carte degli avversari sopra, a destra e a
        // sinistra del centro
        topOpponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightOpponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        leftOpponentCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Creazione di un'istanza di CardComponent per la carta avversario
        opponentCard = new CardComponent(new UnoCard("B", "K", true), cond);
        opponentCard.setPreferredSize(new Dimension(50, 80));

        // Aggiunta delle carte avversarie ai rispettivi pannelli
        for (int i = 0; i < 7; i++) {
            CardComponent opponentCardTop = new CardComponent(new UnoCard("B", "K", true), cond);
            CardComponent opponentCardRight = new CardComponent(new UnoCard("B", "K", true), cond);
            CardComponent opponentCardLeft = new CardComponent(new UnoCard("B", "K", true), cond);

            opponentCardTop.setPreferredSize(new Dimension(50, 80));
            opponentCardRight.setPreferredSize(new Dimension(50, 80));
            opponentCardLeft.setPreferredSize(new Dimension(50, 80));

            // Aggiunta delle carte avversarie nei rispettivi pannelli basato sul numero di
            // avversari
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

    /**
     * Aggiorna le carte del giocatore nell'interfaccia grafica.
     */
    public void updatePlayerCards() {
        playerCardsPanel.removeAll(); // Rimuove tutti i componenti attuali dalle carte del giocatore

        // Aggiunge le carte del giocatore principale nel pannello delle carte del
        // giocatore
        for (UnoCard card : cond.Game.myCards) {
            CardComponent playerCard = new CardComponent(card, cond);
            playerCard.setPreferredSize(new Dimension(50, 80));
            playerCardsPanel.add(playerCard);
        }

        playerCardsPanel.revalidate(); // Rende valido il layout del pannello
        playerCardsPanel.repaint(); // Aggiorna l'interfaccia grafica del pannello
    }

    /**
     * Aggiorna le carte degli avversari nell'interfaccia grafica.
     */
    public void updateOpponentCards() {
        // Rimuove tutti i componenti dai pannelli delle carte avversarie
        topOpponentCardsPanel.removeAll();
        rightOpponentCardsPanel.removeAll();
        leftOpponentCardsPanel.removeAll();

        // Aggiunge le carte avversarie nei rispettivi pannelli basato sul numero di
        // avversari
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

    /**
     * Aggiorna la carta centrale nell'interfaccia grafica, incluso il pulsante
     * "Pesca" e il pulsante "Passo".
     */
    public void updateCentralCard() {
        centerCardPanel.removeAll(); // Rimuove tutti i componenti attualmente presenti nel pannello centrale

        UnoCard centerCard = cond.tempCard; // Ottiene la carta centrale dallo stato del gioco
        CardComponent centerCardComponent = new CardComponent(centerCard, cond);

        centerCardPanel.add(centerCardComponent); // Aggiunge la nuova carta centrale al pannello

        // Aggiunta della carta "Pesca" accanto alla carta centrale
        UnoCard newCard = new UnoCard("B", "K", true);
        CardComponent newCardComponent = new CardComponent(newCard, cond);
        centerCardPanel.add(newCardComponent);

        JButton pescaButton = new JButton("Pesca");
        centerCardPanel.add(pescaButton);
        pescaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cond.Game.handlePesca(); // Gestisce l'azione di pesca nel gioco
                System.out.println("pesca");
            }
        });

        JButton passoButton = new JButton("Passo");
        centerCardPanel.add(passoButton);
        passoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cond.Game.handlePasso(); // Gestisce l'azione di passo nel gioco
            }
        });

        centerCardPanel.revalidate(); // Rende valido il layout del pannello centrale
        centerCardPanel.repaint(); // Aggiorna l'interfaccia grafica del pannello centrale
    }

    /**
     * Aggiorna l'interfaccia grafica per indicare il turno attuale di un giocatore.
     * 
     * @param nomeGiocatore Il nome del giocatore che ha il turno.
     */
    public void aggiornaTurno(String nomeGiocatore) {
        chatArea.append("Turno di " + nomeGiocatore + "\n"); // Aggiunge un messaggio al campo di chat per indicare il
                                                             // turno del giocatore
    }

    /**
     * Gestisce l'interfaccia grafica quando è richiesta la selezione del colore da
     * parte del giocatore.
     * Mostra una finestra di dialogo per consentire al giocatore di selezionare un
     * colore e invia la selezione al server.
     * Visualizza un messaggio di conferma se un colore è stato selezionato con
     * successo.
     */
    public void handleCambioColore() {
        ChooseColorDialog dialog = new ChooseColorDialog(this); // Crea una finestra di dialogo per la scelta del colore
        dialog.setVisible(true); // Mostra la finestra di dialogo

        String selectedColor = dialog.getSelectedColor(); // Ottiene il colore selezionato dall'utente dalla finestra di
                                                          // dialogo

        if (selectedColor != null) {
            // Invia il colore al server o esegue altre azioni necessarie
            // Può aggiornare l'interfaccia grafica in base alla scelta del colore
            JOptionPane.showMessageDialog(this, "Hai scelto il colore: " + selectedColor);

            // Invia il colore selezionato al server attraverso il client
            cond.Game.client.sendMessage("colore;" + selectedColor.charAt(0));
        } else {
            // Se l'utente non ha selezionato alcun colore, mostra un messaggio di avviso
            JOptionPane.showMessageDialog(this, "Nessun colore selezionato");
        }
    }

    public void vittoria() {
        JOptionPane.showMessageDialog(this, "Hai vinto!");
    }

    public void sconfitta() {
        JOptionPane.showMessageDialog(this, "Hai perso!");
    }

}
