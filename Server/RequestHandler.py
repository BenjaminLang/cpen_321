import pymongo


class RequestHandler:
    def _init(self, mongo_db):
        self.mongo_db = mongo_db

    def handle_request(self, req_type, json_data):
        if req_type == 'write':
            json_response = self.handle_write(json_data)
        elif req_type == 'read':
            json_response = self.handle_read(json_data)
        elif req_type == 'update_collection':
            json_response = self.handle_update_collection(json_data)
        elif req_type == 'read_collection_documents':
            json_response = self.handle_read_collection_documents(json_data)
        # else return a ill-formed message response

        return json_response

    def handle_write(self, json_data):
        # insert the data into the database
        try:
            collection = json_data['collection']
            del json_data['message_type']
            self.mongo_db[collection].save(json_data)
            response = {}
            response['message_type'] = 'write_response'

        # construct response message
        except Exception:
            response['status'] = 'failed'

        response['status'] = 'completed'
        return response

    def handle_read(self, json_data):
        # Sort by price and return first
        # Assumptions:
        # Price is a valid field in the document
        collections = json_data['collections']
        response = {}
        response['message_type'] = 'read_response'

        i = 0
        for collection in collections:
            response['collections'][i] = collection
            response[collection] = self.mongo_db[collection].find_one(
                {"data.price": {"$exists": True}}, sort=[("data.price", pymongo.ASCENDING)])
            i += 1

        return response

    def handle_update_collection(self, json_data):
        # insert the data into the database
        return 0

    """
    def handle_read_collection_documents(self, json_data):
        return response
    """

