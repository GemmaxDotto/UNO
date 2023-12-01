import socket
import random
import time
import giocatore as player
import MyTimerThread as myt
import threading
import messagesTCP as msg

clients=0
create=0
mazzo_uno=[]
mazzo_temp=[]
game=False
cardCenter=""
giocatori=[]
mazzo_tavolo=[]
numeroTurno=1
verso_turno=True
accept_connections=True
player_to_draw = None
lastCard=False
giocatoreCorrente=None
messPrec="none;none"



def handle_client(client_socket, shared_message, shutdown_event, giocatori):
    try:
        while not shutdown_event.is_set():
            data = client_socket.recv(1024)
            if not data:
                break

            received_message = data.decode()
            
            if received_message.strip().split(";")[1] == "start":
                global clients
                clients += 1
                nomeGiocatore = received_message.strip().split(";")[0]
                nuovo_giocatore = player.giocatore(nomeGiocatore, 0, client_socket) #creo nuovo giocatore
                
                giocatori.append(nuovo_giocatore) #aggiungo giocatore alla lista
                conferma_message = "ok;start;" + str(clients) + "\r\n" #invio conferma

                print(f"Giocatori: {giocatori[0].get_nick()}") #stampa giocatori
                print(f"Invio: {conferma_message}") #stampa messaggio inviato
                msg.send_messages(nomeGiocatore, conferma_message, giocatori) #invio messaggio
                global game
                game = True #imposto game a true

                global create 
                if create == 0: #se è il primo giocatore a connettersi
                    create=1 #imposto create a 1
                    global mazzo_uno 
                    mazzo_uno = crea_mazzo_uno() #creo mazzo
                    global mazzo_temp 
                    mazzo_temp = mazzo_uno #creo mazzo temporaneo
                    print(mazzo_uno)
                    global cardCenter
                    cardCenter=random.choice(mazzo_temp) #pesco una carta dal mazzo temporaneo
                    while(checkCartaSpeciale(cardCenter)==True): #se la carta centrale è speciale
                        cardCenter=random.choice(mazzo_temp) #pesco una nuova carta
                    
                    
                    mazzo_tavolo.append(cardCenter) #aggiungo carta centrale al mazzo del tavolo
                    mazzo_temp.remove(cardCenter) #rimuovo carta centrale da5l mazzo temporaneo
                    global numeroTurno
                    numeroTurno = 1 #imposto numero turno a 1

               
                     
            elif game == True and received_message.strip().split(";")[0] == "game": #se il messaggio ricevuto è game
                    cardArray = [] #creo array di carte
                    cards, cardArray = create_seven()   #creo 7 carte
                    conferma_message = received_message.strip().split(";")[1] + ";" + cards + "\r\n" #invio messaggio di conferma
                    print(conferma_message)
                    
                    indexGiocatore = -1 #imposto indice giocatore a -1
                    for numero in range(clients):
                        if giocatori[numero].get_nick() == received_message.strip().split(";")[1]: #se il nick del giocatore è uguale al nick del messaggio
                            indexGiocatore= numero #imposto indice giocatore a numero
                            
                    giocatori[indexGiocatore].imposta_mazzo(cardArray) #imposto mazzo del giocatore
                    msg.send_messages(received_message.strip().split(";")[1], conferma_message, giocatori) #invio messaggio di conferma
                
                    time.sleep(10) #attendo 10 secondi
                    global accept_connections 
                    accept_connections = False #imposto accept_connections a false
                    for numero in range(clients): #per ogni giocatore
                        msg.send_messages(giocatori[numero].get_nick(),"GO"+"\r\n",giocatori) #invio messaggio GO
                    
                    
                    
            elif game==True and received_message.strip().split(";")[1]=="first":
                conferma_message = "CentralCard;"+cardCenter +";"+str(clients-1)+ "\r\n"   #invio carta centrale+ numero di avversari
                print(f"Invio: {conferma_message}")
                global giocatoreCorrente
                giocatoreCorrente = giocatori[0]  # Inizia con il primo giocatore
                for numero in range(clients):
                    msg.send_messages(giocatori[numero].get_nick(),conferma_message,giocatori)
                
            elif game == True and received_message.strip().split(";")[1] == "passa":  # se il messaggio ricevuto è passa
                 # Verifica se un giocatore deve ancora pescare
                if player_to_draw is None:
                # Invia un messaggio di errore al giocatore attuale
                 error_message = "errore;{};deve_pescare_prima_di_passare".format(player_to_draw.get_nick())
                 msg.send_messages(received_message.strip().split(";")[0], error_message, giocatori)
                 
                else:
                # Consentire al giocatore attuale di passare il turno
                # reimposta player_to_draw a None
                 player_to_draw = None
                 spostaTurno(1,cambio_verso= False)
                # Aggiungi il resto del tuo codice per il passaggio del turno qui
                # ...
            
            elif game==True and received_message.strip().split(";")[1]=="pesca":
                
                
                nickClient = received_message.strip().split(";")[0]
                giocatoreClient,pos = searchClient(nickClient)
                global messPrec
                if not messPrec.strip().split(";")[1]=="pesca" and not messPrec.strip().split(";")[0]==nickClient:
                 getGiocatoreCorrente()
                 if giocatoreCorrente == giocatoreClient:  #controllo se turno corretto
                  player_to_draw = giocatoreClient
                  card_pescata = pesca_carta()

                  giocatoreClient.aggiungi_carta(card_pescata)               
                  #manda solo carta pescata 
                  conferma_message="pesca;"+nickClient+";"+card_pescata+"\r\n"
                  print(f"Invio: {conferma_message}")
                  msg.send_messages(nickClient,conferma_message,giocatori)
                  #il pesca non presuppone cambio turno bottone passa turno?
                  #spostaTurno(1,cambio_verso= False)
                  messPrec=received_message.strip()
                  HandleN_CarteAvversari(nickClient)    
                
            elif game==True and received_message.strip().split(";")[1]=="lascia":
                #controllo se turno corretto
                nickClient = received_message.strip().split(";")[0]
                giocatoreTemp,pos = searchClient(nickClient)
                getGiocatoreCorrente()
                if giocatoreCorrente == giocatoreTemp:  

                    card_lasciata = received_message.strip().split(";")[2]

                    correct,speciale = checkIsValid(card_lasciata)
                
                
                    if speciale and correct:
                        msgSpeciale,carte,carteStr = gestisciSpeciale(card_lasciata) 
                        giocatoreTemp,pos = searchClient(nickClient)
                        lasciaCarta(card_lasciata,pos)

                        global lastCard
                        if (lastCard and giocatoreClient.get_numero_carte()):
                            conferma_message="vittoria;"+nickClient
                            msg.send_messages(nickClient,conferma_message,giocatori)  
                        elif lastCard:
                            lastCard=False

                        print(msgSpeciale)
                        if len(msgSpeciale)>0:     
                            for numero in range(clients):
                                conferma_message=msgSpeciale
                                msg.send_messages(giocatori[numero].get_nick(),conferma_message,giocatori)
                        if len(carte)>0:
                            giocatoreSuccessivo = getGiocatoreSuccessivo()
                            for i in range(len(carte)):
                                giocatoreSuccessivo.aggiungi_carta(carte[i])
                            conferma_message ="carte_pescate" +  ";" + nickClient + ";" +carteStr+"\r\n"
                            msg.send_messages(nickClient,conferma_message,giocatori)
                            spostaTurno(1,cambio_verso= False)
                    elif correct:
                    
                        giocatoreClient,pos = searchClient(nickClient) 
                        print(giocatoreClient.getMazzoToString())
                        lasciaCarta(card_lasciata,pos)
                    
                        if (lastCard and giocatoreClient.get_numero_carte()):
                            conferma_message="vittoria;"+nickClient
                            msg.send_messages(nickClient,conferma_message,giocatori)
                        elif lastCard:
                            lastCard=False

                        


                        print(received_message.strip())
                    

                        if(giocatoreClient.get_numero_carte()==1 and received_message.strip().split(";")[3]!="uno"):
                            cardPescate = ""
                            for i in range(0,3):
                                card_pescata_tmp = pesca_carta()
                                cardPescate+="-"+card_pescata_tmp
                                giocatoreClient.aggiungi_carta(card_pescata_tmp)
                            msg.send_messages(nickClient,"errore;"+nickClient+";uno_non_detto;"+cardPescate,giocatori)
                        elif giocatoreClient.get_numero_carte()==1 and received_message.strip().split(";")[3]=="uno":
                            lastCard=True


                        
                        #invia solo ok non mazzo


                        conferma_message = "ok"+"\r\n"

                        msg.send_messages(nickClient,conferma_message,giocatori)
                    
                        for numero in range(clients):
                            conferma_message = "CentralChangeCard;"+giocatori[numero].get_nick()+";"+card_lasciata+"\r\n"
                            msg.send_messages(giocatori[numero].get_nick(),conferma_message,giocatori)
                        print(f"Invio: {conferma_message}")
                        

                        spostaTurno(1,cambio_verso= False)
                
                    else:
                        #carta non valida
                        message = "errore;"+nickClient+";carta_non_valida"
                        print("non valida")        
                    HandleN_CarteAvversari(nickClient)    
            else:
                altro_message = "err"
                client_socket.sendall(altro_message.encode())
                print(f"Errore durante la gestione del client: {received_message}")

            

    except Exception as e:
        print(f"Errore durante la gestione del client: {e}")

    finally:
        client_socket.close()
