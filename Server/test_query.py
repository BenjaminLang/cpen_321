# Take in Crawler Data and sends it to server
# Take in User Request and sends back item
import socket
import json


class query:
    def __init__(self):
        self.sock = socket.socket()
        self.host = socket.gethostname()
        self.port = 6969
        self.message = {}

    def start_query(self):
        data = {}
        self.message['message_type'] = 'write'
        self.message['collection'] = 'syrup'
        data['name'] = 'syrup'
        data['store'] = 'costco'
        data['price'] = '69.69'
        self.message['data'] = data
        json_formatted_data = json.dumps(self.message)
        # print(json_formatted_data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_formatted_data.encode())
        self.sock.close()

    def start_query_2(self):
        data = {}
        self.message['message_type'] = 'write'
        self.message['collection'] = 'syrup'
        data['name'] = 'syrup'
        data['store'] = 'costco'
        data['price'] = '69.69'
        self.message['data'] = data
        json_formatted_data = json.dumps(self.message)
        # print(json_formatted_data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_formatted_data.encode())
        self.sock.close()


    def start_query_3(self):
        data = {}
        self.message['message_type'] = 'write'
        self.message['collection'] = 'syrup'
        data['name'] = 'syrup'
        data['store'] = 'walmart'
        data['price'] = '69.69'
        self.message['data'] = data
        json_formatted_data = json.dumps(self.message)
        # print(json_formatted_data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_formatted_data.encode())
        self.sock.close()

if __name__ == "__main__":
    x = query()
    y = query()
    z = query()
    u = query()

    x.start_query()
    y.start_query_2()
    z.start_query_3()
    u.start_query_3()