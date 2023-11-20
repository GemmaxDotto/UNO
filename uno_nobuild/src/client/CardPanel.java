import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardPanel extends JPanel {
    private UnoDeck unoDeck;

    public CardPanel(UnoDeck unoDeck) {
        this.unoDeck = unoDeck;

        // Imposta il layout e aggiungi il listener del mouse
        setLayout(new FlowLayout());
        addMouseListener(new CardClickListener());
        updateCards();  // Aggiorna le carte quando viene creato il pannello
    }

    // Metodo per aggiornare la rappresentazione grafica delle carte
    public void updateCards() {
        removeAll();  // Rimuovi tutte le carte attuali

        // Aggiungi le nuove carte
        for (UnoCard card : unoDeck.getCards()) {
            add(new CardComponent(card));
        }

        revalidate();  // Aggiorna il layout
        repaint();     // Ridisegna il pannello
    }

    // Listener per il clic delle carte
    private class CardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Gestisci il clic della carta
            Component clickedComponent = getComponentAt(e.getPoint());
            if (clickedComponent instanceof CardComponent) {
                UnoCard clickedCard = ((CardComponent) clickedComponent).getCard();
                // Fai qualcosa con la carta cliccata
                System.out.println("Hai cliccato su: " + clickedCard);
            }
        }
    }
}
