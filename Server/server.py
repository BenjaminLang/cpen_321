import socket
from pymongo import MongoClient

class database_server():

    def test_server(self):
        # set up server socket
        server_socket = socket.socket()
        host = socket.gethostname()
        port = 6969
        server_socket.bind((host, port))
        server_socket.listen(10)

        print('trying to connect to mongo')

        # connect to mongoDB
        client = MongoClient()
        db = client.test

        # testing the input

        """firstInput = db.oranges.insert_one(
            {
                "data" : {
                    "name" : "mandarin oranges",
                    "store" : "save on foods",
                    "price" : "1.59",
                }
            }
        )"""

        # testing the query

        cursur = db.oranges.find()
        for document in cursur:
            print(document)

        # print('inputed data')

        while True:
            connection, addr = server_socket.accept()
            connection.recv(1024)
            connection.close()
            print('got connection')




