
/*
 *  thread per la ricezione dei messaggi client
 * 
 */


public class threadMsg extends Thread {

    private TCPClient tcpClient;
    private GameManaging Game;

    public threadMsg(TCPClient tcpClient, GameManaging Game) {
        this.tcpClient = tcpClient;
        this.Game = Game;
    }

    public void start() {

        // Avvio di un thread separato per la gestione dei messaggi dal server
        new Thread(() -> {
            try {

                while (true) {
                    String receivedMessage = tcpClient.receiveMessage();

                    if (receivedMessage != null && receivedMessage.equals("GO")) {
                        Game.on = true;
                    }

                    Thread.sleep(500); 
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }
}
