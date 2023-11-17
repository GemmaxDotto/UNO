import socket
import threading

def handle_client(client_socket, message_event, shutdown_event):
    try:
        

        # Ricevi messaggi dal client e aggiornali nella struttura dati
        while True:
            data = client_socket.recv(1024)
            if not data:
                break

            received_message = data.decode()
            
            parts = received_message.split(';')
            print("Parti del messaggio:", parts)

            if received_message.split(';')[1].strip() == "start":# La condizione è soddisfatta, invia un messaggio di conferma
             conferma_message = "ok" 
             print(f"Invio: {conferma_message}")
             client_socket.sendall(conferma_message.encode())
            else:# La condizione non è soddisfatta, esegui altre azioni o invia un messaggio diverso se necessario
             altro_message = "no"
             client_socket.sendall(altro_message.encode())
             print(f"Errore durante la gestione del client: {received_message}")

             #message_event.set(received_message)

    except Exception as e:
        print(f"Errore durante la gestione del client: {e}")

    finally:
        # Chiudi il socket del client
        client_socket.close()

def send_messages(message_event, shutdown_event):
    try:
        while not shutdown_event.is_set():
            # Attendere fino a quando un nuovo messaggio è disponibile
            message_event.wait()

            # Ottenere il messaggio e reimpostare l'evento
            server_message = message_event.get()
            message_event.clear()

            # Puoi fare ciò che vuoi con il messaggio del server
            print(f"Ricevuto messaggio dal client: {server_message}")

    except Exception as e:
        print(f"Errore durante la gestione dei messaggi: {e}")

def listen_for_clients(server_socket, message_event, shutdown_event):
    try:
        while not shutdown_event.is_set():
            # Accetta una connessione dal client
            client_socket, client_address = server_socket.accept()
            print(f"Connessione accettata da {client_address}")

            # Crea un thread per gestire il client
            client_thread = threading.Thread(target=handle_client, args=(client_socket, message_event, shutdown_event))
            client_thread.start()

    except Exception as e:
        print(f"Errore durante l'ascolto dei client: {e}")

def main():
    server_address = "localhost"
    server_port = 12346  # Specifica la porta desiderata

    # Crea un socket del server
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Lega il socket all'indirizzo e alla porta del server
    server_socket.bind((server_address, server_port))

    # Metti il socket in ascolto
    server_socket.listen()

    print(f"Server in ascolto su {server_address}:{server_port}")

    # Creare un evento per la comunicazione tra i thread
    message_event = threading.Event()

    # Oggetto di controllo per segnalare l'arresto dei thread
    shutdown_event = threading.Event()

    try:
        # Crea un thread per l'ascolto dei client
        listen_thread = threading.Thread(target=listen_for_clients, args=(server_socket, message_event, shutdown_event))
        listen_thread.start()

        # Crea un thread per l'invio dei messaggi
        send_thread = threading.Thread(target=send_messages, args=(message_event, shutdown_event))
        send_thread.start()

        # Attendi l'interruzione da tastiera
        listen_thread.join()

    except KeyboardInterrupt:
        print("Server terminato manualmente.")
    
    finally:
        # Segnala l'arresto dei thread
        shutdown_event.set()

        # Chiudi il socket del server
        server_socket.close()

if __name__ == "__main__":
    main()
