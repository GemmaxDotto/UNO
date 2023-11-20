import threading

def send_messages(shared_message, shutdown_event):
    try:
        while not shutdown_event.is_set():
            # Attendere fino a quando un nuovo messaggio è disponibile
            if shared_message.wait(timeout=1):
                # Ottenere il messaggio e reimpostare la variabile condivisa
                server_message = shared_message.get()
                shared_message.clear()

                # Puoi fare ciò che vuoi con il messaggio del server
                print(f"Ricevuto messaggio dal client: {server_message}")

    except Exception as e:
        print(f"Errore durante la gestione dei messaggi: {e}")



def listen_for_clients(server_socket, shared_message, shutdown_event,target):
    try:
        while not shutdown_event.is_set():
            client_socket, client_address = server_socket.accept()
            print(f"Connessione accettata da {client_address}")
            
            print(str(0))
            
            client_thread = threading.Thread(target=target, args=(client_socket, shared_message, shutdown_event))
            client_thread.start()

    except Exception as e:
        print(f"Errore durante l'ascolto dei client: {e}")