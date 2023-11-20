import javax.swing.*;
import java.awt.*;

public class CardComponent extends JLabel {
    private UnoCard card;

    public CardComponent(UnoCard card) {
        this.card = card;
        setText(card.toString());  // Usa toString() o un'altra rappresentazione testuale della carta
        setOpaque(true);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(50, 80));  // Imposta le dimensioni desiderate
    }

    public UnoCard getCard() {
        return card;
    }
}

