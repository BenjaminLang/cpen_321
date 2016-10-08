import socket

def query():
    sock = socket.socket()
    host = socket.gethostname()
    port = 6969

    sock.connect((host, port))
    sock.send(b"SELECT * FROM books LIMIT 3")
    print('client recieved: ',  repr(sock.recv(1024)))
    sock.close()


if __name__ == '__main__':
    query()