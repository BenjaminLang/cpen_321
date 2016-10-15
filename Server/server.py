import socket
from pymongo import MongoClient
import pickle
from query import query

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
            data = connection.recv(1024)
            request = pickle.load(data)
            print(request.type)
            connection.send(b'done')
            connection.close()

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

        """cursur = db.oranges.find()
        for document in cursur:
            print(document)

        print('inputed data')"""
	"""	
        # getting input data from Crawler
        name, database_input = spider()

        print(name)
        print(database_input)

        # Save vs insert_one
        # Save updates and insert adds another one
        db.name.save(database_input)
"""
"""# getting input data from Crawler
name, database_input = spider()

print(name)
print(database_input)

db.name.insert_one(database_input)

<<<<<<< HEAD
# getting min value
user_req_name = query()

# Sort by price and return first
# Assumptions:
# Price is a valid field in the document
min_price = user_req_name.find_one({"price" : {"$exists": True}},
                                   sort=[("price", 1)])["price"]
print(min_price)
"""
