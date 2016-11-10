from pymongo import MongoClient
from src.RequestHandler import RequestHandler
import json


class DataHandler:
    def send_data(self, input_data):
        msg = json.loads(input_data)
        # process input_data from costco.py
        msg_type = msg['message_type']

        client = MongoClient()
        db = client.test

        service = RequestHandler(db, None)
        service.handle_request(msg_type, input_data)
