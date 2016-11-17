from crawl_lib import *
from pymongo import MongoClient
import unicodedata

base_url = 'https://www.realcanadiansuperstore.ca'
client = MongoClient()
categories_db = client.categories
item_db = client.items
users_db = client.users

# take in a soup, tag_name, class_name and return all "a href" links in that soup
def _get_links(soup, tag_name, class_name):
    links = list()
    for tag in soup.find_all(tag_name, class_name):
        for tagLink in tag.find_all('a'):
            links.append(tagLink['href'])
    return links

# Takes a soup and sends all products to the db (including price, url, name, image)
def _send_products(soup, cat_item):
    print soup
    return
    for prod in soup.find_all('div', 'product-cell quickview-button'):
        print prod
        return
        #TODO: add rest of item and send to db
        if prod_info:
            info = prod_info[0]
            print info
            return
            url = thumbnail['href']
            image = ''
            list_img = thumbnail.find_all('img')

            if list_img:
                img = list_img[0]
                if img.has_attr('src'):
                    image = img['src']
                elif img.has_attr('data-src'):
                    image = img['data-src']

            list_caption = thumbnail.find_all('div', 'caption')
            if list_caption:
                caption = list_caption[0]
                list_price = caption.find_all('div', 'price')
                if list_price:
                    price = str(list_price[0]).split('$')[1].split('</')[0]
                else:
                    continue  # no price for this item

                list_description = caption.find_all('p', 'description')
                if list_description:
                    name = str(list_description[0]).split('>')[1].split('<')[0]
                else:
                    continue  # no name for this item
            else:
                continue  # no info for this item

            data = {}
            name.encode('ascii', 'ignore')
            data['name'] = name.replace('.', '-').lower()
            data['price'] = price
            data['url'] = url
            data['image'] = image
            data['store'] = 'Save on Foods'

            send_to_db(cat_item, data, categories_db, item_db, users_db)
    return

# Parses starting from the base_url and sends the data to the db
def parse():
    soup = get_soup(base_url)
    #food_soup = get_soup(base_url + '/food')
    #home_soup = get_soup(base_url + '/homenLifestyle')

    set_links = set()
    for cat in soup.find_all('li'):
        if "data-level" in cat.attrs and cat['data-level'] in ['2', '3']:
            for cat_link in cat.find_all('a', limit=1):
                set_links.add(base_url + cat_link['href'])

    for link in set_links:
        cat_soup = get_soup(link)
        #print link
        #print cat_soup
        for prod in cat_soup.find_all('div', 'product-image '):
            print prod
            print('\n\n\nyoyo\n\n\n')
            print strip_name(link, 'superstore.ca', '/plp')
            return
            _send_product(prod, split_name(link, 'superstore.ca', '/plp'))
            

    return

if __name__ == '__main__':
    parse()

    # todo:
    # send timestamp along with json doc to server
