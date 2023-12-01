import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * La classe UnoCard rappresenta una singola carta del gioco di Uno.
 * Estende JButton per consentire l'uso della carta come componente Swing.
 */
public class UnoCard extends JButton {

    private String numero; // Numero della carta (es. "0", "1", ..., "9")
    private String colore; // Colore della carta (es. "Rosso", "Giallo", "Blu", "Verde")
    private boolean isSpeciale; // Indica se la carta è speciale (ad es. "+2", "Cambio direzione", "Stop")

    /**
     * Imposta il numero della carta.
     * 
     * @param numero Il numero della carta.
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * Imposta il colore della carta.
     * 
     * @param colore Il colore della carta.
     */
    public void setColore(String colore) {
        this.colore = colore;
    }

    /**
     * Imposta se la carta è speciale o no.
     * 
     * @param isSpeciale True se la carta è speciale, altrimenti False.
     */
    public void setSpeciale(boolean isSpeciale) {
        this.isSpeciale = isSpeciale;
    }

    /**
     * Costruttore per la classe UnoCard.
     * 
     * @param numero     Il numero della carta.
     * @param colore     Il colore della carta.
     * @param isSpeciale Indica se la carta è speciale o no.
     */
    public UnoCard(String numero, String colore, boolean isSpeciale) {
        this.numero = numero;
        this.colore = colore;
        this.isSpeciale = isSpeciale;
    }

    /**
     * Restituisce il percorso dell'immagine della carta.
     * 
     * @return Il percorso dell'immagine della carta.
     */
    public String getImagePath() {
        String imagePath;
        if (isSpeciale) {
            imagePath = "uno_nobuild/src/client/carte/" + getNumero() + getColore() + "_1.png";
        } else {
            imagePath = "uno_nobuild/src/client/carte/" + getNumero() + getColore() + "_0.png";
        }
        return imagePath;
    }

    /**
     * Imposta l'icona della carta da un percorso dell'immagine.
     * 
     * @param imagePath Il percorso dell'immagine della carta.
     */
    public void setIconFromImage(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(image));
    }

    /**
     * Restituisce una rappresentazione testuale della carta.
     * 
     * @return Una stringa contenente il numero e il colore della carta.
     */
    @Override
    public String toString() {
        return numero + colore;
    }

    /**
     * Restituisce il numero della carta.
     * 
     * @return Il numero della carta.
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Restituisce il colore della carta.
     * 
     * @return Il colore della carta.
     */
    public String getColore() {
        return colore;
    }

    /**
     * Verifica se la carta è speciale o no.
     * 
     * @return True se la carta è speciale, altrimenti False.
     */
    public boolean isSpeciale() {
        return isSpeciale;
    }
}
