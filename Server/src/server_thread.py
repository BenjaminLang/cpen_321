import threading

from I_thread import IThread
from server import DatabaseServer


class ServerThread(IThread, threading.Thread):
    def __init__(self, queue):
        threading.Thread.__init__(self)
        self._queue = queue
        
    def run(self):
        service = DatabaseServer(self._queue)
        print('starting server')
        service.test_server()
