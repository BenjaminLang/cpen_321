# Takes in Crawler Data and sends it to server
# Takes in User Request and sends back item
import socket
from pymongo import MongoClient
import json
import unittest

class userTest(unittest.TestCase):
    def __send(self, data):
        sock = socket.socket()
        host = socket.gethostbyname(socket.gethostname())
        port = 6969
        json_data = json.dumps(data)

        sock.connect((host, port))
        sock.send(json_data.encode())
        json_response = sock.recv(1024).decode()
        response = json.loads(json_response)
        sock.close()
        return response

    def test_1(self):
        data = {}
        data['email'] = 'mablibsking@hotmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Ryan Liu'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_create'

        response = self.__send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'log_in'

        response = self.__send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_delete'

        response = self.__send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'log_in'

        response = self.__send(data)
        self.assertEqual('failed', response['status'])

    def test_2(self):
        data = {}
        data['email'] = 'leobelanger1996@gmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Leo Belanger'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_create'

        response = self.__send(data)
        self.assertEqual('success', response['status'])

    def test_3(self):
        data = {}
        data['email'] = 'leobelanger1996@gmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Leo Belanger'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'log_in'

        json_data = json.dumps(data)

        response = self.__send(data)
        self.assertEqual('success', response['status'])

    def test_4(self):
        data = {}
        data['email'] = 'benjaminlang@hotmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Benjamin Lang'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_create'

        json_data = json.dumps(data)

        response = self.__send(data)
        self.assertEqual('success', response['status'])

    def test_5(self):
        data = {}
        data['email'] = 'benjaminlang@hotmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Benjamin Lang'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_login'

        json_data = json.dumps(data)

        response = self.__send(data)
        self.assertEqual('success', response['status'])

    """
    def start_query_7(self):
        data = {}
        data['email'] = 'benjaminlang@hotmail.com'
        data['password'] = 'New_Password'
        data['name'] = 'Benjamin Lang'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'update_acc'

        json_data = json.dumps(data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_data.encode())
        print(self.sock.recv(1024).decode())
        self.sock.close()
    """

if __name__ == "__main__":
    unittest.main()
