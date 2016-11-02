import json
from pymongo import collection


class RequestHandler:

    def __init__(self, categories_db, items_db):
        self.__categories_db = categories_db
        self.__items_db = items_db

    # delegates the requests based on request type
    def handle_request(self, req_type, json_data):
        json_response = {}
        if req_type == 'write':
            json_response = self.__handle_write(json_data)
        elif req_type == 'read':
            json_response = self.__handle_read(json_data)
        return json_response

    def __handle_write(self, json_data):
        # insert the data into the database
        # If item is already in, update data
        response = {}
        response['message_type'] = 'write_response'
        try:
            # convert JSON to dictionary type and extract indexing information
            msg = json.loads(json_data)
            collection = msg['collection']
            item_name = msg['info_object']['name']
            words = item_name.split(' ')
            store_name = json_data['info_object']['store']
            del msg['message_type']
            del msg['collection']

            for word in words:
                # insert them into the database
                self.__categories_db[collection].insert({'item': word})
                valid_ID = self.__items_db[word]\
                    .find({'store': {'$exists': True, '$eq': store_name, '$eq': item_name}})['_id']
                json_data['words'] = words

                # if you get a valid ID, you know that the item exists, so update
                if valid_ID:
                    json_data['_id'] = valid_ID
                    self.__items_db[word].save(json_data)
                # otherwise make a new item
                self.__items_db[word].insert(json_data)

        # construct response message
        except Exception :
            response['status'] = 'failed'
            print('gotrekt')

        response['status'] = 'completed'
        return response

    def __handle_read(self, json_data):
        # set up appropriate indexing information
        response = {}
        response['message_type'] = 'read_response'
        items = json_data['items']
        category_names = self.__categories_db.collection_names
        results = []

        for item in items:
            item_words = item.split(' ')
            for word in item_words:
                if word in category_names:
                    query_array = []
                    query = {}
                    for item_word in item_words:
                        query_array.append({'words': {'$in': [item_word]}})
                    query = {'$and': query_array}
                    results.append(self.__items_db[word].find(query))
                # Else go through the entire database
                # Can flag collection to avoid a few more checks
                else:
                    # For each word in request
                    query = {}
                    for word_2 in item_words:
                        # Construct an array to search
                        query_array.append({'words': {'$in': [word_2]}})
                    query = {'$and': query_array}
                    # For every collection
                    # Checking each all documents for items
                    result = self.__items_db[word].find(query)
                    if result is not None:
                        results.append(result)
                        break

        # results will be a 2 dimensional array containing results for each item
        response['items'] = results

        return response


