import queue
import threading

from request_handler import RequestHandler

from server import DatabaseServer


class MyThread(threading.Thread):
    def __init__(self, name, q):
        threading.Thread.__init__(self)
        self.name = name
        self.__queue = q

    def run(self):
        if self.name == 'server':
            service = DatabaseServer(self.__queue)
            print('in server thread')
            service.test_server()
        elif self.name == 'crawler':
            # get the crawler to start in here
            print('in crawler thread')
        elif self.name == 'req_handle':
            print('starting handler')
            handler = RequestHandler(self.__queue)
            handler.handle_request()




def main():
    q = queue.Queue()
    q.maxsize = 50
    server_thread = MyThread('server', q).start()
    req_handle_thread_1 = MyThread('req_handle', q).start()
    req_handle_thread_2 = MyThread('req_handle', q).start()
    req_handle_thread_3 = MyThread('req_handle', q).start()




if __name__ == "__main__":
    main()
