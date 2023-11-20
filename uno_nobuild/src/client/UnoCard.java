import javax.swing.JButton;

public class UnoCard extends JButton{
    private int numero;
    private String colore;
    private boolean isSpeciale;

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public void setSpeciale(boolean isSpeciale) {
        this.isSpeciale = isSpeciale;
    }

    public UnoCard(int numero, String colore, boolean isSpeciale) {
        this.numero = numero;
        this.colore = colore;
        this.isSpeciale = isSpeciale;
    }

    @Override
    public String toString() {
        return isSpeciale ? colore : numero + colore;
    }


    public int getNumero() {
        return numero;
    }

    public String getColore() {
        return colore;
    }

    public boolean isSpeciale() {
        return isSpeciale;
    }
}