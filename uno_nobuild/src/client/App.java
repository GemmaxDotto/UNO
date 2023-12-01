import javax.swing.SwingUtilities;

/**
 * La classe App rappresenta il punto di ingresso dell'applicazione.
 */
public class App {
    /**
     * Metodo principale (main) che avvia l'applicazione GUI.
     * 
     * @param args Argomenti passati da riga di comando (non utilizzati in questo
     *             caso).
     */
    public static void main(String[] args) {
        // Invoca il metodo invokeLater di SwingUtilities per eseguire
        // l'inizializzazione dell'interfaccia grafica
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crea un'istanza di GUIClientSTART e la rende visibile
                new GUIClientSTART().setVisible(true);
            }
        });
    }
}
