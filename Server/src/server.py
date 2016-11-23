import json
import queue
import socket
import traceback


class DatabaseServer:
    def __init__(self, queue):
        self.__queue = queue
        # set up server socket
        self.__server_socket = socket.socket()
        host = socket.gethostbyname(socket.gethostname())
        port = 6969
        self.__server_socket.bind((host, port))

    def test_server(self):
        self.__server_socket.listen(5)
        try:
            while True:
                connection, addr = self.__server_socket.accept()
                #print('connected')
                try:
                    data = connection.recv(1024).decode()
                except Exception :
                    #print('disconnected')
                    continue

                json_data = json.loads(data)
                #print(json_data)
                try:
                    self.__queue.put(item=(connection, json_data), block=True, timeout=10)

                except queue.Full:
                    print('queue full')
                    continue

        except Exception :
            traceback.print_exc()
