import java.util.ArrayList;

/**
 * La classe GameManaging gestisce la logica di gioco lato client per il gioco
 * Uno.
 */
public class GameManaging {
    TCPClient client;
    ArrayList<UnoCard> myCards = new ArrayList<UnoCard>();
    ArrayList<Integer> avvCards = new ArrayList<Integer>();
    int numeroAvv;
    boolean on = true;
    boolean unoClicked = false;
    String nickNameString;
    ClientGUI GUI;
    Condivisa cond;
    threadMsg threadMsg = null;
    UnoCard clickedCard_tmp = null;

    /**
     * Restituisce le carte del giocatore.
     * 
     * @return ArrayList di UnoCard rappresentanti le carte del giocatore.
     */
    public ArrayList<UnoCard> getMyCards() {
        return myCards;
    }

    /**
     * Costruttore della classe GameManaging.
     * 
     * @param GUI  Oggetto ClientGUI associato.
     * @param cond Oggetto Condivisa associato.
     */
    public GameManaging(ClientGUI GUI, Condivisa cond) {
        this.GUI = GUI;
        this.cond = cond;
    }

    /**
     * Avvia il gioco, inizializzando la connessione al server e l'ingresso del
     * giocatore in una partita.
     */
    public void startGame() {
        client = new TCPClient("localhost", 666);
        JoinParty();
    }

    /**
     * Gestisce la procedura di ingresso del giocatore in una partita.
     */
    public void JoinParty() {
        // Mostra una finestra di dialogo per inserire il nickname del giocatore
        NickNameWindow nicknameW = new NickNameWindow();
        nickNameString = nicknameW.showInputDialog();

        // Crea un messaggio contenente il nickname del giocatore per unirsi alla
        // partita
        String message = nickNameString + ";start";

        // Invia il messaggio al server
        client.sendMessage(message);

        // Riceve una risposta dal server
        String line = client.receiveMessage();
        String receivedMessage = line;

        System.out.println("Received message: " + line);

        // Analizza la risposta ricevuta dal server
        if (receivedMessage != null) {
            String[] messageParts = receivedMessage.split(";");
            if (messageParts.length > 1 && messageParts[1].equals("start")) {
                // Se la risposta contiene "start", invia il messaggio di gioco al server
                System.out.println("Entering Startgame");
                client.sendMessage("game;" + nickNameString);

                // Riceve le carte del giocatore dal server
                String myCardString = client.receiveMessage().split(";")[1];
                if (myCardString != null) {
                    Startgame(myCardString);
                } else {
                    // Gestisce il caso in cui non viene ricevuto alcun messaggio dopo "game;"
                    System.err.println("Errore: Nessun messaggio ricevuto dopo game;");
                }
            } else {
                // Gestisce il caso in cui la risposta ricevuta non è "start"
                System.err.println("Il messaggio ricevuto non è 'start'");
            }
        } else {
            // Gestisce il caso in cui receivedMessage è null
            System.err.println("receivedMessage è null");
        }

        // Avvia un thread per gestire la ricezione dei messaggi dal server
        threadMsg = new threadMsg(client, cond);
        threadMsg.run();

        // Mostra una finestra di attesa per ricevere informazioni sulla carta centrale
        WaitingWindow wait = new WaitingWindow(cond);
        wait.setVisible(true);

        // Invia un messaggio al server per richiedere informazioni sulla carta centrale
        client.sendMessage("CentralCard;first");

        // Attende finché non viene assegnata la carta centrale
        waitCentral();

        /*
         * String mess = client.receiveMessage();
         * cond.tempCard = fromString(mess.strip().split(";")[0]);
         * numeroAvv = Integer.parseInt(mess.strip().split(";")[1]);//3V;1
         */

    }

    /**
     * Attende finché la carta centrale non è stata assegnata nella classe
     * Condivisa.
     */
    private void waitCentral() {
        while (cond.tempCard == null) {
            System.out.println("attendo carta centrale");
        }
    }

    /**
     * Inizializza le carte del giocatore in base alla stringa delle carte ricevute
     * dal server.
     * 
     * @param myCardString Stringa rappresentante le carte del giocatore ricevute
     *                     dal server.
     */
    private void Startgame(String myCardString) {

        String[] carte = myCardString.strip().split("-");

        for (int i = 0; i < 7; i++)
            myCards.add(fromString(carte[i]));

    }

