import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

class CardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Posizioni iniziali per le carte
            int x = 10;
            int y = 10;

            for (UnoCard card : .getCards()) {
                // Disegna il rettangolo della carta
                g.setColor(getColorFromString(card.getColore()));
                g.fillRect(x, y, 80, 120);

                // Disegna il numero sulla carta
                g.setColor(Color.BLACK);
                Font font = new Font("Arial", Font.BOLD, 16);
                g.setFont(font);
                g.drawString(Integer.toString(card.getNumero()), x + 35, y + 60);

                // Sposta la posizione per la prossima carta
                x += 90;

                // Vai a una nuova riga dopo un certo numero di carte (puoi regolare questo valore)
                if (x > getWidth() - 90) {
                    x = 10;
                    y += 130;
                }
            }
        }

        private Color getColorFromString(String color) {
            switch (color.toLowerCase()) {
                case "red":
                    return Color.RED;
                case "green":
                    return Color.GREEN;
                // Aggiungi altri casi per gli altri colori
                default:
                    return Color.BLACK;
            }
        }
    }