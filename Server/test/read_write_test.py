from pymongo import MongoClient
import unittest
import socket
import json


class readWriteTest(unittest.TestCase):
    def test_1(self):
        self.client = MongoClient()

        # create write and send data to DB
        write = {}
        data = {}
        write['message_type'] = 'write'
        write['collection'] = 'food'

        data['name'] = 'syrup'
        data['store'] = 'costco'
        data['price'] = '12.99'
        data['url'] = 'SyrupCostco.com'
        data['image'] = 'SyrupCostco.jpeg'

        write['data'] = data

        self.__send(write)

        # generate read request message
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['syrup']
        read['email'] = ''

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['store'] = []

        read['options'] = options

        # send to server and get response
        # assert the correct message has been written into the DB and
        # that it can be read out correctly
        response = self.__send(read)
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
        write['collection'] = 'food'

        data['name'] = 'syrup'
        data['store'] = 'walmart'
        data['price'] = '34.99'
        data['url'] = 'SyrupWalmart.com'
        data['image'] = 'SyrupWalmart.jpeg'

        write['data'] = data
        self.__send(write)

        # attempt to search for syrups
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['syrup']
        read['email'] = ''

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['store'] = []

        read['options'] = options

        response = self.__send(read)
        self.assertTrue(len(response) == 3)
        self.assertTrue((response['items'][0]['data']['url'] == 'SyrupWalmart.com' and
                        (response['items'][1]['data']['url'] == 'SyrupCostco.com')) or
                        ((response['items'][0]['data']['url'] == 'SyrupCostco.com' and
                        (response['items'][1]['data']['url'] == 'SyrupWalmart.com'))))
        self.assertEqual(response['status'], 'No Email')

        self.client.close()

    def test_3(self):
        self.client = MongoClient()

        # read as a user, and look at recommended items
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['syrup']
        read['email'] = 'mablibsking@hotmail.com'

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['store'] = []

        read['options'] = options

        response = self.__send(read)
        self.assertTrue(len(response) == 3)
        self.assertEqual(response['status'], 'success')

        recommend = {}
        recommend['message_type'] = 'recommend'
        recommend['email'] = read['email']

        response = self.__send(recommend)

        self.assertEqual(response['status'], 'success')
        self.assertEqual(len(response['rec_list']), 1)

        self.client.close()

    def test_4(self):
        self.client = MongoClient()

        # read as a user, and look at recommended items
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['syrup']
        read['email'] = ''

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['store'] = ['walmart']

        read['options'] = options

        response = self.__send(read)
        self.assertTrue(len(response) == 3)
        self.assertEqual(response['status'], 'No Email')

        ret_list = response['items']

        for item in ret_list:
            self.assertEqual(item['data']['store'], options['store'][0]
                             # or item['data']['store'], options['store'][1]
                            )

        self.client.close()

    def test_5(self):
        self.client = MongoClient()

        # read as a user, and look at recommended items
        read = {}
        read['message_type'] = 'read'
        read['items'] = ['syrup']
        read['email'] = ''

        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        options['store'] = ''
        options['range_min'] = '1.00'
        options['range_max'] = '34.00'

        read['options'] = options

        response = self.__send(read)
        self.assertTrue(len(response) == 3)
        self.assertEqual(response['status'], 'No Email')

        ret_list = response['items']

        for item in ret_list:
            self.assertTrue(options['range_min'] <= item['data']['price'] <= options['range_max'])

        print(response['items'])
        self.client.close()

    def __send(self, data):
        sock = socket.socket()
        host = socket.gethostbyname(socket.gethostname())
        port = 6969
        json_data = json.dumps(data)

        sock.connect((host, port))
        sock.send(json_data.encode())
        json_data = sock.recv(1024)
        response = json.loads(json_data.decode())
        sock.close()
        return response

if __name__ == "__main__":
    unittest.main()
