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
mazzo_tavolo=[]

def handle_client(client_socket, shared_message, shutdown_event, giocatori):
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
                nomeGiocatore=received_message.strip().split(";")[0]
                #nomeGiocatore = "giocatore" + str(clients)
                #nuovo_giocatore = player.giocatore(nomeGiocatore,0,client_socket,clients)
                
                nuovo_giocatore = player.giocatore(nomeGiocatore,0,client_socket)

               
                giocatori.append(nuovo_giocatore)
                conferma_message = "ok;start;"+str(clients)+"\r\n"

                #timer_thread = myt.MyTimerThread(30000, StartGame)
                #timer_thread.start();

                print(f"Giocatori: {giocatori[0].get_nick()}")
                print(f"Invio: {conferma_message}")
                msg.send_messages(nomeGiocatore,conferma_message,giocatori)
                #client_socket.sendall(conferma_message.encode())
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
                    conferma_message= received_message.strip().split(";")[1]+";" +cards + "\r\n"
                    print(conferma_message)
                    giocatori[numero].imposta_mazzo(cards)
                    #socketTemp=giocatori[numero].get_c_socket()
                     #client_socket.sendall(conferma_message.encode())
                    msg.send_messages(received_message.strip().split(";")[1],conferma_message,giocatori)
                    
                    
            elif game==True and received_message.strip().split(";")[1]=="first":
                card=random.choice(mazzo_temp)
                conferma_message = card + "\r\n"   
                
                for numero in range(clients):
                    msg.send_messages(giocatori[numero].get_nick(),conferma_message,giocatori)
                
                #client_socket.sendall(conferma_message.encode())
            
            elif game==True and received_message.strip().split(";")[1]=="pesca":
                nickClient = received_message.strip().split(";")[0]
                giocatoreClient,pos = searchClient(nickClient)
                card_pescata = pesca_carta()

                giocatoreClient.aggiungi_carta(card_pescata)                
                conferma_message = nickClient + ";" + "mazzo" + ";" +giocatoreClient.getMazzoToString()+ "\r\n"
                msg.send_messages(nickClient,conferma_message,giocatori)
                #giocatoreClient.c_socket.sendall(conferma_message.encode())
            elif game==True and received_message.strip().split(";")[1]=="lascia":
                card_lasciata = received_message.strip().split(";")[2]
                correct,speciale = checkIsValid(card_lasciata)

                #if correct_card or correct_speciale:
                if True:
                    lasciaCarta(card_lasciata)

                    nickClient = received_message.strip().split(";")[0]
                    giocatoreClient,pos = searchClient(nickClient) 

                    if(giocatoreClient.getNumeroCarte()==1 & received_message.strip().split(";")[3]!="1"):
                        for i in range(0,3):
                            giocatoreClient.aggiungi_carta(pesca_carta())
<<<<<<< HEAD

                    
                    conferma_message = nickClient + ";" + "mazzo" + ";" +giocatoreClient.getMazzoToString()   

                    if correct_speciale:
                        msgSpeciale = getMsgSpeciale(card_lasciata)         
                        shared_message.set(msg)
                    msg.send_messages(nickClient,conferma_message,giocatori)
                else:
                    #carta non valida
                    message = nickClient+";errore;carta_non_valida"
            
                
=======
>>>>>>> de9cdacd77aeb25c5593b7e9f0d13f0db690ee28

                    
                    conferma_message = nickClient + ";" + "mazzo" + ";" +giocatoreClient.getMazzoToString()   

                    if correct_speciale:
                        msgSpeciale = getMsgSpeciale(card_lasciata)         
                        shared_message.set(msg)
                    giocatoreClient.get_c_socket.sendall(conferma_message.encode())
                else:
                    #carta non valida
                    message = nickClient+";errore;carta_non_valida"
            else:
                altro_message = "err"
                client_socket.sendall(altro_message.encode())
                print(f"Errore durante la gestione del client: {received_message}")

            

    except Exception as e:
        print(f"Errore durante la gestione del client: {e}")

    finally:
        client_socket.close()


def getMsgSpeciale(card):
    return
 
<<<<<<< HEAD
def getMsgSpeciale(card):
    return
 
=======
>>>>>>> de9cdacd77aeb25c5593b7e9f0d13f0db690ee28
def checkIsValid(card_lasciata):
    speciale = checkCartaSpeciale(card_lasciata)
    correct = False

    last_color = mazzo_tavolo[-1][1]
    last_number = mazzo_tavolo[-1][0]


    if speciale and card_lasciata.rfind("_"):
        color_card_speciale = card_lasciata.split("_")[1]
        if not color_card_speciale == last_color:
            correct = False
    elif speciale:
        correct = speciale
    else:   
        number_card=card_lasciata[0]
        color_card=card_lasciata[1]
        
        if number_card==last_number or color_card==last_color:
            correct = True   
        else:
            correct = False 
    return correct,speciale
<<<<<<< HEAD

def checkCartaSpeciale(card):
    if len(card)>2:
        return True
    return False
=======
>>>>>>> de9cdacd77aeb25c5593b7e9f0d13f0db690ee28

def lasciaCarta(carta,posGiocatore):
    mazzo_tavolo.append(carta)
    giocatori[posGiocatore].rimuoviCarta(carta)

def checkCartaSpeciale(card):
    if len(card)>2:
        return True
    return False
def create_seven(): 
    cards=""   
    
    for numero in range(7):
      card=random.choice(mazzo_temp)
      cards+=card+"-"
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

    carte_speciali = ['Skip_', 'Reverse_', 'Draw Two_']

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
        if giocatore.get_nick() == nickClient:
            return giocatore, i
        i += 1
        
    return None, None

      
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
        listen_thread = threading.Thread(target=msg.listen_for_clients, args=(server_socket, shared_message, shutdown_event, handle_client, giocatori))
        listen_thread.start()

        send_thread = threading.Thread(target=msg.send_messages, args=(shared_message, shutdown_event, giocatori))
        send_thread.start()

        listen_thread.join()
    
    


    except KeyboardInterrupt:
        print("Server terminato manualmente.")
    
    finally:
        shutdown_event.set()
        server_socket.close()

if __name__ == "__main__":
    main()

