import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;


import javax.swing.SwingUtilities;

/**
 * GameManaging
 */
public class GameManaging {
    TCPClient client;
    ArrayList<UnoCard> myCards=new ArrayList<UnoCard>();
    ArrayList<UnoCard> carteButtate=new ArrayList<UnoCard>();

    public ArrayList<UnoCard> getMyCards() {
        return myCards;
    }

    public GameManaging()  {
        client = new TCPClient("localhost", 666);
        Join();
    }

    public void Join() {

        NickNameWindow nicknameW = new NickNameWindow();
        String nickNameString = nicknameW.showInputDialog();

        String message = nickNameString + ";start";

        client.sendMessage(message);
        // String line=receiveMessage();
        // String line = input.readLine();
        wait waiting = new wait();
        // String receivedMessage = receiveMessage().trim();
        String line = client.receiveMessage();

        // Mostra la finestra di attesa all'inizio

        // SwingUtilities.invokeLater(waiting::showWaitWindow);

        // Loop finché non ricevi un messaggio specifico dal server
        // while (true) {

        /*
         * int length=is.read(buffer,0,buffer.length);
         * String receivedMessage = new String(buffer,0,length);
         */

        String receivedMessage = line;

  
        System.out.println("Received message: " + line);

        if (receivedMessage != null) {
            String[] messageParts = receivedMessage.split(";");
            if (messageParts.length > 1 && messageParts[1].equals("start")) {
                System.out.println("Entering Startgame");
                client.sendMessage("game;1");
        
                String myCardString = client.receiveMessage();
                if (myCardString != null) {
                    Startgame(myCardString);
                } else {
                    // Gestione del caso in cui la ricezione del messaggio fallisce
                    System.err.println("Errore: Nessun messaggio ricevuto dopo game;");
                    // Potresti decidere di gestire questa situazione in modo specifico, ad esempio riprovando o gestendo l'errore in altro modo.
                }
            } else {
                // Gestione del caso in cui il messaggio ricevuto non corrisponde a "start"
                System.err.println("Il messaggio ricevuto non è 'start'");
                // Potresti decidere di gestire questa situazione in modo specifico, ad esempio ignorando il messaggio o inviando una risposta.
            }
        } else {
            // Gestione del caso in cui receivedMessage è null
            System.err.println("receivedMessage è null");
            // Potresti decidere di gestire questa situazione in modo specifico, ad esempio riprovando o gestendo l'errore in altro modo.
        }
        

        // Aggiorna la finestra di attesa (puoi anche farlo solo quando ricevi "no")
        // SwingUtilities.invokeLater(waiting::showWaitWindow);

        // Attendi per evitare un loop troppo veloce
        // Thread.sleep(500); // 0,5 secondo
        // }

        /*
         * waiting.closeWaitWindow(() -> {
         * // Altre operazioni dopo l'attesa...
         * 
         * });
         */

    }

    private void Startgame(String myCardString) {

        String[] carte=myCardString.strip().split(";");

        for (int i = 0; i < 7; i++)
            myCards.add(fromString(carte[i]));
        // setCards(myCards);

    }

    // Metodo statico per creare una UnoCard da una stringa
    public static UnoCard fromString(String cardString) {
        /* if (cardString.length() < 2 || cardString.length() > 3) {
            // La stringa deve essere di lunghezza 2 o 3 (ad es. "1V" o "DS")
            throw new IllegalArgumentException("La stringa deve essere di lunghezza 2 o 3");
        } */

        if (cardString.length() == 2) {
            // Carta normale
            int numero = Character.getNumericValue(cardString.charAt(0));
            String colore = cardString.substring(1);
            return new UnoCard(numero, colore, false);
        } else {
            // Carta speciale
            String colore = "none"; //cardString.substring(0, 1);
            return new UnoCard(0, colore, true);
        }
    }

    public UnoCard getCenterCard() {  
        
        return carteButtate.get(carteButtate.size());
    }
}
