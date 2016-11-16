import json
from item_ops import ItemOps as ido
from cat_ops import CatOps as cdo
from cache_ops import CacheOps as mdo
from user_ops import UserOps as udo

class RequestHandler:
    def __init__(self, cat_db, items_db, users_db, cache_db):
        self.__cat_db = cat_db
        self.__items_db = items_db
        self.__users_db = users_db
        self.__cache_db = cache_db

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
            json_response = self.__handle_delete(json_data)
        elif req_type == 'log_in':
            json_response = self.__handle_login(json_data)
        elif req_type == 'update_acc':
            json_response = self.__handle_update(json_data)
        return json_response

    def __handle_write(self, json_data):
        # insert the data into the database
        # If item is already in, update data

        response = {}
        response['message_type'] = 'write_response'
        insert_query = json.loads(json_data)
        collection = insert_query['collection']

        item_result = ido.insert_items(self.__items_db, insert_query)
        cat_result = cdo.insert_cat(self.__cat_db, collection)
       
        if item_result and cat_result:
            response['status'] = 'success'
            #print('write success')
        elif item_result and not cat_result:
            response['status'] = 'item_insert'
            #print('item_insert')
        elif not item_result and cat_result:
            response['status'] = 'cat_insert'
            print('cat insert')
        else:
            response['status'] = 'failed scrub'
            #print('nyess')

        return response

    def __handle_read(self, json_data):
        response = {}  
        response['message_type'] = 'read_response'
        item_name = json_data['items'][0]
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

        response['items'] = ret_data

        return response

    def __handle_save_list(self, json_data):
        response = {}
        response['message_type'] = 'message_save_response'

    # def __retrieve_lists(self, json_data):

    def __handle_create(self, json_data):
        response = {}
        response['message_type'] = 'acc_create_response'
        del json_data['message_type']

        create_res = udo.create_acc(self.__users_db, json_data)

        if create_res:
            response['status'] = 'success'
        else:
            response['status'] = 'failed'

        return response

    def __handle_delete(self, json_data):
        response = {}
        response['message_type'] = 'acc_delete_response'
        del json_data['message_type']

        del_res = udo.del_acc(self.__users_db, json_data)

        if del_res:
            response['status'] = 'success'
        else:
            response['status'] = 'failed'

        return response

    def __handle_login(self, json_data):
        response = {}
        response['message_type'] = 'login_response'
        del json_data['message_type']

        log_res = udo.log_in(self.__users_db, json_data)

        if log_res:
            response['status'] = 'Authenticated'
        else:
            response['status'] = 'Wrong Authentication'

        return response

    def __handle_update(self, json_data):
        response = {}
        response['message_type'] = 'update_response'
        del json_data['message_type']

        update_res = udo.update_acc(self.__users_db, json_data)

        if update_res:
            response['status'] = 'Updated properly'
        else:
            response['status'] = 'Could not update'

        return response
