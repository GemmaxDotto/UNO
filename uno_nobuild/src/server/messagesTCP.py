import threading
import server as svr  # Presumo che server sia un modulo o un file Python importato

# Aggiungi un blocco per la sincronizzazione
giocatori_lock = threading.Lock()

def send_messages(giocatore_nick, messaggio, giocatori):
    """
    Funzione per inviare un messaggio a un giocatore specifico.

    Args:
    - giocatore_nick (str): Il nickname del giocatore a cui inviare il messaggio.
    - messaggio (str): Il messaggio da inviare al giocatore.
    - giocatori (list): Una lista di oggetti giocatore a cui inviare il messaggio.

    """
    try:
        with giocatori_lock:
            giocatore_destinatario = None
            # Cerca il giocatore nella lista in base al nickname
            for giocatore in giocatori:
                if giocatore.get_nick().strip() == giocatore_nick.strip():
                    giocatore_destinatario = giocatore
                    break

        if giocatore_destinatario:
            # Invia il messaggio al socket del giocatore trovato
            giocatore_destinatario.get_c_socket().sendall(messaggio.encode('utf-8'))
        else:
            print(f"Giocatore con nickname {giocatore_nick} non trovato.")

    except Exception as e:
        print(f"Errore durante l'invio del messaggio al giocatore {giocatore_nick}: {e}")

def listen_for_clients(server_socket, shared_message, shutdown_event, target, giocatori):
    """
    Funzione per ascoltare connessioni in arrivo dai client.

    Args:
    - server_socket: Il socket del server.
    - shared_message: Un messaggio condiviso tra i client.
    - shutdown_event: Un oggetto Event utilizzato per controllare la chiusura del server.
    - target: La funzione target da eseguire per gestire il client.
    - giocatori (list): Una lista di giocatori connessi al server.
    
    """
    try:
        while not shutdown_event.is_set():
            if svr.accept_connections == True:
                # Accetta le connessioni dai client
                client_socket, client_address = server_socket.accept()
                print(f"Connessione accettata da {client_address}")
                
                print(str(0))
                
                # Crea un thread separato per gestire la connessione del client
                client_thread = threading.Thread(target=target, args=(client_socket, shared_message, shutdown_event, giocatori))
                client_thread.start()
            else:
                print("Connessioni non accettate")

    except Exception as e:
        print(f"Errore durante l'ascolto dei client: {e}")