def HandleN_CarteAvversari(nickClient):
    for numero in range(clients):
            MessagexGUI="CarteAvversari;AVV_"+nickClient+";"+str(giocatori[numero].get_numero_carte())+"\r\n"
            msg.send_messages(giocatori[numero].get_nick(),MessagexGUI,giocatori)
            print(f"Invio: {MessagexGUI}")
                     
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
    nTurnoTMP = addTurno(1, verso_turno, numeroTurno)
    if nTurnoTMP > clients:
     nTurnoTMP -= clients
    player = giocatori[nTurnoTMP - 1]
    return player

def getGiocatoreCorrente():
    global giocatoreCorrente
    giocatoreCorrente = giocatori[numeroTurno - 1]
    for numero in range(clients):
        MessagexGUI="Turno;"+giocatoreCorrente.get_nick()+"\r\n"
        msg.send_messages(giocatori[numero].get_nick(),MessagexGUI,giocatori)
        print(f"Invio: {MessagexGUI}")

def gestisciSpeciale(card):
    pesca=0
    carte=[]
    msg=""
    carteStr=""
    
    if card[0]=="R":
            spostaTurno(1,cambio_verso= True)
    elif card[0]=="S":
            nickGiocatoreSuccessivo=getGiocatoreSuccessivo().get_nick()
            spostaTurno(2,cambio_verso= False)
            msg = "speciale;"+nickGiocatoreSuccessivo+";salta_turno"
    elif card[0]=="T":
            nickGiocatoreSuccessivo=getGiocatoreSuccessivo().get_nick()
            msg = "speciale;"+nickGiocatoreSuccessivo+";pesca_due"
            pesca=2


    elif card=="FD":
        nickGiocatoreSuccessivo=getGiocatoreSuccessivo().get_nick()
        msg = "speciale;"+nickGiocatoreSuccessivo+";pesca_quattro"
        pesca=4
    
    elif card=="CC":
        spostaTurno(-1,cambio_verso= False)

    for i in range(pesca):
        c = pesca_carta()
        carte.append(c)
        carteStr+="-"+c

    return msg,carte,carteStr
