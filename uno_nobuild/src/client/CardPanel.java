import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CardPanel extends JPanel {
    private UnoDeck unoDeck;

    public CardPanel(UnoDeck unoDeck) {
        this.unoDeck = unoDeck;

        // Imposta il layout e aggiungi il listener del mouse
        setFocusable(true);  // Assicurati che il pannello sia cliccabile
        setLayout(new FlowLayout());
       // addMouseListener(new CardClickListener());
        updateCards(); // Aggiorna le carte quando viene creato il pannello
    }

    // Metodo per aggiornare la rappresentazione grafica delle carte
    // Nel metodo updateCards di CardPanel
    public void updateCards() {
        removeAll(); // Rimuovi tutte le carte attuali

        // Aggiungi le nuove carte
        for (UnoCard card : unoDeck.getCards()) {
            ImageIcon icon = new ImageIcon(card.getImagePath());
            JLabel label = new JLabel(icon);
            add(label);
        }

        revalidate(); // Aggiorna il layout
        repaint(); // Ridisegna il pannello
    }

    // Metodo per aggiungere nuove carte nella parte superiore
    public void addCardsToTop(ArrayList<CardComponent> newCards) {
        JPanel newCardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        for (CardComponent cardComponent : newCards) {
            newCardsPanel.add(cardComponent);
        }

        add(newCardsPanel, 0); // Aggiungi il nuovo pannello nella parte superiore
        revalidate(); // Aggiorna il layout
        repaint(); // Ridisegna il pannello
    }

    // Nel tuo pannello delle carte (CardPanel)
    public void addCardsToBottom(ArrayList<CardComponent> newCards) {
        JPanel newCardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        for (CardComponent cardComponent : newCards) {
            newCardsPanel.add(cardComponent);
        }

        add(newCardsPanel); // Aggiungi il nuovo pannello alla fine
        revalidate(); // Aggiorna il layout
        repaint(); // Ridisegna il pannello
    }

    /*
     // Listener per il clic delle carte
    private class CardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Gestisci il clic della carta
            Component clickedComponent = getComponentAt(e.getPoint());
            if (clickedComponent instanceof CardComponent) {
                UnoCard clickedCard = ((CardComponent) clickedComponent).getCard();
                GameManaging.tempCard = clickedCard;
                GameManaging.receivedCard();
                // Fai qualcosa con la carta cliccata
                System.out.println("Hai cliccato su: " + clickedCard);

            }
        }
    } */
}
