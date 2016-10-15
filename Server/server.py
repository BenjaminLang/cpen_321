import socket

import pymongo
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
            # print(json_data['data']['name'])
            # print(json_data['data']['price'])
            # print(json_data['data']['store'])
            # print(json_data['message_type'])
            # print(json_data['collection'])

            que_type = json_data['message_type']

            if que_type == 'write':
                collection = json_data['collection']
                db[collection].save(json_data)

            if que_type == 'read':
                # Sort by price and return first
                # Assumptions:
                # Price is a valid field in the document
                collection = json_data['collection']
                min_price = db[collection].find_one({"data.price": {"$exists": True}}, sort=[("data.price", pymongo.ASCENDING)])
                print(min_price)
            connection.close()