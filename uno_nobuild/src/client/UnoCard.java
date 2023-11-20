public class UnoCard {
    private int numero;
    private String colore;
    private boolean isSpeciale;

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