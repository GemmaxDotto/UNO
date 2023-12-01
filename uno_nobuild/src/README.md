# PROGETTO UNO DI Provenzi & Dotto

## Server

Il server è implementato in Python. Gestisce la logica di gioco e mantiene lo stato del gioco. Comunica con i client tramite un protocollo basato su socket (TCP).

### Installazione e Utilizzo

1. Naviga nella directory del server.
2. Installa le dipendenze richieste di Python.
3. Esegui lo script del server utilizzando Python.

## Client

Il client è un'interfaccia utente grafica (GUI) implementata in Java. Comunica con il server (TCP) per inviare azioni del giocatore e ricevere aggiornamenti di gioco.

### Installazione e Utilizzo

1. Naviga nella directory del client.
2. Installa le dipendenze richieste di Java.
3. Esegui la GUI utilizzando Java.

## Protocollo TCP

Il Transmission Control Protocol (TCP) è un protocollo di comunicazione utilizzato in questo progetto per inviare pacchetti di dati sulla rete. È una consegna affidabile, ordinata e controllata degli errori di un flusso di byte tra applicazioni in esecuzione su host che comunicano tramite una rete IP.

Nel contesto del progetto Uno:

- Il server utilizza TCP per inviare aggiornamenti di gioco ai client e ricevere azioni dei giocatori.
- Il client utilizza TCP per inviare azioni dei giocatori al server e ricevere aggiornamenti di gioco.

TCP garantisce che tutti gli aggiornamenti di gioco e le azioni dei giocatori vengano consegnati nell'ordine corretto e senza errori, il che è fondamentale per mantenere lo stato del gioco e fornire un'esperienza di gioco fluida.

## Logica di Gioco

La logica di gioco per il progetto Uno è implementata nel server. Segue le regole standard di Uno con alcune caratteristiche aggiuntive:

- Il gioco inizia con ogni giocatore che riceve 7 carte.
- I giocatori si alternano in senso orario, giocando una carta che corrisponde al colore o al numero della carta superiore sul mazzo di scarti.
- Se un giocatore non può giocare una carta, deve pescare una carta dal mazzo.
- Le carte speciali come Salta, Inverti e Pesca Due introducono meccaniche di gioco aggiuntive.
- Il gioco termina quando un giocatore non ha più carte.

Il server gestisce lo stato del gioco, inclusi il mazzo di scarti e la direzione corrente del gioco. Si occupa anche di far rispettare le regole del gioco, come abbinare il colore o il numero della carta superiore sul mazzo di scarti e gestire le carte speciali.
