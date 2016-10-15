from server import database_server
import threading

class myThread(threading.Thread):
    def __init__(self, name):
        threading.Thread.__init__(self)
        self.name = name

    def run(self):
        if self.name == 'server':
            service = database_server()
            print('in server thread')
            service.test_server()
        elif self.name == 'crawler':
            # get the crawler to start in here
            print('in crawler thread')