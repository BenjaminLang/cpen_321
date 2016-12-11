import socket
import json
import unittest
import ssl


class ListTest(unittest.TestCase):
    @staticmethod
    def _send(data):
        sock = socket.socket()
        host = socket.gethostbyname(socket.gethostname())
        port = 6969
        
        context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
        context.verify_mode = ssl.CERT_REQUIRED
        context.check_hostname = True
        context.load_verify_locations('/home/ryangroup/cpen_321/Server/src/ca.crt')
        connection = context.wrap_socket(sock,server_hostname='checkedout')
        json_data = json.dumps(data)

        connection.connect((host, port))
        connection.send(json_data.encode())
        json_response = connection.recv(1024).decode()
        response = json.loads(json_response)
        connection.close()
        return response

    def test_1(self):
        data = {}
        data['email'] = 'mablibsking2@hotmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Ryan Liu'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_create'

        response = self._send(data)
        self.assertEqual('success', response['status'])

    def test_2(self):
        data = {}
        data['message_type'] = 'add_list'
        data['list_name'] = 'list_1'
        data['email'] = 'mablibsking2@hotmail.com'

        data_list = []
        data_list.append({'name': 'syrup',
                          'store': 'costco',
                          'price': '12.99'})
        data_list.append({'name': 'coffee',
                          'store': 'walmart',
                          'price': '5.99'})
        data['list'] = data_list
        response = self._send(data)
        self.assertEqual('success', response['status'])

        del data['list']
        data['message_type'] = 'get_list'
        response = self._send(data)
        self.assertEqual('success', response['status'])
        self.assertListEqual(data_list, response['list'])

    def test_3(self):
        data = {}
        data['message_type'] = 'add_list'
        data['list_name'] = 'list_2'
        data['email'] = 'mablibsking2@hotmail.com'

        data_list = []
        data_list.append({'name': 'cereal',
                          'store': 'costco',
                          'price': '4.55'})
        data_list.append({'name': 'shoes',
                          'store': 'walmart',
                          'price': '5.99'})
        data['list'] = data_list
        response = self._send(data)
        self.assertEqual('success', response['status'])

        del data['list']
        data['message_type'] = 'get_list'
        response = self._send(data)
        self.assertEqual('success', response['status'])
        self.assertListEqual(data_list, response['list'])

    def test_4(self):
        data = {}
        data['message_type'] = 'delete_list'
        data['list_name'] = 'list_1'
        data['email'] = 'mablibsking2@hotmail.com'

        response = self._send(data)
        self.assertEqual(response['status'], 'success')

        data['message_type'] = 'get_list'
        response = self._send(data)
        self.assertEqual(response['status'], 'failed')

    def test_5(self):
        data = {}
        data['message_type'] = 'delete_list'
        data['list_name'] = 'list_3'
        data['email'] = 'mablibsking2@hotmail.com'

        response = self._send(data)
        self.assertEqual(response['status'], 'failed')

    def test_6(self):
        data = {}
        data['email'] = 'mablibsking2@hotmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Ryan Liu'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_delete'

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_login'

        response = self._send(data)
        self.assertEqual('DNE', response['status'])

if __name__ == '__main__':
    unittest.main()
