# Takes in Crawler Data and sends it to server
# Takes in User Request and sends back item
import socket
from pymongo import MongoClient
from crawl_lib import *

class query:
    def __init__(self):
        self.client = MongoClient()
        self.client = MongoClient()
        self.sock = socket.socket()
        self.host = socket.gethostbyname(socket.gethostname())
        self.port = 6969

    def start_query(self):
        data = {}
        data['email'] = 'mablibsking@hotmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Ryan Liu'
        data['list'] = []
        data['message_type'] = 'acc_create'

        json_data = json.dumps(data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_data.encode())
        print(self.sock.recv(1024).decode())
        self.sock.close()

    def start_query_2(self):
        data = {}
        data['email'] = 'leobelanger1996@gmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Leo Belanger'
        data['list'] = []
        data['message_type'] = 'acc_create'

        json_data = json.dumps(data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_data.encode())
        print(self.sock.recv(1024).decode())
        self.sock.close()

    def start_query_3(self):
        data = {}
        data['email'] = 'mablibsking@hotmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Ryan Liu'
        data['list'] = []
        data['message_type'] = 'acc_delete'

        json_data = json.dumps(data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_data.encode())
        print(self.sock.recv(1024).decode())
        self.sock.close()

    def start_query_4(self):
        data = {}
        data['email'] = 'leobelanger1996@gmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Leo Belanger'
        data['list'] = []
        data['message_type'] = 'log_in'

        json_data = json.dumps(data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_data.encode())
        print(self.sock.recv(1024).decode())
        self.sock.close()

    def start_query_5(self):
        data = {}
        data['email'] = 'biggusdikusmcgee@gmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Benjamin Lang'
        data['list'] = []
        data['message_type'] = 'acc_create'

        json_data = json.dumps(data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_data.encode())
        print(self.sock.recv(1024).decode())
        self.sock.close()

    def start_query_6(self):
        data = {}
        data['email'] = 'biggusdikusmcgee@gmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Benjamin Lang'
        data['list'] = []
        data['message_type'] = 'acc_login'

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
    v = query()
    w = query()
    a = query()

    # x.start_query()
    y.start_query_2()
    # z.start_query_3()
    u.start_query_4()
    # v.start_query_5()
    # w.start_query_6()
    a.start_query_2()
