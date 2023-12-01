import threading
import time

class MyTimerThread(threading.Thread):
    """
    Classe per la creazione di un timer in un thread separato.

    Args:
    - interval (float): Intervallo di tempo in secondi tra le esecuzioni della funzione target.
    - target_function (function): La funzione da eseguire quando il timer scatta.

    """

    def __init__(self, interval, target_function):
        """
        Inizializza un nuovo oggetto MyTimerThread.

        Args:
        - interval (float): Intervallo di tempo in secondi tra le esecuzioni della funzione target.
        - target_function (function): La funzione da eseguire quando il timer scatta.

        """
        super().__init__()
        self.interval = interval
        self.target_function = target_function
        self._stop_event = threading.Event()

    def stop(self):
        """ 
        Interrompe il timer.
        """
        self._stop_event.set()

    def run(self):
        """
        Avvia l'esecuzione del timer.
        """
        while not self._stop_event.is_set():
            time.sleep(self.interval)
            self.target_function()

# Funzione da eseguire quando il timer scatta
