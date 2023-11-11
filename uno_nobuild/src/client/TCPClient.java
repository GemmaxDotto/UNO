import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

    public static void main(String[] args) {
        try {
            // Specifica l'indirizzo IP e la porta del server
            String serverAddress = "localhost";
            int serverPort = 666; // Sostituisci con la porta del tuo server

            // Crea una connessione TCP al server
            Socket socket = new Socket(serverAddress, serverPort);

            // Ottieni lo stream di output dalla socket
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Ottieni l'input dell'utente (puoi usarlo per inviare comandi al server)
            Scanner scanner = new Scanner(System.in);

            // Esempio: invia un messaggio al server
            System.out.print("Inserisci il messaggio da inviare al server: ");
            String messageToSend = scanner.nextLine();

            // Invia il messaggio al server
            output.println(messageToSend);

            // Chiudi la connessione
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

