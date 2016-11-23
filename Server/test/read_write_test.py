from pymongo import MongoClient
import unittest
import socket
import json
import time

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
        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
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
        options = {}
        options['price'] = 'min'
        options['num'] = '-1'
        read['options'] = options

        response = self.__send(read)
        self.assertTrue(len(response) == 2)
        self.assertTrue((response['items'][0]['data']['url'] == 'SyrupWalmart.com' and
                        (response['items'][1]['data']['url'] == 'SyrupCostco.com')) or
                        ((response['items'][0]['data']['url'] == 'SyrupCostco.com' and
                        (response['items'][1]['data']['url'] == 'SyrupWalmart.com'))))

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
