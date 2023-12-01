
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * La classe CardComponent estende JButton e rappresenta un componente grafico
 * che visualizza una singola carta del gioco Uno.
 */
public class CardComponent extends JButton {
    private UnoCard card;
    private Condivisa cond;

    /**
     * Costruisce un oggetto CardComponent che rappresenta una carta del gioco Uno.
     * 
     * @param card La carta Uno da visualizzare nel componente.
     * @param cond Oggetto Condivisa che gestisce la logica di gioco.
     */
    public CardComponent(UnoCard card, Condivisa cond) {
        this.card = card;
        this.cond = cond;

        // Imposta l'immagine della carta ridimensionata come icona del pulsante
        ImageIcon originalIcon = new ImageIcon(card.getImagePath());
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(50, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        setIcon(scaledIcon);

        setPreferredSize(new Dimension(50, 80)); // Imposta le dimensioni desiderate
        setFocusable(true); // Abilita l'evento di input
        addMouseListener(new CardClickListener()); // Aggiunge un listener per il click del mouse
    }

    /**
     * Restituisce la carta associata a questo componente.
     * 
     * @return La carta Uno associata al componente.
     */
    public UnoCard getCard() {
        return card;
    }

    /**
     * Inner class che funge da listener per il click del mouse sul componente.
     */
    private class CardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            UnoCard clickedCard = getCard();

            // Gestisce l'evento del click sulla carta
            cond.Game.handleLascia(clickedCard); // Invia la carta cliccata al gestore di gioco

            System.out.println("carta cliccata: " + clickedCard);
        }
    }
}
