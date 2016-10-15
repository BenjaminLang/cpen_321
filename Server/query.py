# Take in Crawler Data and sends it to server
# Take in User Request and sends back item

class query():
    def __init__(self, ):
        self.sock = socket.socket()
        self.host = socket.gethostname()
        self.port = 6969

    def start_query(self):
        self.sock.connect((self.host, self.port))
        self.sock.send()
        print('client recieved: ',  repr(self.sock.recv(1024)))
        self.sock.close()

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
if __name__ == "__main__":
    spider()
"""

