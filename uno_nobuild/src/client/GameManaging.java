
import java.util.ArrayList;

/**
 * GameManaging
 */
public class GameManaging {
    TCPClient client;
    ArrayList<UnoCard> myCards = new ArrayList<UnoCard>();
    // ArrayList<UnoCard> carteButtate = new ArrayList<UnoCard>();
    int numeroAvv;
    UnoCard tempCard;
    boolean on = true;
    String nickNameString;
    ClientGUI GUI;

    public ArrayList<UnoCard> getMyCards() {
        return myCards;
    }

    public GameManaging(ClientGUI GUI) {
        this.GUI = GUI;
        client = new TCPClient("localhost", 666);
        Join();
    }

    public void Join() {

        NickNameWindow nicknameW = new NickNameWindow();
        nickNameString = nicknameW.showInputDialog();

        String message = nickNameString + ";start";

        client.sendMessage(message);

        String line = client.receiveMessage();

        String receivedMessage = line;

        System.out.println("Received message: " + line);

        if (receivedMessage != null) {
            String[] messageParts = receivedMessage.split(";");
            if (messageParts.length > 1 && messageParts[1].equals("start")) {
                System.out.println("Entering Startgame");
                client.sendMessage("game;" + nickNameString);

                String myCardString = client.receiveMessage().split(";")[1];
                if (myCardString != null) {
                    Startgame(myCardString);
                } else {
                    // Gestione del caso in cui la ricezione del messaggio fallisce
                    System.err.println("Errore: Nessun messaggio ricevuto dopo game;");

                }
            } else {
                // Gestione del caso in cui il messaggio ricevuto non corrisponde a "start"
                System.err.println("Il messaggio ricevuto non è 'start'");

            }
        } else {
            // Gestione del caso in cui receivedMessage è null
            System.err.println("receivedMessage è null");

        }

        threadMsg threadMsg = new threadMsg(client, this);
        threadMsg.start();// thread per ricezioni msg

        WaitingWindow wait = new WaitingWindow(this);
        wait.setVisible(true);

        client.sendMessage("CentralCard;first");
        String mess = client.receiveMessage();
        tempCard = fromString(mess.strip().split(";")[0]);
        numeroAvv = Integer.parseInt(mess.strip().split(";")[1]);

    }

    private void Startgame(String myCardString) {

        String[] carte = myCardString.strip().split("-");

        for (int i = 0; i < 7; i++)
            myCards.add(fromString(carte[i]));

    }

    // Metodo statico per creare una UnoCard da una stringa
    public static UnoCard fromString(String cardString) {

        if (cardString.length() == 2) {
            // Carta normale
            int numero = Character.getNumericValue(cardString.charAt(0));
            String colore = cardString.substring(1);
            return new UnoCard(Integer.toString(numero), colore, false);
        } else {
            // Carta speciale
            String colore = "";
            String num = "";
            switch (cardString.split("_")[0]) {
                case "Skip":
                    colore = cardString.split("_")[1];
                    num = "S";
                    break;
                case "Reverse":
                    colore = cardString.split("_")[1];
                    num = "R";
                    break;
                case "Draw Two":
                    colore = cardString.split("_")[1];
                    num = "T";
                    break;
                case "Draw Four":
                    colore = "D";
                    num = "F";
                    break;
                case "Change Color":
                    colore = "C";
                    num = "C";
                    break;

                default:
                    break;
            }
            return new UnoCard(num, colore, true);
        }
    }

    public void setCenterCard(UnoCard card) {

        tempCard = card;

    }

    public static void receivedCard() {
        // client.sendMessage(tempCard.toString());
    }

    public void onCardClicked(UnoCard card) {
        System.out.println("CIAOOOO");
    }

    public boolean isOn() {
        return on;
    }

    public void handlePesca() {
        client.sendMessage(nickNameString + ";" + "pesca");
    }

    public void handleLascia(UnoCard clickedCard) {
        if (!clickedCard.getColore().equals("K") && !clickedCard.toString().equals(tempCard.toString())) {
            client.sendMessage(nickNameString + ";" + "lascia;" + clickedCard.toString());
            String receString = client.receiveMessage().strip();
            if (receString.equals("ok")) {
                // gestire ricezione mess speciali
                setCenterCard(clickedCard);
                myCards.remove(clickedCard);
                tempCard = clickedCard;

                GUI.updatePlayerCards();
                GUI.updateCentralCard();

            }
        }
    }
}
