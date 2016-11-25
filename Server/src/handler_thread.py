import threading

from I_thread import IThread
from request_handler import RequestHandler


class HandlerThread(IThread, threading.Thread):
    def __init__(self, queue):
        threading.Thread.__init__(self)
        self._queue = queue

    def run(self):
        service = RequestHandler(self._queue)
        print('running handler')
        service.handle_request()
