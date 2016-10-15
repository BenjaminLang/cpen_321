from enum import Enum

class message_type(Enum):
    find = 1
    insert = 2

class message:
    def __init__(self, type):
        self.type = type
        self.items = None
        self.to_insert = None

class find_message(message):
    def init_find_message(self, items):
        # function to set items to query for in database
        if self.type == message_type.find:
            self.items = items
        else:
            print('message type mismatch')

class insert_message(message):
    def init_insert_message(self, to_insert):
        # function to set items to insert/update in database
        # to_insert is a json formatted object
        if self.type == message_type.insert:
            self.to_insert = to_insert
        else:
            print('message type mismatch')






