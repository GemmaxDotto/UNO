import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class UnoCard extends JButton {
    private String numero;
    private String colore;
    private boolean isSpeciale;

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public void setSpeciale(boolean isSpeciale) {
        this.isSpeciale = isSpeciale;
    }

    public UnoCard(String numero, String colore, boolean isSpeciale) {
        this.numero = numero;
        this.colore = colore;
        this.isSpeciale = isSpeciale;
    }

    public String getImagePath() {

        String imagePath;
        if (isSpeciale) {
            imagePath = "uno_nobuild/src/client/carte/" + getNumero() + getColore() + "_1.png";
        } else {
            imagePath = "uno_nobuild/src/client/carte/" + getNumero() + getColore() + "_0.png";
        }

        return imagePath;
    }

    public void setIconFromImage(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(image));
    }

    @Override
    public String toString() {
        return numero + colore;
    }

    public String getNumero() {
        return numero;
    }

    public String getColore() {
        return colore;
    }

    public boolean isSpeciale() {
        return isSpeciale;
    }
}