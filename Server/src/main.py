from thread_factory import ThreadFactory

"""
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
"""


def main():
    thread_factory = ThreadFactory()
    handler1 = thread_factory.create_thread('req_handle')
    handler2 = thread_factory.create_thread('req_handle')
    handler3 = thread_factory.create_thread('req_handle')
    server_thread = thread_factory.create_thread('server')

    handler1.start()
    handler2.start()
    handler3.start()
    server_thread.start()
    
if __name__ == "__main__":
    main()
