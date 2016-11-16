# Takes in Crawler Data and sends it to server
# Takes in User Request and sends back item
import socket
from pymongo import MongoClient
from crawl_lib import *


class query:
    def __init__(self):
        self.client = MongoClient()
        self.sock = socket.socket()
        self.host = socket.gethostbyname(socket.gethostname())
        self.port = 6969

    def start_query(self):
        data = {}
        data['message_type'] = 'read'
        data['items'] = ['syrup']
        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        data['options'] = options

        json_data = json.dumps(data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_data.encode())
        print(self.sock.recv(1024).decode())
        self.sock.close()

    def start_query_2(self):
        data = {}
        data['message_type'] = 'read'
        data['items'] = ['samsung']
        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        data['options'] = options

        json_data = json.dumps(data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_data.encode())
        print(self.sock.recv(1024).decode())
        self.sock.close()

if __name__ == "__main__":
    x = query()
    y = query()
    z = query()
    u = query()

    x.start_query()
    y.start_query_2()
    z.start_query()
    u.start_query_2()
