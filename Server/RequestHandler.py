import json
import traceback


class RequestHandler:
    def __init__(self, categories_db, items_db, users_db):
        self.__categories_db = categories_db
        self.__items_db = items_db
        self.__users_db = users_db

    # delegates the requests based on request type
    def handle_request(self, req_type, json_data):
        json_response = {}
        if req_type == 'write':
            json_response = self.__handle_write(json_data)
        elif req_type == 'read':
            json_response = self.__handle_read(json_data)
        elif req_type == 'acc_create':
            json_response = self.__handle_create(json_data)
        elif req_type == 'acc_del':
            json_respone = self.__handle_delete(json_data)
        elif req_type = 'log_in':
            json_response = self.__handle_login(json_data)
        return json_response

    def __handle_write(self, json_data):
        # insert the data into the database
        # If item is already in, update data
        response = {}
        response['message_type'] = 'write_response'
        try:
            # convert JSON to dictionary type and extract indexing information
            print (json_data)
            msg = json.loads(json_data)
            url = msg['data']['url']
            words = (msg['data']['name']).split()
            msg['words'] = words
            del msg['message_type']
            del msg['collection']

            for word in words:
                # insert them into the database
                data = list(self.__items_db[word].find({'data.url': {'$eq': url}}))
                # if you get a valid ID, you know that the item exists, so update
                if len(data) != 0:
                    msg['_id'] = data[0]['_id']
                    self.__items_db[word].save(msg)
                # otherwise make a new item
                else:
                    self.__items_db[word].insert(msg)

        # construct response message
        except Exception :
            traceback.print_exc()
            response['status'] = 'failed'

        response['status'] = 'success'
        return response

    def __handle_read(self, json_data):
        response = {}
        response['message_type'] = 'read_response'    
        try:
            # set up appropriate indexing information, json_data is a dict
            items = json_data['items']
            results = []
            result = []
            price = json_data['options']['price']
            num = int(json_data['options']['num'])

            item_words = items.split()

            query = {'words': { '$all': item_words }}
            print(query)

            if item_words is not None:
                if(price == 'min'):
                    res_data = list(self.__items_db[item_words[0]].find(query).sort('data.price',1))
                    if(num == -1):
                        result = res_data
                    else:
                        result = res_data[0:(num-1)]
                else:
                    res_data = list(self.__items_db[item_words[0]].find(query))
                    
                    if(num == -1):
                        result = res_data
                    else:
                        result = res_data[0:(num-1)]
delet
                if result is not None:
                    for res in result:
                        del res['_id']
                        del res['words']
                
                results.append(result)
            response['items'] = results
            
        except Exception:
            traceback.print_exc()

        return response

    def __handle_create(self, json_data):
        response = {}
        response['message_type'] = 'acc_create_response'
        try:            
            msg = json.loads(json_data)
            
            data = []
            data.append(msg['username'])
            data.append(msg['password'])
            data.append(msg['location'])
            data.append(msg['email'])
            
            user_cat = ['user', 'pass', 'loc', 'email']

            for i in range (0, 3):
                message = {}
                message['category'] = user_cat[i]
                message[user_cat[i]] = data[i]
                json_formatted_data = json.dumps(message)
                self.users_db[data[0]].insert(json_formatted_data)
            response['status'] = 'success'

        except Exception:
            traceback.print_exc()
            response['status'] = 'failed'

        return response

    def __handle_delete(self, json_data):
        response = {}
        response['message_type'] = 'acc_delete_response'
        try:            
            msg = json.loads(json_data)
            user = msg['user']
            
            del msg['message_type']
            del msg['user']
            
            result = self.__users_db[user].delete_many({})
        
            if result.deleted_count != 0:
                response['status'] = 'success'
            else:
                response['status'] = 'failed'
        except Exception:
            traceback.print_exc()
            response['status'] = 'failed'
        return response

    def __handle_login(self, json_data):
        response = {}
        response['message_type'] = 'login_response'
        try:
            msg = json.loads(json_data)
            user = msg['user']
            auth = msg['pass']

            del msg['message_type']
            del msg['user']
            
            ''' 
            user_data = list(self.__users_db[user].find({'category':'pass'})
            if(user_data[0]['pass'] == auth):
                response['status'] = 'authenticated'
            else:
                response['status'] = 'wrong authentication'           
            '''
        except Exception:
            traceback.print_exc()
            response['status'] = 'failed'

        return response

