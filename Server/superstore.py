from crawl_lib import *
from pymongo import MongoClient
import unicodedata

base_url = 'https://www.realcanadiansuperstore.ca'
client = MongoClient()
categories_db = client.categories
item_db = client.items
users_db = client.users

# Takes in a soup and sends the product to the db (including price, url, name, image)
def _send_product(prod, cat_item, url):
    data = {}
    for img in prod.find_all('img'):
        data['name'] = img['alt']
        data['image'] = img['src']
    for button in prod.find_all('button'):
        data['price'] = strip_name(str(button), 'Price:', 'EA').strip().replace('\n', '')

    data['store'] = 'Superstore'
    data['url'] = url
    print(data)
    return

    send_to_db(cat_item, data, categories_db, item_db, users_db)

    return

# Parses starting from the base_url and sends the data to the db
def parse():
    soup = get_soup(base_url)

    set_links = set()
    for cat in soup.find_all('li'):
        if "data-level" in cat.attrs and cat['data-level'] in ['2', '3']:
            for cat_link in cat.find_all('a', limit=1):
                set_links.add(base_url + cat_link['href'])

    for link in set_links:
        cat_soup = get_soup(link)
        for prod in cat_soup.find_all('div', 'product-image'):
            _send_product(prod, strip_name(link, 'superstore.ca', '/plp'), link)

    return

if __name__ == '__main__':
    parse()

    # todo:
    # send timestamp along with json doc to server
