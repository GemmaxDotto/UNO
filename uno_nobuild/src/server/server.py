import socket
import random

clients=0
mazzo_uno=[]
game=False


def handle_client(client_socket, shared_message, shutdown_event):
    try:
        while not shutdown_event.is_set():
            data = client_socket.recv(1024)
            if not data:
                break

            received_message = data.decode()
            parts = received_message.split(';')
            print("Parti del messaggio:", parts)
            
            if received_message.split(';')[1].strip() == "start":
                global clients
                clients += 1
                client_info = (client_socket, received_message.split(';')[0].strip())
                global clientsInfo
                clientsInfo.append(client_info)
                conferma_message = "ok;start"+"\r\n";
                """ timer_thread = MyTimerThread(30000, StartGame)
                timer_thread.start(); """
                print(f"Invio: {conferma_message}")
                client_socket.sendall(conferma_message.encode())
                
                # Setta la variabile condivisa
                shared_message.set(received_message)
            elif game==True:
                # Esempio di utilizzo
                mazzo_uno = crea_mazzo_uno()
                print(mazzo_uno)
                shared_message.set(received_message)
            else:
                altro_message = "err"
                client_socket.sendall(altro_message.encode())
                print(f"Errore durante la gestione del client: {received_message}")

            

    except Exception as e:
        print(f"Errore durante la gestione del client: {e}")

    finally:
        client_socket.close()
      

def crea_mazzo_uno():
    colori = ['R', 'G', 'B', 'V']  # Rosso, Verde, Blu, Verde
    numeri = [str(i) for i in range(10)]  # Numeri da 0 a 9

    carte_numerate = [f"{numero}{colore}" for colore in colori for numero in numeri]

    carte_speciali = ['Skip', 'Reverse', 'Draw Two']

    mazzo = carte_numerate * 2  # Doppie carte numerate
    mazzo += [f"{colore}{speciale}" for colore in colori for speciale in carte_speciali] * 2  # Doppie carte speciali

    mazzo += ['Draw Four'] * 4  # Quattro carte Wild e Wild Draw Four

    # Mescola il mazzo
    random.shuffle(mazzo)

    return mazzo


      
""" def StartGame():
    game=True
    if clients>=1:
        conferma_message = "game;2"+"\r\n";
        print(f"Invio: {conferma_message}")
    elif clients>=3:
        conferma_message = "game;3"+"\r\n";
        print(f"Invio: {conferma_message}")  
    elif clients>=4:
        conferma_message = "game;4"+"\r\n";
        print(f"Invio: {conferma_message}")
    send_messages(conferma_message.encode())
     """

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

def listen_for_clients(server_socket, shared_message, shutdown_event):
    try:
        while not shutdown_event.is_set():
            client_socket, client_address = server_socket.accept()
            print(f"Connessione accettata da {client_address}")

            client_thread = threading.Thread(target=handle_client, args=(client_socket, shared_message, shutdown_event))
            client_thread.start()

    except Exception as e:
        print(f"Errore durante l'ascolto dei client: {e}")

import threading
import time

class MyTimerThread(threading.Thread):
    def __init__(self, interval, target_function):
        super().__init__()
        self.interval = interval
        self.target_function = target_function
        self._stop_event = threading.Event()

    def stop(self):
        self._stop_event.set()

    def run(self):
        while not self._stop_event.is_set():
            time.sleep(self.interval)
            self.target_function()

# Funzione da eseguire quando il timer scatta



def main():
    server_address = "localhost"
    server_port = 666

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((server_address, server_port))
    server_socket.listen()

    print(f"Server in ascolto su {server_address}:{server_port}")

    shared_message = threading.Event()
    shutdown_event = threading.Event()

    try:
        listen_thread = threading.Thread(target=listen_for_clients, args=(server_socket, shared_message, shutdown_event))
        listen_thread.start()

        send_thread = threading.Thread(target=send_messages, args=(shared_message, shutdown_event))
        send_thread.start()

        listen_thread.join()

    except KeyboardInterrupt:
        print("Server terminato manualmente.")
    
    finally:
        shutdown_event.set()
        server_socket.close()

if __name__ == "__main__":
    main()

