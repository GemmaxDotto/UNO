/**
 * La classe Condivisa è responsabile della condivisione di dati
 * importanti tra le varie parti del programma.
 */
public class Condivisa {
    /** Oggetto GameManaging che gestisce il gioco. */
    public GameManaging Game;

    /** Carta temporanea utilizzata nel gioco. */
    public UnoCard tempCard;

    /**
     * Costruisce un'istanza di Condivisa.
     * 
     * @param GUI Oggetto ClientGUI per l'interfaccia grafica.
     */
    public Condivisa(ClientGUI GUI) {
        this.Game = new GameManaging(GUI, this);
        System.out.println("Game");
    }

    /**
     * Avvia la gestione del gioco in GameManaging.
     */
    public void startGameManaging() {
        this.Game.startGame();
    }
}
