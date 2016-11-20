import json
from urllib.request import urlopen

from bs4 import BeautifulSoup

from src.request_handler import RequestHandler


# Takes in a url and returns a soup
def get_soup(url):
    html = urlopen(url).read()
    return BeautifulSoup(html, 'html.parser')

# Takes a string and returns the substring after pre and before post
def strip_name(string, pre, post):
    return string.split(pre)[1].split(post)[0]

# Sending individual categories (documents) to database
def send_to_db(cat_name, info_object, cat_db, items_db, users_db, cache_db):
    item = {}
    data = {}
    sub_data = {}
    item['message_type'] = 'write'
    item['collection'] = cat_name.replace('$', '').replace('-', '').replace('.', '').replace(' ', '').lower()

    item['data'] = info_object
    json_data = json.dumps(item, indent = 2)
    service = RequestHandler()
    service.handle_crawler(json_data)
