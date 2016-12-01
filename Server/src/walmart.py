import json
import time

from crawl_lib import *

# Parses starting from the base_url and sends the data to the db
def parse():
    deps_exclusions = {'91083', '5426', '4096', '4104'}
    full_json = json.loads(urlopen('http://api.walmartlabs.com/v1/taxonomy?format=json&apiKey=' + api_key).read().decode('utf8'))

    departments = full_json['categories']

    for department in departments:
        if department['id'] in deps_exclusions:
            continue
        print(department['id'])
        categories = department['children']

        for category in categories:
            if 'name' in category:
                cat_name = category['name']
            else:
                print('there is no name for this category! skipping it for now: ' + category)
                continue

            if 'children' in category:
                subcats = category['children']
            else:
                subcats = [category]

            for subcat in subcats:
                cat_id = subcat['id']

                cat_json = json.loads(urlopen('http://api.walmartlabs.com/v1/paginated/items?format=json&category=' + cat_id + '&apiKey=' + api_key).read().decode('utf8'))
                items = cat_json['items']

                for item in items:
                    data = {}

                    data['name'] = item['name']

                    if 'salePrice' in item:
                        price = '%.2f' % item['salePrice']
                        data['price'] = price
                    else:
                        continue # no price for this item

                    data['url'] = item['productUrl']

                    if 'thumbnailImage' in item:
                        data['image'] = item['thumbnailImage']
                    else:
                        data['image'] = ''

                    data['store'] = 'Walmart'

                    send_to_db(cat_name, data)
                    time.sleep(0.1)
    return

if __name__ == '__main__':
    api_key = 'dw25ngn8v6wa97qt757m2a97'
    parse()

    # todo:
    # send timestamp along with json doc to server
