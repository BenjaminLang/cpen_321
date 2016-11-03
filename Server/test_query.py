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
        self.message['message_type'] = 'read'
        self.message['items'] = ['syrup']
        json_formatted_data = json.dumps(self.message)
        # print(json_formatted_data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_formatted_data.encode())
        self.sock.close()

    def start_query_2(self):
        self.message['message_type'] = 'read'
        self.message['items'] = ['chocolate']
        json_formatted_data = json.dumps(self.message)
        # print(json_formatted_data)

        self.sock.connect((self.host, self.port))
        self.sock.send(json_formatted_data.encode())
        self.sock.close()

if __name__ == "__main__":
    x = query()
    y = query()

    x.start_query()
    # y.start_query_2()