5

        
 
def checkIsValid(card_lasciata):
    speciale = checkCartaSpeciale(card_lasciata)
    correct = False

    last_color = mazzo_tavolo[-1][1]
    last_number = mazzo_tavolo[-1][0]


    if speciale and card_lasciata[0]=="T" or card_lasciata[0]=="S" or card_lasciata[0]=="R":
        color_card_speciale = card_lasciata[0]
        if not color_card_speciale == last_color:
            correct = False
    elif speciale and card_lasciata[0]=="F" or card_lasciata[0]=="C":
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
    print(mazzo_tavolo)
    giocatori[posGiocatore].rimuovi_carta(getCardComplete(carta))

def checkCartaSpeciale(card):
    if card=="FD" or card=="CC" or card[0]=="R" or card[0]=="S" or card[0]=="T": #carte speciali DRAW FOUR (FD) , CHANGE COLOR (CC) , REVERSE (R*) , SKIP (S*) , DRAW TWO (TB)
        return True
    return False

def getCardComplete(cardRec):
        if cardRec=="CC" :
            return "Change Color"
        elif cardRec[0]=="S" :
            return "Skip_"+cardRec[1]
        elif cardRec[0]=="R":
            return "Reverse_"+cardRec[1]
        elif cardRec[0]=="T":
            return "Draw Two_"+cardRec[1]
        elif cardRec=="FD":
            return "Draw Four"
        else:
            return cardRec

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