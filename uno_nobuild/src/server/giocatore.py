class giocatore:
    def __init__(self, nick, numero_carte, c_socket):
        """
        Inizializza un oggetto Giocatore con un nome utente (nick), 
        un numero di carte e un socket (c_socket).
        """
        self.nick = nick
        self.numero_carte = numero_carte
        self.c_socket = c_socket
        self.mazzo = []

    def get_nick(self):
        """
        Restituisce il nome utente (nick) del giocatore.
        """
        return self.nick

    def get_numero_carte(self):
        """
        Restituisce il numero di carte attualmente possedute dal giocatore.
        """
        return self.numero_carte

    def get_c_socket(self):
        """
        Restituisce il socket associato al giocatore.
        """
        return self.c_socket

    def aggiungi_carta(self, card):
        """
        Aggiunge una carta al mazzo del giocatore e aggiorna il numero di carte.
        """
        self.mazzo.append(card)
        self.imposta_numero_carte(len(self.mazzo))

    def rimuovi_carta(self, card):
        """
        Rimuove una carta dal mazzo del giocatore e aggiorna il numero di carte.
        """
        self.mazzo.remove(card)
        self.imposta_numero_carte(len(self.mazzo))

    def imposta_mazzo(self, cards):
        """
        Imposta il mazzo del giocatore con una lista di carte e aggiorna il numero di carte.
        """
        self.mazzo = cards
        self.imposta_numero_carte(len(self.mazzo))

    def imposta_numero_carte(self, nuovo_numero_carte):
        """
        Imposta il numero di carte del giocatore.
        """
        self.numero_carte = nuovo_numero_carte

    def getMazzoToString(self):
        """
        Restituisce una stringa rappresentante il mazzo di carte del giocatore.
        """
        output = ""
        for card in self.mazzo:
            output += card + ","
        return output

    def getMazzo(self):
        """
        Restituisce il mazzo di carte del giocatore.
        """
        return self.mazzo

""" # Esempio di utilizzo della classe Giocatore
giocatore1 = Giocatore("Alice", 10)
print(f"Nick: {giocatore1.ottieni_nick()}, Numero di carte: {giocatore1.ottieni_numero_carte()}")

# Modifica il numero di carte del giocatore
giocatore1.imposta_numero_carte(8)
print(f"Nuovo numero di carte: {giocatore1.ottieni_numero_carte()}")
 """