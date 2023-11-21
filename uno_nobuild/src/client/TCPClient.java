import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.swing.SwingUtilities;

public class TCPClient {
    private Socket socket;
    private PrintWriter output;
    BufferedReader input;
   // UnoDeck carte;
    //private InputStream is;
    //byte[] buffer =new byte[2000];
    //TCPClient client=new TCPClient("localhost",12346);

    public TCPClient(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //InputStream is=socket.getInputStream();
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public String receiveMessage() {
        try {
            // Leggi il messaggio dal socket
            String receivedMessage = input.readLine();

            // Rimuovi spazi bianchi iniziali e finali
            if(receivedMessage==null)
             return null;
            else
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
