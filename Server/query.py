# Take in Crawler Data and sends it to server
# Take in User Request and sends back item
import socket
import pickle
from message import find_message, insert_message, message_type

class query:
    def __init__(self, type):
        self.sock = socket.socket()
        self.host = socket.gethostname()
        self.port = 6969
        self.message = find_message(type)

    def start_query(self):
        self.sock.connect((self.host, self.port))
        request = pickle.dump(self.message)
        self.sock.send(request)
        print('client recieved: ',  repr(self.sock.recv(1024)))
        self.sock.close()

if __name__ == "__main__":
    x = query(message_type.find)
    x.start_query()



"""
def query():
    user_request = "Oranges"
    # print(user_request)
    return user_request


def spider():
    data_in = {
        "name": "Oranges",
        "store": "SuperStore",
        "price": 2.99
    }

    name = "Oranges"
    # print(name)
    # print(data_in)
    return name, data_in


"""

