import socket
import json
import subprocess
from pymongo import MongoClient

from RequestHandler import RequestHandler

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

        requestHandler = RequestHandler(db)

        while True:
            connection, addr = server_socket.accept()
            data = connection.recv(1024).decode()
            json_data = json.loads(data)

            response = RequestHandler.handle_request(json_data['message_type'], json_data)
            json_response = json.dumps(response)
            connection.send(json_response.encode())
            connection.close()
