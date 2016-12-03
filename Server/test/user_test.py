# Takes in Crawler Data and sends it to server
# Takes in User Request and sends back item
import socket
import json
import unittest
import ssl


class userTest(unittest.TestCase):
    @staticmethod
    def _send(data):
        sock = socket.socket()
        host = socket.gethostbyname(socket.gethostname())
        port = 6969

        context = ssl.SSLContext(ssl.PROTOCOL_TLSv1_2)
        context.verify_mode = ssl.CERT_REQUIRED
        context.check_hostname = True
        context.load_verify_locations('/home/ryangroup/cpen_321/Server/src/ca.crt')
        connection = context.wrap_socket(sock, server_hostname='checkedout')
        json_data = json.dumps(data)

        connection.connect((host, port))
        connection.send(json_data.encode())
        json_response = connection.recv(1024).decode()
        response = json.loads(json_response)
        connection.close()
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

        data['message_type'] = 'acc_delete'

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

        response = self._send(data)
        self.assertEqual('success', response['status'])
        
        data['message_type'] = 'acc_delete'

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

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['old_password'] = 'Not_the_right_password'
        response = self._send(data)
        self.assertNotEqual('success', response['status'])
        
        data['message_type'] = 'acc_delete'

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_login'

        response = self._send(data)
        self.assertEqual('DNE', response['status'])

    def test_7(self):
        data = {}
        data['email'] = 'dioz.rl@gmail.com'
        data['password'] = 'Test_account'
        data['name'] = 'Ryan Liu'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_create'

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_login'
        response = self._send(data)
        self.assertNotEqual('success', response['status'])
        self.assertEqual('Not Verified', response['status'])

        verify = {}
        verify['email'] = 'dioz.rl@gmail.com'
        verify['message_type'] = 'acc_verify'
        verify['verify_num'] = str(111111)

        response = self._send(verify)
        self.assertEqual('failed', response['status'])

        verify['verify_num'] = str(666666)
        response = self._send(verify)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_login'

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_delete'

        response = self._send(data)
        self.assertEqual('success', response['status'])

        data['message_type'] = 'acc_login'

        response = self._send(data)
        self.assertEqual('DNE', response['status'])
        
if __name__ == "__main__":
    unittest.main()

