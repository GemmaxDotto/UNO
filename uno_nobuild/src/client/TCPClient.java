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

    public void Join(){




        //TCPClient client=new TCPClient("localhost",12346);
            NickNameWindow nicknameW=new NickNameWindow();
            String nickNameString=nicknameW.showInputDialog();
        
            String message=nickNameString+";start";

        sendMessage(message);

        // Dichiarazione di una variabile per la finestra di attesa
wait waiting = new wait();

// Avvio di un thread separato per la gestione dei messaggi dal server
new Thread(() -> {
    // Mostra la finestra di attesa all'inizio
    SwingUtilities.invokeLater(waiting::showWaitWindow);

    // Loop finché il messaggio ricevuto dal server è diverso da "ok"
    while (!receiveMessage().equals("ok")) {
        try {
            // Aggiorna la finestra di attesa (puoi anche farlo solo quando ricevi "no")
            SwingUtilities.invokeLater(waiting::showWaitWindow);

            // Attendi per evitare un loop troppo veloce
           Thread.sleep(500); // 1 secondo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    SwingUtilities.invokeLater(() -> {
        waiting.closeWaitWindow(() -> {
            // Altre operazioni dopo l'attesa...
            System.out.println("Finestra di attesa chiusa!");
            // Avvia la GUI principale
            SwingUtilities.invokeLater(ClientGUI::new);
        });
    });

    
}).start();

       



    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public String receiveMessage() {
         return "start";
       /*  try {
            if(input.readLine().equals("ok;"))
            {
                return "start";
            }
            else
            return "not enough players";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } */
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
