import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

public class TCPClient {
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;

    public TCPClient(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Join() {

        // TCPClient client=new TCPClient("localhost",12346);
        NickNameWindow nicknameW = new NickNameWindow();
        String nickNameString = nicknameW.showInputDialog();

        String message = nickNameString + ";start";

        sendMessage(message);

        
        
        wait waiting=new wait();
        
        new Thread(() -> {
            // Mostra la finestra di attesa all'inizio
            SwingUtilities.invokeLater(waiting::showWaitWindow);
        
            try {
                // Loop finché non ricevi un messaggio specifico dal server
                while (true) {
                    String receivedMessage = receiveMessage().trim();
                    
        
                    if (receivedMessage != null && receivedMessage.equals("ok")) {
                        break;
                    }
        
                    // Aggiorna la finestra di attesa (puoi anche farlo solo quando ricevi "no")
                    SwingUtilities.invokeLater(waiting::showWaitWindow);
        
                    // Attendi per evitare un loop troppo veloce
                    Thread.sleep(500); // 0,5 secondo
                }

                
        
                waiting.closeWaitWindow(() -> {
                    // Altre operazioni dopo l'attesa...
                    System.out.println("Finestra di attesa chiusa!");
                    // Avvia la GUI principale
                    SwingUtilities.invokeLater(ClientGUI::new);
                });
        
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
        

        

    public void sendMessage(String message) {
        output.println(message);
    }

    public String receiveMessage() {
        try {
            // Leggi il messaggio dal socket
            String receivedMessage = input.readLine();

            // Puoi fare ulteriori elaborazioni se necessario

            // Rimuovi spazi bianchi iniziali e finali e confronta con "ok"
            return receivedMessage.trim();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
