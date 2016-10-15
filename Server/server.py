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
            print(json_data['name'])
            print(json_data['price'])
            print(json_data['store'])
            connection.close()

            connection_2, addr_2 = server_socket.accept()
            data_2 = connection_2.recv(1024).decode()
            json_data_2 = json.loads(data_2)
            print(json_data_2['name'])
            print(json_data_2['price'])
            print(json_data_2['store'])
            connection_2.close()