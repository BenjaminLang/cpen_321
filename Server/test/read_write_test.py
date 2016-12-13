from pymongo import MongoClient
import unittest
import socket
import json
import ssl

import sys
sys.path.append('/home/ryangroup/cpen_321/Server/src')
from request_handler import RequestHandler

url1 = 'https://www.facebook.com/mango475?fref=ts'
img1 = 'https://scontent-sea1-1.xx.fbcdn.net/v/t1.0-9/576480_10151292510184882_466120745_n.jpg?oh=aae37bb574a6be178491d9100d3a3e7a&oe=58EB7F5C'
url2 = 'https://www.facebook.com/shaurya.sharad?fref=ts'
img2 = 'https://scontent-sea1-1.xx.fbcdn.net/v/t1.0-9/1422608_945426875497408_1455403238629541686_n.jpg?oh=8bb1d42baa526bd2432074e7f62b0fd9&oe=58AF1F09'

class readWriteTest(unittest.TestCase):
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
        #print(json_response)
        response = json.loads(json_response)
        connection.close()
        return response

    def test_1(self):
        self.client = MongoClient()
        context = ssl.create_default_context()
        # create write and send data to DB
        write = {}
        data = {}
        write['message_type'] = 'write'
        write['collection'] = 'animalfeet'

        data['name'] = 'RyanPigFeet'
        data['store'] = 'Costco'
        data['price'] = '12.99'
        data['url'] = url1
        data['image'] = img1

        write['data'] = data
        json_data = json.dumps(write, indent=2)
        service = RequestHandler()
        service.handle_crawler(json_data)

        # generate read request message
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['RyanPigFeet']
        read['email'] = ''

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['stores'] = []
        options['range_max'] = ''
        options['range_min'] = ''

        read['options'] = options

        # send to server and get response
        # assert the correct message has been written into the DB and
        # that it can be read out correctly
        response = self._send(read)
        self.assertEqual(response['items'][0]['data']['name'], write['data']['name'])
        self.assertEqual(response['items'][0]['data']['price'], write['data']['price'])
        self.assertEqual(response['items'][0]['data']['store'], write['data']['store'])
        self.assertEqual(response['items'][0]['data']['url'], write['data']['url'])
        self.assertEqual(response['items'][0]['collection'], write['collection'])
        self.assertEqual(response['status'], 'No Email')
        self.client.close()

    def test_2(self):
        self.client = MongoClient()

        # write another syrup item into the DB
        write = {}
        data = {}
        write['message_type'] = 'write'
        write['collection'] = 'animalfeet'

        data['name'] = 'RyanPigFeet'
        data['store'] = 'Walmart'
        data['price'] = '34.99'
        data['url'] = url2
        data['image'] = img2

        write['data'] = data
        json_data = json.dumps(write, indent=2)
        service = RequestHandler()
        service.handle_crawler(json_data)

        # attempt to search for syrups
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['RyanPigFeet']
        read['email'] = ''

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['stores'] = []
        options['range_max'] = ''
        options['range_min'] = ''

        read['options'] = options

        response = self._send(read)
        self.assertTrue(len(response) == 3)
        self.assertTrue((response['items'][0]['data']['url'] == url2 and
                        (response['items'][1]['data']['url'] == url1)) or
                        ((response['items'][0]['data']['url'] == url1 and
                        (response['items'][1]['data']['url'] == url2))))
        self.assertEqual(response['status'], 'No Email')

        self.client.close()

    '''
    def test_3(self):
        self.client = MongoClient()

        # read as a user, and look at recommended items
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['RyanPigFeet']
        read['email'] = 'mablibsking2@hotmail.com'

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['stores'] = []
        options['range_max'] = ''
        options['range_min'] = ''

        read['options'] = options

        response = self._send(read)
        self.assertTrue(len(response) == 3)
        self.assertEqual(response['status'], 'success')

        recommend = {}
        recommend['message_type'] = 'recommend'
        recommend['email'] = read['email']

        response = self._send(recommend)

        self.assertEqual(response['status'], 'success')
        self.assertEqual(len(response['rec_list']), 1)

        data = {}
        data['email'] = read['email']
        data['message_type'] = 'acc_delete'
        
        response = self._send(data)
        self.assertEqual('success', response['status'])

        self.client.close()
    '''

    def test_4(self):
        self.client = MongoClient()

        # read as a user, and look at recommended items
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['RyanPigFeet']
        read['email'] = ''

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['stores'] = ['Walmart']
        options['range_max'] = ''
        options['range_min'] = ''

        read['options'] = options

        response = self._send(read)
        self.assertTrue(len(response) == 3)
        self.assertEqual(response['status'], 'No Email')

        ret_list = response['items']

        for item in ret_list:
            self.assertEqual(item['data']['store'], options['stores'][0]
                             # or item['data']['store'], options['stores'][1]
                            )

        self.client.close()

    def test_5(self):
        self.client = MongoClient()

        # read as a user, and look at recommended items
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['RyanPigFeet']
        read['email'] = ''

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['stores'] = []
        options['range_min'] = '1.00'
        options['range_max'] = '34.00'

        read['options'] = options

        response = self._send(read)
        self.assertTrue(len(response) == 3)
        self.assertEqual(response['status'], 'No Email')

        ret_list = response['items']

        for item in ret_list:
            self.assertTrue(options['range_min'] <= item['data']['price'] <= options['range_max'])

        #print(response['items'])
        self.client.close()

if __name__ == "__main__":
    unittest.main()
