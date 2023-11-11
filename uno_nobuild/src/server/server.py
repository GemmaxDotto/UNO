import socket
import threading

def handle_client(client_socket):
    try:
        # Ricevi i dati dal client
        data = client_socket.recv(1024)
        print(f"Ricevuto: {data.decode()}")

        # Puoi qui gestire la logica del gioco Uno in base ai dati ricevuti
        # Ad esempio, aggiornare lo stato di gioco, rispondere con un messaggio, ecc.

    except Exception as e:
        print(f"Errore durante la gestione del client: {e}")
    finally:
        # Chiudi la connessione
        client_socket.close()

def start_server():
    # Specifica l'indirizzo IP e la porta su cui il server ascolterà le connessioni
    server_address = "localhost"
    server_port = 666

    # Crea il socket del server
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Associa il socket all'indirizzo IP e alla porta
    server_socket.bind((server_address, server_port))

    # Metti il server in ascolto su un massimo di 5 connessioni in coda
    server_socket.listen(5)
    print(f"Server in ascolto su {server_address}:{server_port}")

    try:
        while True:
            # Accetta la connessione dal client
            client_socket, client_address = server_socket.accept()
            print(f"Connessione accettata da {client_address}")

            # Crea un thread per gestire il client
            client_thread = threading.Thread(target=handle_client, args=(client_socket,))
            client_thread.start()

    except KeyboardInterrupt:
        print("Server terminato in modo manuale.")
    finally:
        # Chiudi il socket del server
        server_socket.close()

if __name__ == "__main__":
    start_server()
