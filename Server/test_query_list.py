import socket
import json

class Query:
    def test(self):
        print('should pass')
        self.__make_liu()
        print('should pass')
        self.__insert_list_1()
        print('should pass')
        self.__insert_list_2()
        print('should pass')
        self.__retrieve_list_1()
        print('should pass')
        self.__retrieve_list_2()
        print('should fail')
        self.__retrieve_list_3()
        print('should pass')
        self.__delete_list_2()
        print('should fail')
        self.__retrieve_list_2()
        print('should pass')
        self.__delete_list_1()
        print('should fail')
        self.__delete_list_3()
        print('should fail')
        self.__retrieve_list_1()
        return

    def __send(self, json_data):
        sock = socket.socket()
        host = socket.gethostbyname(socket.gethostname())
        port = 6969

        sock.connect((host, port))
        sock.send(json_data.encode())
        print(sock.recv(1024).decode() + '\n')
        sock.close()

    def __make_liu(self):
        data = {}
        data['email'] = 'mablibsking@hotmail.com'
        data['password'] = 'UBC_student_2016'
        data['name'] = 'Ryan Liu'
        data['list'] = []
        data['list_names'] = []
        data['message_type'] = 'acc_create'

        json_data = json.dumps(data)
        self.__send(json_data)

    def __insert_list_1(self):
        data = {}
        data['message_type'] = 'add_list'
        data['list_name'] = 'list_1'
        data['email'] = 'mablibsking@hotmail.com'

        data_list = []
        data_list.append({'name': 'syrup',
                          'store': 'costco',
                          'price': '12.99'})
        data_list.append({'name': 'coffee',
                          'store': 'walmart',
                          'price': '5.99'})
        data['list'] = data_list

        json_data = json.dumps(data)
        self.__send(json_data)

    def __insert_list_2(self):
        data = {}
        data['message_type'] = 'add_list'
        data['list_name'] = 'list_2'
        data['email'] = 'mablibsking@hotmail.com'

        data_list = []
        data_list.append({'name': 'cereal',
                          'store': 'costco',
                          'price': '4.55'})
        data_list.append({'name': 'shoes',
                          'store': 'walmart',
                          'price': '5.99'})
        data['list'] = data_list

        json_data = json.dumps(data)
        self.__send(json_data)


    def __retrieve_list_1(self):
        data = {}
        data['message_type'] = 'retrieve_list'
        data['email'] = 'mablibsking@hotmail.com'
        data['list_name'] = 'list_1'

        json_data = json.dumps(data)
        self.__send(json_data)


    def __retrieve_list_2(self):
        data = {}
        data['message_type'] = 'retrieve_list'
        data['list_name'] = 'list_2'
        data['email'] = 'mablibsking@hotmail.com'

        json_data = json.dumps(data)
        self.__send(json_data)

    def __retrieve_list_3(self):
        data = {}
        data['message_type'] = 'retrieve_list'
        data['list_name'] = 'list_3'
        data['email'] = 'mablibsking@hotmail.com'

        json_data = json.dumps(data)
        self.__send(json_data)

    def __delete_list_1(self):
        data = {}
        data['message_type'] = 'delete_list'
        data['list_name'] = 'list_1'
        data['email'] = 'mablibsking@hotmail.com'

        json_data = json.dumps(data)
        self.__send(json_data)

    def __delete_list_2(self):
        data = {}
        data['message_type'] = 'delete_list'
        data['list_name'] = 'list_2'
        data['email'] = 'mablibsking@hotmail.com'

        json_data = json.dumps(data)
        self.__send(json_data)

    def __delete_list_3(self):
        data = {}
        data['message_type'] = 'delete_list'
        data['list_name'] = 'list_3'
        data['email'] = 'mablibsking@hotmail.com'

        json_data = json.dumps(data)
        self.__send(json_data)

if __name__ == '__main__':
    x = Query()
    x.test()






