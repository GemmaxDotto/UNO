import socket
import random
import time
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
numeroTurno=1
verso_turno=True

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
                    global mazzo_uno
                    mazzo_uno = crea_mazzo_uno()
                    global mazzo_temp
                    mazzo_temp=mazzo_uno
                    print(mazzo_uno)
                    global numeroTurno
                    numeroTurno=1
                # Setta la variabile condivisa
                #shared_message.set(received_message)
               
                     
            elif game==True and received_message.strip().split(";")[0]=="game":
                for numero in range(clients):
                    cardArray=[]
                    cards,cardArray=create_seven()
                    conferma_message= received_message.strip().split(";")[1]+";" +cards + "\r\n"
                    print(conferma_message)


                    giocatori[numero].imposta_mazzo(cardArray)
                    #socketTemp=giocatori[numero].get_c_socket()
                     #client_socket.sendall(conferma_message.encode())

                    msg.send_messages(received_message.strip().split(";")[1],conferma_message,giocatori)
                # 30 secondi di attesa dei Client 10 per Debug se mi scordo rimarranno 10
                time.sleep(10)
                for numero in range(clients):
                    msg.send_messages(giocatori[numero].get_nick(),"GO"+"\r\n",giocatori)
                    
                    
                    
            elif game==True and received_message.strip().split(";")[1]=="first":
                card=random.choice(mazzo_temp)
                mazzo_tavolo.append(card)
                conferma_message = card +";"+str(clients-1)+ "\r\n"   #invio carta centrale+ numero di avversari
                
                for numero in range(clients):
                    msg.send_messages(giocatori[numero].get_nick(),conferma_message,giocatori)
                
                #client_socket.sendall(conferma_message.encode())
            
            elif game==True and received_message.strip().split(";")[1]=="pesca":
                #controllo se turno corretto
                nickClient = received_message.strip().split(";")[0]
                giocatoreClient,pos = searchClient(nickClient)
                card_pescata = pesca_carta()

                giocatoreClient.aggiungi_carta(card_pescata)               
                #manda solo carta pescata 
                conferma_message=nickClient+";"+card_pescata
                conferma_message = nickClient + ";" + "mazzo" + ";" +giocatoreClient.getMazzoToString()+ "\r\n"
                msg.send_messages(nickClient,conferma_message,giocatori)
                spostaTurno(1,cambio_verso= False)
                
            elif game==True and received_message.strip().split(";")[1]=="lascia":
                #controllo se turno corretto

                card_lasciata = received_message.strip().split(";")[2]

                correct,speciale = checkIsValid(card_lasciata)

                
                nickClient = received_message.strip().split(";")[0]

                if correct:


                    giocatoreClient,pos = searchClient(nickClient) 
                    print("1")
                    print(pos)
                    print(giocatoreClient.getMazzoToString())
                    lasciaCarta(card_lasciata,pos)
                    print("2")
                    
                    print(received_message.strip())
                    

                    if(giocatoreClient.get_numero_carte()==1 and received_message.strip().split(";")[3]!="1"):
                        print("3")
                        for i in range(0,3):
                            giocatoreClient.aggiungi_carta(pesca_carta())

                    #invia solo ok non mazzo

                    print("4")

                    conferma_message = "ok"+"\r\n"


                    msg.send_messages(nickClient,conferma_message,giocatori)
                    print("5")

                    spostaTurno(1,cambio_verso= False)
                    print("6")




                if speciale and correct:
                        msgSpeciale,carte = gestisciSpeciale(card_lasciata)    
                        print(msgSpeciale)
                        if len(msgSpeciale)>0:     
                            for numero in range(clients):
                                conferma_message=msgSpeciale
                                msg.send_messages(giocatori[numero].get_nick(),conferma_message,giocatori)
                        if len(carte)>0:
                            giocatoreSuccessivo = getGiocatoreSuccessivo()
                            for i in range(len(carte)):
                                giocatoreSuccessivo.aggiungi_carta(carte[i])
                            conferma_message = nickClient + ";" + "mazzo" + ";" +giocatoreClient.getMazzoToString()+"\r\n"
                            msg.send_messages(nickClient,conferma_message,giocatori)
                            spostaTurno(1,cambio_verso= False)

                            
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

def spostaTurno(numero,cambio_verso):
    global verso_turno,numeroTurno
    if cambio_verso:
        verso_turno=not verso_turno
    
    numeroTurno = addTurno(numero,verso_turno,numeroTurno)

def addTurno(num,verso,numTurno):
    global clients

    if verso:
        numTurno+=num
        if numTurno>clients: #giro avanza oltre al max client e deve tornare al primo
            numTurno-=clients
    else:
        numTurno-=num
        if numTurno<1: #giro avanza oltre al min client e deve tornare all'ultimo
            numTurno+=clients
    return numTurno



def getGiocatoreSuccessivo():
    #basato sul futuro giocatore del prossimo turno
    global numeroTurno,verso_turno
    nTurnoTMP = numeroTurno
    nTurnoTMP = addTurno(1,verso_turno,nTurnoTMP)
    player = giocatori[nTurnoTMP]
    return player
    

def gestisciSpeciale(card):
    pesca=0
    carte=[]
    msg=""
    if card.rfind("_"):
        nameSpecial = card.split("_")[0]
        if nameSpecial=="Reverse":
            spostaTurno(1,cambio_verso= True)
        elif nameSpecial=="Skip":
            nickGiocatoreSuccessivo=getGiocatoreSuccessivo().get_nick()
            spostaTurno(2,cambio_verso= False)
            msg = "speciale;"+nickGiocatoreSuccessivo+";salta_turno"
        elif nameSpecial=="Draw Two":
            nickGiocatoreSuccessivo=getGiocatoreSuccessivo().get_nick()
            msg = "speciale;"+nickGiocatoreSuccessivo+";pesca_due"
            pesca=2


    elif card=="Draw Four":
        nickGiocatoreSuccessivo=getGiocatoreSuccessivo().get_nick()
        msg = "speciale;"+nickGiocatoreSuccessivo+";pesca_quattro"
        pesca=4
    
    elif card=="Change Color":
        spostaTurno(-1,cambio_verso= False)

    for i in range(pesca):
        carte.append(pesca_carta())

    return msg,carte


        
 
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

def lasciaCarta(carta,posGiocatore):
    mazzo_tavolo.append(carta)
    giocatori[posGiocatore].rimuovi_carta(carta)

def checkCartaSpeciale(card):
    if len(card)>2:
        return True
    return False

def create_seven(): 
    cards=""   
    cardArray=[]
    
    for numero in range(7):
      card=random.choice(mazzo_temp)
      cardArray.append(card)
      cards+=card+"-"
      mazzo_temp.remove(card)
      
    return cards,cardArray

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



#tempCard=fromString(mess.split(";")[1]);