    /**
     * Imposta lo stato del gioco.
     * 
     * @param b Stato booleano da impostare per il gioco.
     */
    public void setOn(Boolean b) {
        this.on = b;
    }

    /**
     * Metodo statico per creare un'istanza di UnoCard da una stringa.
     * 
     * @param cardString Stringa che rappresenta una carta nel formato specifico del
     *                   gioco Uno.
     * @return Un'istanza di UnoCard creata dalla stringa fornita.
     */
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

    /**
     * Imposta la carta centrale nella classe Condivisa.
     * 
     * @param card La carta UnoCard da impostare come carta centrale.
     */
    public void setCenterCard(UnoCard card) {

        cond.tempCard = card;
    }

    /**
     * Restituisce lo stato corrente del gioco.
     * 
     * @return true se il gioco è attivo, altrimenti false.
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Gestisce l'azione di pescare una carta inviando un messaggio al server.
     */
    public void handlePesca() {
        client.sendMessage(nickNameString + ";" + "pesca");
    }

    /**
     * Gestisce l'azione di giocare una carta inviando un messaggio al server.
     * 
     * @param clickedCard La carta UnoCard selezionata dal giocatore da giocare.
     */
    public void handleLascia(UnoCard clickedCard) {
        this.clickedCard_tmp = clickedCard;
        if (!clickedCard.getColore().equals("K")) {
            if (this.unoClicked) {
                client.sendMessage(nickNameString + ";" + "lascia;" + clickedCard.toString() + ";uno");
                unoClicked = false;
            } else {
                client.sendMessage(nickNameString + ";" + "lascia;" + clickedCard.toString());
            }
            System.out.println("inviata");
        }
    }

    /**
     * Gestisce la risposta del server dopo l'azione di giocare una carta.
     * Aggiorna lo stato del gioco e dell'interfaccia grafica del giocatore.
     */
    public void gestisciRispostaLascia() {
        // Aggiornamento dello stato del gioco e dell'interfaccia grafica del giocatore
        // Rimozione della carta giocata dalla lista delle carte del giocatore e
        // aggiornamento dell'interfaccia grafica
        myCards.remove(clickedCard_tmp);
        // Aggiornamento dell'interfaccia grafica delle carte del giocatore
        GUI.updatePlayerCards();

        // Reimpostazione della carta temporanea giocata a null per prepararsi alla
        // prossima giocata
        this.clickedCard_tmp = null;
    }

    /**
     * Gestisce la risposta del server dopo l'azione di pesca di una carta.
     * Aggiorna lo stato del gioco e dell'interfaccia grafica del giocatore.
     * 
     * @param drawCardString Stringa rappresentante la carta pescata ricevuta dal
     *                       server.
     */
    public void gestisciRispostaPesca(String drawCardString) {
        UnoCard drawCard = fromString(drawCardString);
        myCards.add(drawCard);

        GUI.updatePlayerCards();

    }

    /**
     * Gestisce la risposta del server dopo l'azione di cambiare il colore della
     * carta centrale.
     * Aggiorna la carta centrale e l'interfaccia grafica del giocatore.
     * 
     * @param string Stringa rappresentante la nuova carta centrale ricevuta dal
     *               server.
     */
    public void gestisciRispostaCambia(String string) {
        setCenterCard(fromString(string));
        GUI.updateCentralCard();
        // GUI.updatePlayerCards();
        System.out.println("printed");
    }

    /**
     * Gestisce la visualizzazione delle carte dell'avversario.
     * Aggiorna lo stato delle carte dell'avversario e l'interfaccia grafica.
     * 
     * @param numCarte Numero di carte dell'avversario ricevuto dal server.
     */
    public void gestisciGUIAvv(String numCarte) {
        avvCards.clear();
        for (int i = 0; i < numeroAvv; i++) {
            avvCards.add(Integer.parseInt(numCarte));
        }
        GUI.updateOpponentCards();
    }

    /**
     * Gestisce l'azione di passare il turno inviando un messaggio al server.
     */
    public void handlePasso() {
        client.sendMessage(nickNameString + ";" + "passo");
    }

    /**
     * Aggiorna l'interfaccia grafica del giocatore per indicare il turno del
     * giocatore corrente.
     * 
     * @param nomeGiocatore Nome del giocatore corrente.
     */
    public void aggiornaTurnoGUI(String nomeGiocatore) {
        GUI.aggiornaTurno(nomeGiocatore);

    }

}
