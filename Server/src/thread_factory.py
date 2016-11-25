import queue

from src.I_thread_factory import IThreadFactory
from src.server_thread import ServerThread
from src.handler_thread import HandlerThread


class ThreadFactory(IThreadFactory):
    def __init__(self):
        self._queue = queue.Queue()
        self._queue.maxsize = 50    

    def create_thread(self, thread_type):
        thread = None
        if thread_type == 'server':
            thread = ServerThread(self._queue)
        elif thread_type == 'req_handle':
            thread = HandlerThread(self._queue)
        return thread
