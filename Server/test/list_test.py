import socket
import json
import unittest


class ListTest(unittest.TestCase):
    @staticmethod
    def __send(data):
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

    def test_2(self):
        data = {}
        data['message_type'] = 'add_list'
        data['list_name'] = 'list_1'
        data['email'] = 'mablibsking@hotmail.com'

        data_list = []
        data_list.append({'name': 'syrup',
                          'store': 'costco',
                          'price': '12.99'})
        data_list.append({'name': 'coffee',
                          'store': 'walmart',
                          'price': '5.99'})
        data['list'] = data_list
        response = self.__send(data)
        self.assertEqual('success', response['status'])

        del data['list']
        data['message_type'] = 'retrieve_list'
        response = self.__send(data)
        self.assertEqual('success', response['status'])
        self.assertListEqual(data_list, response['list'])

    def test_3(self):
        data = {}
        data['message_type'] = 'add_list'
        data['list_name'] = 'list_2'
        data['email'] = 'mablibsking@hotmail.com'

        data_list = []
        data_list.append({'name': 'cereal',
                          'store': 'costco',
                          'price': '4.55'})
        data_list.append({'name': 'shoes',
                          'store': 'walmart',
                          'price': '5.99'})
        data['list'] = data_list
        response = self.__send(data)
        self.assertEqual('success', response['status'])

        del data['list']
        data['message_type'] = 'retrieve_list'
        response = self.__send(data)
        self.assertEqual('success', response['status'])
        self.assertListEqual(data_list, response['list'])

    def test_4(self):
        data = {}
        data['message_type'] = 'delete_list'
        data['list_name'] = 'list_1'
        data['email'] = 'mablibsking@hotmail.com'

        response = self.__send(data)
        self.assertEqual(response['status'], 'success')

        data['message_type'] = 'retrieve_list'
        response = self.__send(data)
        self.assertEqual(response['status'], 'failed')

    def __delete_list_3(self):
        data = {}
        data['message_type'] = 'delete_list'
        data['list_name'] = 'list_3'
        data['email'] = 'mablibsking@hotmail.com'

        response = self.__send(data)
        self.assertEqual(response['status'], 'failed')


if __name__ == '__main__':
    unittest.main()
