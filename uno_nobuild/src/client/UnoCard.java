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

    public String getImage(){
        
        if(isSpeciale()==true)
        return "uno_nobuild//src//client//carte//"+getNumero()+getColore()+"_1.png";
        else
        return "uno_nobuild//src//client//carte//"+getNumero()+getColore()+"_0.png";
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