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

    public void Join() {

        
        
        NickNameWindow nicknameW = new NickNameWindow();
        String nickNameString = nicknameW.showInputDialog();

        String message = nickNameString + ";start";

        sendMessage(message);
        //String line=receiveMessage();
        //String line = input.readLine();
        wait waiting=new wait();
           //String receivedMessage = receiveMessage().trim();
        String line=receiveMessage();
        

            // Mostra la finestra di attesa all'inizio
            
           // SwingUtilities.invokeLater(waiting::showWaitWindow);
        
            
                // Loop finché non ricevi un messaggio specifico dal server
                //while (true) {

                    /* int length=is.read(buffer,0,buffer.length);
                    String receivedMessage = new String(buffer,0,length); */
                    
                    String receivedMessage = line;
        
                    if (receivedMessage != null && receivedMessage.split(";")[1].equals("start")) {
                       // break;
                       waiting.closeWaitWindow(() -> {
                        // Altre operazioni dopo l'attesa...]
                        Startgame();
                   
                    });
                    }
        
                    // Aggiorna la finestra di attesa (puoi anche farlo solo quando ricevi "no")
                    //SwingUtilities.invokeLater(waiting::showWaitWindow);
        
                    // Attendi per evitare un loop troppo veloce
                    //Thread.sleep(500); // 0,5 secondo
               // }

                
        
                /* waiting.closeWaitWindow(() -> {
                    // Altre operazioni dopo l'attesa...
                   
                }); */

        
           

    }

    private void Startgame() {
        sendMessage("game;");
        String myCardString=receiveMessage();
        UnoDeck myCards=new UnoDeck();
        for(int i=0;i<7;i++)
            myCards.addCards(fromString(myCardString.split(";")[i]));
        
            //new UnoDeckGUI(myCards).setVisible(true);
    }

    // Metodo statico per creare una UnoCard da una stringa
    public static UnoCard fromString(String cardString) {
        if (cardString.length() < 2 || cardString.length() > 3) {
            // La stringa deve essere di lunghezza 2 o 3 (ad es. "1V" o "DS")
            throw new IllegalArgumentException("La stringa deve essere di lunghezza 2 o 3");
        }

        if (cardString.length() == 2) {
            // Carta normale
            int numero = Character.getNumericValue(cardString.charAt(0));
            String colore = cardString.substring(1);
            return new UnoCard(numero, colore, false);
        } else {
            // Carta speciale
            String colore = cardString.substring(0, 1);
            return new UnoCard(0, colore, true);
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
