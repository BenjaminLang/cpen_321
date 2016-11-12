# Take in Crawler Data and sends it to server
# Take in User Request and sends back item
import socket
import json


class query:
    def __init__(self):
        self.sock = socket.socket()
        self.host = socket.gethostname()
        self.port = 6969
        self.data = {}
        self.message = {}
        self._id = 1

    def start_query(self):
        self.message['message_type'] = "write"
        self.message['collection'] = "oranges"
        self.message['_id'] = self._id
        self.data['name'] = 'mandarin oranges'
        self.data['store'] = 'save on foods'
        self.data['price'] = '1.50'
        self.message['data'] = self.data
        json_formatted_data = json.dumps(self.message)
        # print(json_formatted_data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_formatted_data.encode())
        self.sock.close()

    def start_query2(self):
        self.message['message_type'] = "write"
        self.message['collection'] = "oranges"
        self.message['_id'] = self._id
        self.data['name'] = 'mandarin oranges'
        self.data['store'] = 'superstore'
        self.data['price'] = '1.40'
        self.message['data'] = self.data
        json_formatted_data = json.dumps(self.message)
        # print(json_formatted_data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_formatted_data.encode())
        self.sock.close()

    def start_query3(self):
        self.message['message_type'] = "read"
        self.message['collection'] = "oranges"
        json_formatted_data = json.dumps(self.message)
        # print(json_formatted_data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_formatted_data.encode())
        response = self.sock.recv(1024).decode()
        dict = json.loads(response)
        print(response)
        self.sock.close()

if __name__ == "__main__":
    x = query()
    y = query()
    y._id = x._id + 1

    x.start_query()
    y.start_query2()
    x._id += 1


    z = query()
    z.start_query3()