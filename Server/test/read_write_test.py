from pymongo import MongoClient
from src.crawl_lib import *
import unittest
import socket


class readWriteTest(unittest.TestCase):
    def test_1(self):
        self.client = MongoClient()

        # create write and send data to DB
        write = {}
        write['name'] = 'syrup'
        write['store'] = 'costco'
        write['price'] = '12.99'
        write['collection'] = 'food'
        write['url'] = 'SyrupCostco.com'
        write['image'] = 'SyrupCostco.jpeg'
        send_to_db(write['collection'], write)

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
        self.assertEqual(response['items'][0]['data']['name'], write['name'])
        self.assertEqual(response['items'][0]['data']['price'], write['price'])
        self.assertEqual(response['items'][0]['data']['store'], write['store'])
        self.assertEqual(response['items'][0]['data']['url'], write['url'])
        self.assertEqual(response['items'][0]['collection'], write['collection'])
        self.client.close()

    def test_2(self):
        self.client = MongoClient()

        # write another syrup item into the DB
        write = {}
        write['name'] = 'syrup'
        write['store'] = 'walmart'
        write['price'] = '34.99'
        write['collection'] = 'food'
        write['url'] = 'SyrupWalmart.com'
        write['image'] = 'SyrupWalmart.jpeg'

        send_to_db(write['collection'], write)

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
        json_response = sock.recv(1024).decode()
        response = json.loads(json_response)
        sock.close()
        return response

if __name__ == "__main__":
    unittest.main()
