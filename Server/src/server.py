import json
import queue
import socket
import traceback
import ssl

class DatabaseServer:
    def __init__(self, queue):
        self.__queue = queue
        # set up server socket
        # from python documentation
        self._context = ssl.create_default_context(ssl.Purpose.CLIENT_AUTH)
        self._context.load_cert_chain(certfile="server.crt", keyfile="server.key")

        host = socket.gethostbyname(socket.gethostname())
        port = 6969
        self._server_socket = socket.socket()
        self._server_socket.bind((host,port))


    def test_server(self):
        self._server_socket.listen(5)
        try:
            while True:
                connection, addr = self._server_socket.accept()

                try:
                    secure_conn = self._context.wrap_socket(connection, server_side=True)
                except ssl.SSLError:
                    print('caught SSL Error!!')
                    continue
                
                #print('connected')
                try:
                    data = secure_conn.recv(1024).decode()
                except Exception :
                    #print('disconnected')
                    continue

                try:
                    json_data = json.loads(data)
                except json.decoder.JSONDecodeError:
                    print('caught JSON decode error')
                    continue

                print(json_data)

                try:
                    self.__queue.put(item=(secure_conn, json_data), block=True, timeout=10)

                except queue.Full:
                    print('queue full')
                    continue

        except Exception :
            traceback.print_exc()
