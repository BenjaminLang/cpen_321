import socket

class query():
    def __init__(self, ):
        self.sock = socket.socket()
        self.host = socket.gethostname()
        self.port = 6969

    def start_query(self):
        self.sock.connect((self.host, self.port))
        self.sock.send()
        print('client recieved: ',  repr(self.sock.recv(1024)))
        self.sock.close()
