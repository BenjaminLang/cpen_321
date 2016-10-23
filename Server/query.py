from pymongo import MongoClient

from RequestHandler import RequestHandler


class DataHandler:
    def send_data(input_data):
        # process input_data from costco.py
        msg_type = input_data['message_type']

        client = MongoClient()
        db = client.test

        service = RequestHandler(db)
        service.handle_request(msg_type, input_data)
