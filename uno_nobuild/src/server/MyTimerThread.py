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