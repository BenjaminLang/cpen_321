from crawl_lib import *
from pymongo import MongoClient
import unicodedata
import json

# Parses starting from the base_url and sends the data to the db
def parse():
    deps_exclusions = {'91083', '5426', '4096'}
    full_json = json.loads(urlopen('http://api.walmartlabs.com/v1/taxonomy?format=json&apiKey=' + api_key).read().decode('utf8'))

    departments = full_json['categories']

    for department in departments:
        if department['id'] in deps_exclusions:
            continue
        categories = department['children']

        for category in categories:
            if 'children' in category:
                subcats = category['children']
            else:
                subcats = [category]

            for subcat in subcats:
                cat_id = subcat['id']
                cat_name = subcat['name']

                cat_json = json.loads(urlopen('http://api.walmartlabs.com/v1/paginated/items?format=json&category=' + cat_id + '&apiKey=' + api_key).read().decode('utf8'))
                items = cat_json['items']

                for item in items:
                    data = {}

                    name = item['name']
                    name.encode('ascii', 'ignore')
                    data['name'] = name.replace('.', '-').lower()

                    if 'salePrice' in item:
                        data['price'] = item['salePrice']
                    else:
                        continue # no price for this item

                    data['url'] = item['productUrl']

                    if 'thumbnailImage' in item:
                        data['image'] = item['thumbnailImage']
                    else:
                        data['image'] = ''

                    data['store'] = 'Walmart'

                    send_to_db(cat_name, data, categories_db, item_db, users_db)
    return

if __name__ == '__main__':
    api_key = 'dw25ngn8v6wa97qt757m2a97'
    client = MongoClient()
    categories_db = client.categories
    item_db = client.items
    users_db = client.users
    parse()

    # todo:
    # add category exclusion list
    # send timestamp along with json doc to server
