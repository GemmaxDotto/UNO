import javax.swing.*;
import java.awt.*;

public class UnoDeckGUI extends JFrame {
    private UnoDeck unoDeck;
    private CardPanel cardPanel;

    public UnoDeckGUI(UnoDeck unoDeck) {
        this.unoDeck = unoDeck;
        // Impostazioni della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("UNO Deck");

        // Pannello per disegnare le carte
        cardPanel = new CardPanel(unoDeck);
        add(cardPanel);

        pack();
        setVisible(true);
    }

    // Metodo per aggiornare la rappresentazione grafica delle carte
    public void updateCardRepresentation() {
        cardPanel.updateCards();
    }
}
