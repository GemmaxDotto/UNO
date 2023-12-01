import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * La classe TCPClient rappresenta un client TCP che si connette a un server
 * remoto e invia/riceve messaggi.
 */
public class TCPClient {
    private Socket socket; // Socket per la connessione al server
    private PrintWriter output; // Flusso di output per inviare messaggi al server
    private BufferedReader input; // Flusso di input per ricevere messaggi dal server

    /**
     * Costruttore per la classe TCPClient. Si connette al server remoto
     * specificato.
     * 
     * @param serverAddress Indirizzo IP del server remoto.
     * @param serverPort    Porta del server remoto.
     */
    public TCPClient(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort); // Connessione al server remoto
            output = new PrintWriter(socket.getOutputStream(), true); // Output per inviare messaggi
            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Input per ricevere messaggi
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invia un messaggio al server.
     * 
     * @param message Il messaggio da inviare.
     */
    public void sendMessage(String message) {
        output.println(message);
    }

    /**
     * Riceve un messaggio dal server.
     * 
     * @return Il messaggio ricevuto dal server, null se la connessione è terminata
     *         o ci sono errori.
     */
    public String receiveMessage() {
        try {
            String receivedMessage = input.readLine(); // Legge un messaggio dal server

            // Restituisce il messaggio ricevuto (null se la connessione è terminata)
            return (receivedMessage != null) ? receivedMessage.trim() : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chiude la connessione con il server.
     */
    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close(); // Chiude il socket
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
