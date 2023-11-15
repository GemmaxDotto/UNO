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


    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public String receiveMessage() {
        try {
            return input.readLine();
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
