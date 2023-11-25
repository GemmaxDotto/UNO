
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
                    if(receivedMessage.strip().equals("ok")){
                        cond.Game.gestisciRispostaLascia();
                        System.out.println("ok ricevuto");
                    }
    

                    Thread.sleep(500); 
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }
}
