import socket
from configparser import ConfigParser

class database_server():

    def setup_config(self, filename='config.ini', section='mysql'):
        parser = ConfigParser()
        parser.read(filename)

        db = {}
        if parser.has_section(section):
            items = parser.items(section)
            for item in items:
                db[item[0]] = item[1]
        return db

    def test_server(self):
        server_socket = socket.socket()
        host = socket.gethostname()
        port = 6969
        server_socket.bind((host, port))
        server_socket.listen(10)

        while True:
            connection, addr = server_socket.accept()
            connection.close()
            print('got connection')

