
import org.w3c.dom.events.MouseEvent;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CardComponent extends JButton {
    private UnoCard card;
    GameManaging Game;
    ClientGUI GUI;

    public CardComponent(UnoCard card, GameManaging Game,ClientGUI GUI) {
        this.card = card;
        // setText(card.toString()); // Usa toString() o un'altra rappresentazione
        // testuale della carta
        // setOpaque(true);
        // setBackground(Color.WHITE);
        this.GUI=GUI;
        ImageIcon originalIcon = new ImageIcon(card.getImagePath());

        // Ridimensiona l'immagine
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(50, 80, Image.SCALE_SMOOTH); // Imposta le dimensioni
                                                                                         // desiderate
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        setIcon(scaledIcon);
        setPreferredSize(new Dimension(50, 80)); // Imposta le dimensioni desiderate
        setFocusable(true); // Abilita l'evento di input
        addMouseListener(new CardClickListener());
        // setBorder(BorderFactory.createLineBorder(Color.RED)); // Cambia il colore a
        // tuo piacimento
        // Oppure
        // setBackground(Color.YELLOW); // Cambia il colore a tuo piacimento */
    }

    public UnoCard getCard() {
        return card;
    }

    private class CardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            // Ottieni la carta associata a questo componente
            UnoCard clickedCard = getCard();

            // Invia un messaggio al server
            // Assicurati che il tuo oggetto GameManaging abbia un metodo
            // sendMessageToServer()
            // che possa inviare il messaggio al server
            //&&clickedCard.toString().(Game.myCards)
            if (Game != null&&!clickedCard.getColore().equals("K")) {
                Game.client.sendMessage(Game.nickNameString+";"+"lascia;"+clickedCard.toString());
                Game.setCenterCard(clickedCard);
                Game.carteButtate.add(clickedCard);
                Game.myCards.remove(clickedCard);
                GUI.CardUpdateGUI();
            }
            System.out.println("carta cliccata"+clickedCard);
        }
    }

}