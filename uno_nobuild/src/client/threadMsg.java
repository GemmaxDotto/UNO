
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

                    if (receivedMessage != null && receivedMessage.equals("GO")) {
                        cond.Game.on = true;
                    }
                    if(receivedMessage != null && receivedMessage.strip().equals("ok")){
                        cond.Game.gestisciRispostaLascia();
                        System.out.println("ok ricevuto");
                    }
                    if(receivedMessage != null &&receivedMessage.strip().split(";")[0].equals("CentralCard")){
                        cond.tempCard = GameManaging.fromString(receivedMessage.strip().split(";")[1]);
                        cond.Game.numeroAvv=Integer.parseInt(receivedMessage.strip().split(";")[2]);
                        System.out.println("carta centrale ricevuta");
                    }
                    if(receivedMessage != null &&receivedMessage.strip().split(";")[0].equals("pesca")){
                        cond.Game.gestisciRispostaPesca(receivedMessage.strip().split(";")[2]);
                        System.out.println("carta pescata");
                    }
                    /* if(receivedMessage != null &&receivedMessage.strip().split(";")[0].equals("uno")){
                        cond.Game.gestisciRispostaUno(receivedMessage.strip().split(";")[1]);
                        System.out.println("uno ricevuto");
                    }
                    if(receivedMessage != null &&receivedMessage.strip().split(";")[0].equals("vittoria")){
                        cond.Game.gestisciRispostaVittoria(receivedMessage.strip().split(";")[1]);
                        System.out.println("vittoria ricevuta");
                    }*/
                    //risposta con carta centrale da cambiare
                    if(receivedMessage != null &&receivedMessage.strip().split(";")[0].equals("CentralChangeCard")){
                        cond.Game.gestisciRispostaCambia(receivedMessage.strip().split(";")[1]);
                        System.out.println("cambia centrale");
                    }
     

                    Thread.sleep(500); 
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }
}
