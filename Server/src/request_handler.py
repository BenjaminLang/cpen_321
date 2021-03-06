import json
import queue

import bson.json_util
from cache_ops import CacheOps as mdo
from cat_ops import CatOps as cdo
from user_ops import UserOps as udo
from item_ops import ItemOps as ido

from pymongo import MongoClient
from operator import attrgetter


class RequestHandler:
    def __init__(self, queue=None):
        self.__queue = queue
        client = MongoClient()
        self.__cat_db = client.cat_db
        self.__items_db = client.items_db
        self.__users_db = client.users_db
        self.__cache_db = client.cache_db

    def handle_crawler(self, json_query):
        json_data = json.loads(json_query)
        req_type = json_data['message_type']
        json_response = {}
        if req_type == 'write':
            json_response = self.__handle_write(json_data)

        return json_response

    def handle_request(self):
        while True:
            try:
                (connection, json_data) = self.__queue.get(block=True, timeout=10)
            except queue.Empty:
                continue

            req_type = json_data['message_type']
            json_response = {}
            if req_type == 'write':
                json_response = self.__handle_write(json_data)
            elif req_type == 'read':
                json_response = self.__handle_read(json_data)
            elif req_type == 'acc_create':
                json_response = self.__handle_create(json_data)
            elif req_type == 'acc_del':
                json_response = self.__handle_delete(json_data)
            elif req_type == 'acc_login':
                json_response = self.__handle_login(json_data)
            elif req_type == 'acc_update':
                json_response = self.__handle_update(json_data)
            elif req_type == 'add_list':
                json_response = self.__handle_save_list(json_data)
            elif req_type == 'retrieve_list':
                json_response = self.__handle_retrieve_list(json_data)
            elif req_type == 'delete_list':
                json_response = self.__handle_delete_list(json_data)

            if req_type != 'read':
               print(json_response)

            json_response = bson.json_util.dumps(json_response)
            connection.send(json_response.encode())
            connection.close()

        return

    def __handle_write(self, json_data):
        # insert the data into the database
        # If item is already in, update data

        response = {}
        response['message_type'] = 'write_response'
        collection = json_data['collection']

        item_result = ido.insert_items(self.__items_db, json_data)
        cat_result = cdo.insert_cat(self.__cat_db, collection)

        if item_result and cat_result:
            response['status'] = 'success'
            #print('write success')
        elif item_result and not cat_result:
            response['status'] = 'item_insert'
            print('item_insert')
        elif not item_result and cat_result:
            response['status'] = 'cat_insert'
            print('cat insert')
        else:
            response['status'] = 'failed scrub'
            print('nyess')

        return response

    def __handle_read(self, json_data):
        response = {}  
        response['message_type'] = 'read_response'
        item_name = json_data['items'][0].lower()
        cache_results = mdo.read_cache(self.__cache_db, item_name)
        if cache_results == 'Not found':
            # get the categories to search into
            categories = cdo.return_categories(self.__cat_db)
            results, cat_list = ido.read_items(self.__items_db, json_data, categories)
        else:
            # read item db with given category
            results, cat_list = ido.read_items(self.__items_db, json_data, cache_results)

        mdo.insert_cache(self.__cache_db, item_name, cat_list)
        ret_data = []
        for i in results:
            for j in i:
                ret_data.append(j)

        num = int(json_data['options']['num'])
        if num == -1:
            num = 100

        if json_data['options']['price'] == 'min':
            ret_data.sort(key=lambda x: float(x['data']['price']))
        elif json_data['options']['price'] == 'max':
            ret_data.sort(key=lambda x: float(x['data']['price']), reverse=True)

        response['items'] = ret_data[0:num]

        return response

    def __handle_save_list(self, json_data):
        response = {}
        response['message_type'] = 'save_list_response'
        list_save_status = udo.add_list(self.__users_db, json_data)

        response['status'] = list_save_status

        return response

    def __handle_retrieve_list(self, json_data):
        response = {}
        response['message_type'] = 'retrieve_list_response'
        retrieved_list = udo.retrieve_lists(self.__users_db, json_data)

        if len(retrieved_list) != 0:
            response['list'] = retrieved_list
            response['status'] = 'success'
        else:
            response['status'] = 'failed'
        return response

    def __handle_delete_list(self, json_data):
        response = {}
        response['message_type'] = 'delete_list_response'
        deletion_status = udo.delete_list(self.__users_db, json_data)

        response['status'] = deletion_status

        return response

    def __handle_create(self, json_data):
        response = {}
        response['message_type'] = 'acc_create_response'
        del json_data['message_type']

        create_res = udo.create_acc(self.__users_db, json_data)

        response['status'] = create_res

        return response

    def __handle_delete(self, json_data):
        response = {}
        response['message_type'] = 'acc_delete_response'
        del json_data['message_type']

        del_res = udo.del_acc(self.__users_db, json_data)

        response['status'] = del_res

        return response

    def __handle_login(self, json_data):
        response = {}
        response['message_type'] = 'login_response'
        del json_data['message_type']

        log_res, name = udo.log_in(self.__users_db, json_data)
        if log_res is 'success':
            response['list_names'] = udo.get_user_list_names(self.__users_db, json_data)
        response['name'] = name
        response['status'] = log_res # DNE, success, or failed
        return response

    def __handle_update(self, json_data):
        response = {}
        response['message_type'] = 'update_response'
        del json_data['message_type']

        update_res = udo.update_acc(self.__users_db, json_data)

        response['status'] = update_res

        return response

