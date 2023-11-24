import threading


# Aggiungi un blocco per la sincronizzazione
giocatori_lock = threading.Lock()

def send_messages(giocatore_nick, messaggio, giocatori):
    try:
        with giocatori_lock:
            giocatore_destinatario = None
            for giocatore in giocatori:
                if giocatore.get_nick().strip() == giocatore_nick.strip():
                    giocatore_destinatario = giocatore
                    break

        if giocatore_destinatario:
        # Fai qualcosa con il giocatore trovato
            giocatore_destinatario.get_c_socket().sendall(messaggio.encode('utf-8'))
        else:
            print(f"Giocatore con nickname {giocatore_nick} non trovato.")


    except Exception as e:
        print(f"Errore durante l'invio del messaggio al giocatore {giocatore_nick}: {e}")

def listen_for_clients(server_socket, shared_message, shutdown_event, target, giocatori):
    try:
        while not shutdown_event.is_set():
            client_socket, client_address = server_socket.accept()
            print(f"Connessione accettata da {client_address}")
            
            print(str(0))
            
            client_thread = threading.Thread(target=target, args=(client_socket, shared_message, shutdown_event, giocatori))
            client_thread.start()

    except Exception as e:
        print(f"Errore durante l'ascolto dei client: {e}")