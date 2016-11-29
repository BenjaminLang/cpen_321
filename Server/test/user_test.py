# Takes in Crawler Data and sends it to server
# Takes in User Request and sends back item
import socket
import json
import unittest


class userTest(unittest.TestCase):
    def _send(self, data):
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

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_login'

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_del'

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_login'

        response = self._send(data)
        self.assertEqual('DNE', response['status'])

    def test_2(self):
        data = {}
        data['email'] = 'leobelanger1996@gmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Leo Belanger'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_create'

        response = self._send(data)
        self.assertEqual('success', response['status'])

    def test_3(self):
        data = {}
        data['email'] = 'leobelanger1996@gmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Leo Belanger'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_login'

        json_data = json.dumps(data)

        response = self._send(data)
        self.assertEqual('success', response['status'])
        
        data['message_type'] = 'acc_del'

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_login'

        response = self._send(data)
        self.assertEqual('DNE', response['status'])

    def test_4(self):
        data = {}
        data['email'] = 'benjaminlang@hotmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Benjamin Lang'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_create'

        json_data = json.dumps(data)

        response = self._send(data)
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

        response = self._send(data)
        self.assertEqual('success', response['status'])

    def test_6(self):
        data = {}
        data['email'] = 'benjaminlang@hotmail.com'
        data['password'] = 'New_Password'
        data['old_password'] = 'UBC_student_2016'
        data['name'] = 'Benjamin Lang'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_update'

        json_data = json.dumps(data)

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['old_password'] = 'Not_the_right_password'
        response = self._send(data)
        self.assertNotEqual('success', response['status'])
        
        data['message_type'] = 'acc_del'

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_login'

        response = self._send(data)
        self.assertEqual('DNE', response['status'])
        
if __name__ == "__main__":
    unittest.main()

