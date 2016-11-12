from server import DatabaseServer
import threading


class MyThread(threading.Thread):
    def __init__(self, name):
        threading.Thread.__init__(self)
        self.name = name

    def run(self):
        if self.name == 'server':
            service = DatabaseServer()
            print('in server thread')
            service.test_server()
        elif self.name == 'crawler':
            # get the crawler to start in here
            print('in crawler thread')
