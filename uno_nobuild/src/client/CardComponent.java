import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class CardComponent extends JButton {
    private UnoCard card;
    GameManaging Game;

    public CardComponent(UnoCard card,GameManaging Game) {
        this.card = card;
       // setText(card.toString());  // Usa toString() o un'altra rappresentazione testuale della carta
        //setOpaque(true);
        //setBackground(Color.WHITE);
        setIcon(new ImageIcon(card.getImagePath()));  // Assicurati che l'immagine venga impostata correttamente
        setPreferredSize(new Dimension(50, 80));  // Imposta le dimensioni desiderate
        setFocusable(true);  // Abilita l'evento di input
        addMouseListener(new CardClickListener());
    }

    public UnoCard getCard() {
        return card;
    }

    private class CardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            // Chiamato quando la carta viene cliccata
            System.out.println("Hai cliccato su: " + card);
            // Puoi fare qualcos'altro con la carta, ad esempio notificarla al tuo GameManaging
            Game.onCardClicked(card);
        }
    }

}

