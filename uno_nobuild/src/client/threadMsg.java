/**
 * Questa classe gestisce la ricezione dei messaggi dal client.
 */
public class threadMsg extends Thread {
    private TCPClient tcpClient;
    private Condivisa cond;

    /**
     * Costruttore della classe threadMsg.
     * 
     * @param tcpClient Il client TCP per la comunicazione.
     * @param cond      Oggetto Condivisa contenente i dati condivisi tra i thread.
     */
    public threadMsg(TCPClient tcpClient, Condivisa cond) {
        this.tcpClient = tcpClient;
        this.cond = cond;
    }

    /**
     * Il metodo run del thread per la gestione dei messaggi in ingresso dal server.
     */
    @Override
    public void run() {
        // Avvio di un thread separato per la gestione dei messaggi dal server
        new Thread(() -> {
            try {

                while (true) {
                    // Ricezione del messaggio dal server
                    String receivedMessage = tcpClient.receiveMessage();
                    String[] messageParts = receivedMessage.split(";");

                    // Gestisce i vari tipi di messaggi in base alla loro struttura

                    // Gestione dei messaggi di singola parola (GO, ok)
                    if (messageParts.length == 1) {
                        if (receivedMessage != null && receivedMessage.equals("GO")) {
                            // Modifica lo stato di 'on' nel gioco in base al messaggio ricevuto
                            cond.Game.on = true;
                        }
                        if (receivedMessage != null && receivedMessage.strip().equals("ok")) {
                            cond.Game.gestisciRispostaLascia();
                            System.out.println("ok ricevuto");
                        }
                    }

                    // Gestione dei messaggi con più parti
                    if (messageParts.length > 1) {
                        // Messaggio per impostare la carta centrale
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("CentralCard")) {
                            cond.tempCard = GameManaging.fromString(receivedMessage.strip().split(";")[1]);
                            cond.Game.numeroAvv = Integer.parseInt(receivedMessage.strip().split(";")[2]);
                            System.out.println("carta centrale ricevuta");
                        }
                        // Gestione di messaggi speciali (Skip, Reverse, Draw Two, Draw Four, Change
                        // Color)
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("speciale")) {
                            System.out.println("messaggio speciale ricevuto");
                            switch (receivedMessage.strip().split(";")[2]) {
                                case "salta_turno":
                                    cond.Game.GUI.chatArea.append(
                                            "giocatore" + receivedMessage.strip().split(";")[1] + " salta un turno\n");
                                    break;
                                case "pesca_due":
                                    cond.Game.GUI.chatArea.append(
                                            "giocatore" + receivedMessage.strip().split(";")[1] + " pesca due carte\n");
                                    break;
                                case "pesca_quattro":
                                    cond.Game.GUI.chatArea.append(
                                            "giocatore" + receivedMessage.strip().split(";")[1] + " sceglie colore\n");
                                    if (cond.Game.nickNameString.equals(receivedMessage.strip().split(";")[1])) {
                                        cond.Game.GUI.handleCambioColore();
                                    }
                                    break;
                                case "cambia_colore":
                                    cond.Game.GUI.chatArea.append("giocatore" + receivedMessage.strip().split(";")[1]
                                            + " sceglie il colore\n");
                                    if (cond.Game.nickNameString.equals(receivedMessage.strip().split(";")[1])) {
                                        cond.Game.GUI.handleCambioColore();
                                    }
                            }
                        }

                        String nickClient = receivedMessage.strip().split(";")[1];

                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("pesca")
                                && cond.Game.nickNameString.equals(nickClient)) {
                            cond.Game.gestisciRispostaPesca(receivedMessage.strip().split(";")[2]);
                            System.out.println("carta pescata");
                        }
                      
                        // Gestione del messaggio di risposta alla vittoria

                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("vittoria")
                                && cond.Game.nickNameString.equals(nickClient)) {
                            cond.Game.gestisciRispostaVittoria(receivedMessage.strip().split(";")[1]);
                            System.out.println("vittoria ricevuta");
                        }


                        // risposta con carta centrale da cambiare

                        // Gestione del cambio della carta centrale
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("CentralChangeCard")
                                && cond.Game.nickNameString.equals(nickClient)) {
                            cond.Game.gestisciRispostaCambia(receivedMessage.strip().split(";")[2]);
                            System.out.println("cambia centrale");
                        }
                        // Gestione errori
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("errore")
                                && cond.Game.nickNameString.equals(nickClient)) {
                            if (receivedMessage.strip().split(";")[2].equals("carta_non_valida")) {
                                System.out.println("carta non valida");
                            }
                            if (receivedMessage.strip().split(";")[2].equals("uno_non_detto")) {
                                System.out.println("uno non detto");
                                String[] cartePescate = receivedMessage.strip().split(";")[2].split("-");
                                for (int i = 0; i < cartePescate.length; i++) {
                                    cond.Game.gestisciRispostaPesca(cartePescate[i]);
                                }

                            }

                        }
                        // gestione carte ricevute dopo un +4/+2
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("carte_pescate")
                                && cond.Game.nickNameString.equals(nickClient)) {
                            // pescato carte dovuto a un piu 4/2
                            String[] cartePescate = receivedMessage.strip().split(";")[2].split("-");
                            for (int i = 0; i < cartePescate.length; i++) {
                                cond.Game.gestisciRispostaPesca(cartePescate[i]);
                            }
                            System.out.println("carte ricevute");
                        }
                        // gestione carte avversari
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("CarteAvversari")) {
                            String nickTmp = receivedMessage.strip().split(";")[1];
                            if (!nickTmp.split("_")[1].equals(cond.Game.nickNameString)) {
                                cond.Game.gestisciGUIAvv(receivedMessage.strip().split(";")[2]);
                                System.out
                                        .println("carte avversari ricevute: " + receivedMessage.strip().split(";")[2]);
                            }
                        }
                        // gestione turno ricevuto
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("Turno")) {
                            cond.Game.aggiornaTurnoGUI(receivedMessage.strip().split(";")[1]);
                            System.out.println("Turno ricevuto");

                        }

                    }

                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
