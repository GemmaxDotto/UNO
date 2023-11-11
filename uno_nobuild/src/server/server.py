import socket
import threading
from queue import Queue, Empty 



def handle_client(client_socket, message_queue, shutdown_event):
    try:
        # Invia un messaggio al client
        message_to_send = "Ciao, client!"
        client_socket.sendall(message_to_send.encode())

        # Ricevi messaggi dal client e aggiungili alla coda
        while True:
            data = client_socket.recv(1024)
            if not data:
                break

            received_message = data.decode()
            message_queue.put((client_socket, received_message))

    except Exception as e:
        print(f"Errore durante la gestione del client: {e}")

    finally:
        # Chiudi il socket del client
        client_socket.close()

def send_messages(message_queue, shutdown_event):
    try:
        while not shutdown_event.is_set():
            try:
                # Prendi un messaggio dalla coda e invialo a tutti i client
                message = message_queue.get(timeout=1)
                client_socket, message_to_send = message
                print(f"Invio messaggio a tutti i client: {message_to_send}")
                client_socket.sendall(message_to_send.encode())

            except Empty:
                pass

    except Exception as e:
        print(f"Errore durante l'invio dei messaggi: {e}")

def listen_for_clients(server_socket, message_queue, shutdown_event):
    try:
        while not shutdown_event.is_set():
            # Accetta una connessione dal client
            client_socket, client_address = server_socket.accept()
            print(f"Connessione accettata da {client_address}")

            # Crea un thread per gestire il client
            client_thread = threading.Thread(target=handle_client, args=(client_socket, message_queue, shutdown_event))
            client_thread.start()

    except Exception as e:
        print(f"Errore durante l'ascolto dei client: {e}")

def main():
    server_address = "localhost"
    server_port = 12346

    # Crea un socket del server
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Lega il socket all'indirizzo e alla porta del server
    server_socket.bind((server_address, server_port))

    # Metti il socket in ascolto
    server_socket.listen()

    print(f"Server in ascolto su {server_address}:{server_port}")

    # Coda per la comunicazione tra i thread
    message_queue = Queue()

    # Oggetto di controllo per segnalare l'arresto dei thread
    shutdown_event = threading.Event()

    try:
        # Crea un thread per l'ascolto dei client
        listen_thread = threading.Thread(target=listen_for_clients, args=(server_socket, message_queue, shutdown_event))
        listen_thread.start()

        # Crea un thread per l'invio dei messaggi
        send_thread = threading.Thread(target=send_messages, args=(message_queue, shutdown_event))
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
