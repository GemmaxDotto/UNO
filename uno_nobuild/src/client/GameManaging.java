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
    //static UnoCard tempCard;
    boolean on=true;
    String nickNameString;
    Condivisa cond;


    public ArrayList<UnoCard> getMyCards() {
        return myCards;
    }

    public GameManaging(Condivisa cond)  {
        this.cond=cond;
        client = new TCPClient("localhost", 666);
        Join();
    }

    public void Join() {

        NickNameWindow nicknameW = new NickNameWindow();
        nickNameString = nicknameW.showInputDialog();

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
                client.sendMessage("game;"+nickNameString);
        
                String myCardString = client.receiveMessage().split(";")[1];
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

        
        client.sendMessage("CentralCard;first");
        String mess = client.receiveMessage();
        cond.tempCard = fromString(mess);

        /*
        //thread per ricezioni msg
        threadMsg t = new threadMsg(cond);
        t.start();


        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */

        



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

        String[] carte=myCardString.strip().split("-");

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
            return new UnoCard(Integer.toString(numero), colore, false);
        } else {
            // Carta speciale
            String colore=""; //cardString.substring(0, 1);
            String num="";
            switch (cardString.split("_")[0]) {
                case "Skip":
                    colore=cardString.split("_")[1];
                    num="S";
                    break;
                case "Reverse":
                    colore=cardString.split("_")[1];
                    num="R";
                    break;
                case "Draw Two":
                    colore=cardString.split("_")[1];
                    num="T";
                    break;
                case "Draw Four":
                    colore="D";
                    num="F";
                    break;
                case "Change Color":
                    colore="C";
                    num="C";
                    break;

                default:
                    break;
            }
            return new UnoCard(num, colore, true);
        }
    }

    public void setCenterCard(UnoCard card) {  
        
        cond.tempCard=card;
       // return carteButtate.get(carteButtate.size());
        
    }

    public UnoCard getCenterCard() {  
        
        
      return carteButtate.get(carteButtate.size());
        
    }

    public static void receivedCard(){
      //  client.sendMessage(tempCard.toString());
    }

    public void onCardClicked(UnoCard card) {
        System.out.println("CIAOOOO");
    }
}
