import json
import traceback

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
            print (json_data)
            # msg = json.loads(json_data)
            msg = json_data
            collection = msg['collection']
            item_name = msg['data']['name']
            words = item_name.split()
            store_name = msg['data']['store']
            msg['words'] = words
            del msg['message_type']
            del msg['collection']

            for word in words:
                # insert them into the database
                self.__categories_db[collection].insert({'item': word})
                data = list(self.__items_db[word].find({'$and': [{'store': {'$exists': True, '$eq': store_name}},
                                            {'name': {'$exists': True, '$eq': item_name}}]}))

                # if you get a valid ID, you know that the item exists, so update
                if len(data) != 0:
                    valid_ID = data[0]
                    msg['_id'] = valid_ID
                    self.__items_db[word].save(msg)
                # otherwise make a new item
                self.__items_db[word].insert(msg)

        # construct response message
        except Exception :
            traceback.print_exc()
            response['status'] = 'failed'
            print(Exception)

        response['status'] = 'completed'
        return response

    def __handle_read(self, json_data):
        # set up appropriate indexing information, json_data is a dict
        response = {}
        response['message_type'] = 'read_response'
        items = json_data['items']
        results = []

        for item in items:
            item_words = item.split()
            query_array = []

            # For each word in request
            for searchable_item in item_words:
                query_array.append({'words': {'$elemMatch': {'$eq': searchable_item}}})

            query = {'$and': query_array}
            for searchable_item in item_words:
                result = list(self.__items_db[searchable_item].find(query))
                if result is not None:
                    results.append(result)
                    break

        response['items'] = results
        return response



