import socket
import random
import giocatore as player
import MyTimerThread as myt
import threading
import messagesTCP as msg

clients=0
mazzo_uno=[]
mazzo_temp=[]
game=False
giocatori=[]

def handle_client(client_socket, shared_message, shutdown_event):
    try:
        while not shutdown_event.is_set():
            data = client_socket.recv(1024)
            if not data:
                break

            received_message = data.decode()
            
            #parts = received_message.strip().split(';')
            #print("Parti del messaggio:", parts)
            
            if received_message.strip().split(";")[1] == "start":
                global clients
                clients += 1
                nomeGiocatore = "giocatore" + str(clients)
                #nuovo_giocatore = player.giocatore(nomeGiocatore,0,client_socket,clients)
                nuovo_giocatore = player.giocatore(nomeGiocatore,0,client_socket)

                giocatori.append(nuovo_giocatore)
                conferma_message = "ok;start;"+str(clients)+"\r\n"

                #timer_thread = myt.MyTimerThread(30000, StartGame)
                #timer_thread.start();

                print(f"Invio: {conferma_message}")
                client_socket.sendall(conferma_message.encode())
                global game
                game=True 

                if clients==1 :
                    mazzo_uno = crea_mazzo_uno()
                    global mazzo_temp
                    mazzo_temp=mazzo_uno
                    print(mazzo_uno)
                # Setta la variabile condivisa
                #shared_message.set(received_message)
               
                     
            elif game==True and received_message.strip().split(";")[0]=="game":
                for numero in range(clients):
                    cards=create_seven()
                    conferma_message=cards+"\r\n"
                    print(conferma_message)
                    giocatori[numero].imposta_mazzo(cards)
                    #socketTemp=giocatori[numero].get_c_socket()
                    client_socket.sendall(conferma_message.encode())
            
            elif game==True and received_message.strip().split(";")[0]=="pesca":
                card_pescata = pesca_carta()
                nickClient = received_message.strip().split(";")[1]
                giocatoreClient,pos = searchClient(nickClient)

                giocatoreClient.aggiungi_carta(card_pescata)                

                giocatoreClient.get_c_socket.sendall(conferma_message.encode())
                
                
            else:
                altro_message = "err"
                client_socket.sendall(altro_message.encode())
                print(f"Errore durante la gestione del client: {received_message}")

            

    except Exception as e:
        print(f"Errore durante la gestione del client: {e}")

    finally:
        client_socket.close()
 

def create_seven(): 
    cards=""   
    
    for numero in range(7):
      card=random.choice(mazzo_temp)
      cards+=card+";"
      mazzo_temp.remove(card)
    return cards

def pesca_carta():
    card = mazzo_temp[-1]
    mazzo_temp.pop()
    return card
  


def crea_mazzo_uno():
    colori = ['R', 'G', 'B', 'V']  # Rosso, Verde, Blu, Verde
    numeri = [str(i) for i in range(10)]  # Numeri da 0 a 9

    carte_numerate = [f"{numero}{colore}" for colore in colori for numero in numeri]

    carte_speciali = ['Skip-', 'Reverse-', 'Draw Two-']

    mazzo = carte_numerate * 2  # Doppie carte numerate
    mazzo += [f"{speciale}{colore}" for colore in colori for speciale in carte_speciali] * 2  # Doppie carte speciali

    mazzo += ['Draw Four'] * 4  # Quattro carte Wild e Wild Draw Four
    mazzo += ['Change Color'] * 4  # Quattro carte Wild e Wild Draw Four

    # Mescola il mazzo
    random.shuffle(mazzo)

    return mazzo

def searchClient(nickClient):
    i = 0
    for giocatore in giocatori:
        if giocatore.get_nick == nickClient:
            return giocatore,i
        i+=1
        
    return None,None

      
def StartGame():
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
    msg.send_messages(conferma_message.encode())
    

def main():
    server_address = "localhost"
    server_port = 666

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((server_address, server_port))
    server_socket.listen()

    print(f"Server in ascolto su {server_address}:{server_port}")

    shared_message = threading.Event()
    shutdown_event = threading.Event()
    i= 0
    try:
        listen_thread = threading.Thread(target=msg.listen_for_clients, args=(server_socket, shared_message, shutdown_event,handle_client))
        listen_thread.start()

        send_thread = threading.Thread(target=msg.send_messages, args=(shared_message, shutdown_event))
        send_thread.start()

        listen_thread.join()
    
    


    except KeyboardInterrupt:
        print("Server terminato manualmente.")
    
    finally:
        shutdown_event.set()
        server_socket.close()

if __name__ == "__main__":
    main()

