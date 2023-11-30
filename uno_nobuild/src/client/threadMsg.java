
/*
 *  thread per la ricezione dei messaggi client
 * 
 */

public class threadMsg extends Thread {

    private TCPClient tcpClient;
    private Condivisa cond;

    public threadMsg(TCPClient tcpClient, Condivisa cond) {
        this.tcpClient = tcpClient;
        this.cond = cond;
    }

    @Override
    public void run() {
        // Avvio di un thread separato per la gestione dei messaggi dal server
        new Thread(() -> {
            try {

                while (true) {
                    String receivedMessage = tcpClient.receiveMessage();
                    String[] messageParts = receivedMessage.split(";");

                    if (messageParts.length == 1) {
                        if (receivedMessage != null && receivedMessage.equals("GO")) {
                            cond.Game.on = true;
                        }
                        if (receivedMessage != null && receivedMessage.strip().equals("ok")) {
                            cond.Game.gestisciRispostaLascia();
                            System.out.println("ok ricevuto");
                        }
                    }

                    if (messageParts.length > 1) {
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("CentralCard")) {
                            cond.tempCard = GameManaging.fromString(receivedMessage.strip().split(";")[1]);
                            cond.Game.numeroAvv = Integer.parseInt(receivedMessage.strip().split(";")[2]);
                            System.out.println("carta centrale ricevuta");
                        }
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("speciale")) {
                            System.out.println("messaggio speciale ricevuto");
                            switch (receivedMessage.strip().split(";")[2]) {
                                case "salta_turno":
                                    System.out.println(
                                            "giocatore" + receivedMessage.strip().split(";")[1] + " salta un turno");
                                    break;
                                case "pesca_due":
                                    System.out.println(
                                            "giocatore" + receivedMessage.strip().split(";")[1] + " pesca due carte");

                                    break;
                                case "pesca_quattro":
                                    System.out.println("giocatore" + receivedMessage.strip().split(";")[1]
                                            + " pesca quattro carte");
                                    break;

                            }
                        }

                        String nickClient = receivedMessage.strip().split(";")[1];

                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("pesca")
                                && cond.Game.nickNameString.equals(nickClient)) {
                            cond.Game.gestisciRispostaPesca(receivedMessage.strip().split(";")[2]);
                            System.out.println("carta pescata");
                        }
                        /*
                         * 
                         * if(receivedMessage != null
                         * &&receivedMessage.strip().split(";")[0].equals("vittoria")){
                         * cond.Game.gestisciRispostaVittoria(receivedMessage.strip().split(";")[1]);
                         * System.out.println("vittoria ricevuta");
                         * }
                         */
                        // risposta con carta centrale da cambiare
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("CentralChangeCard")
                                && cond.Game.nickNameString.equals(nickClient)) {
                            cond.Game.gestisciRispostaCambia(receivedMessage.strip().split(";")[2]);
                            System.out.println("cambia centrale");
                        }
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
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("carte_pescate")
                                && cond.Game.nickNameString.equals(nickClient)) {
                            // pescato carte dovuto a un piu 4/2
                            String[] cartePescate = receivedMessage.strip().split(";")[2].split("-");
                            for (int i = 0; i < cartePescate.length; i++) {
                                cond.Game.gestisciRispostaPesca(cartePescate[i]);
                            }
                            System.out.println("carte ricevute");
                        }
                        if (receivedMessage != null && receivedMessage.strip().split(";")[0].equals("CarteAvversari")) {
                            String nickTmp=receivedMessage.strip().split(";")[1];
                            if(!nickTmp.split("_")[1].equals(cond.Game.nickNameString)){
                            cond.Game.gestisciGUIAvv(receivedMessage.strip().split(";")[2]);
                            System.out.println("carte avversari ricevute");
                            }
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
