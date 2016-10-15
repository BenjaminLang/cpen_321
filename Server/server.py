import socket
from pymongo import MongoClient
import json

class database_server:
    def test_server(self):
        # set up server socket
        server_socket = socket.socket()
        host = socket.gethostname()
        port = 6969
        server_socket.bind((host, port))
        server_socket.listen(10)

        # connect to mongoDB
        client = MongoClient()
        db = client.test

        while True:
            connection, addr = server_socket.accept()
            data = connection.recv(1024).decode()
            json_data = json.loads(data)
            print(json_data['data']['name'])
            print(json_data['data']['price'])
            print(json_data['data']['store'])
            connection.close()
