# Takes in Crawler Data and sends it to server
# Takes in User Request and sends back item
from pymongo import MongoClient
from crawl_lib import *

class query:
    def __init__(self):
        self.client = MongoClient()

    def start_query(self):
        data = {}
        data['name'] = 'syrup'
        data['store'] = 'costco'
        data['price'] = '12.99'
        data['url'] = 'SyrupCostco.com'
        data['image'] = 'SyrupCostco.jpeg'

        send_to_db('syrups', data, self.client.categories_db, self.client.items_db)

    def start_query_2(self):
        data = {}
        data['name'] = 'syrup'
        data['store'] = 'walmart'
        data['price'] = '34.99'
        data['url'] = 'SyrupWalmart.com'
        data['image'] = 'SyrupWalmart.jpeg'

        send_to_db('syrups', data, self.client.categories_db, self.client.items_db)

if __name__ == "__main__":
    x = query()
    y = query()
    z = query()
    u = query()

    x.start_query()
    y.start_query_2()
    z.start_query()
    u.start_query_2()
