
import org.w3c.dom.events.MouseEvent;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CardComponent extends JButton {
    private UnoCard card;
    Condivisa cond;
    ClientGUI GUI;
    


   
    public CardComponent(UnoCard card,Condivisa cond) {

        this.card = card;
        this.cond = cond;


        ImageIcon originalIcon = new ImageIcon(card.getImagePath());
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(50, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        setIcon(scaledIcon);
        setPreferredSize(new Dimension(50, 80)); // Imposta le dimensioni desiderate
        setFocusable(true); // Abilita l'evento di input
        addMouseListener(new CardClickListener());

    }

    public UnoCard getCard() {
        return card;
    }

    private class CardClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            UnoCard clickedCard = getCard();


            cond.Game.handleLascia(clickedCard);

            System.out.println("carta cliccata" + clickedCard);

        }
    }

}