class giocatore:
    def __init__(self, nick, numero_carte,c_socket):
        self.nick = nick
        self.numero_carte = numero_carte
        self.c_socket=c_socket

    def get_nick(self):
        return self.nick

    def get_numero_carte(self):
        return self.numero_carte

    def get_c_socket(self):
        return self.c_socket
    

    def imposta_numero_carte(self, nuovo_numero_carte):
        self.numero_carte = nuovo_numero_carte


""" # Esempio di utilizzo della classe Giocatore
giocatore1 = Giocatore("Alice", 10)
print(f"Nick: {giocatore1.ottieni_nick()}, Numero di carte: {giocatore1.ottieni_numero_carte()}")

# Modifica il numero di carte del giocatore
giocatore1.imposta_numero_carte(8)
print(f"Nuovo numero di carte: {giocatore1.ottieni_numero_carte()}")
 """