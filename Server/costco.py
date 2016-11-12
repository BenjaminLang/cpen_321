from crawl_lib import *
from pymongo import MongoClient
import unicodedata


base_url = 'http://www.costco.ca'
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
    for prod in soup.find_all('div', 'col-xs-6 col-md-4 col-xl-3 product'):
        list_a = prod.find_all('a')
        if list_a:
            thumbnail = list_a[0]
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
            data['store'] = 'Costco'

            send_to_db(cat_item, data, categories_db, item_db, users_db)
    return

# Parses starting from the base_url and sends the data to the db
def parse():
    soup = get_soup(base_url)
    departments = _get_links(soup, 'li', 'category-level-1')

    deps_exclusions = {'auto', 'funeral', 'gift-cards-tickets-floral'}

    for department in departments:
        if strip_name(department, 'costco.ca/', '.html') in deps_exclusions:
            continue

        dep_soup = get_soup(department)
        categories = _get_links(dep_soup, 'div', 'col-xs-6 col-md-3')
        for category in categories:
            cat_name = strip_name(category, 'costco.ca/', '.html')

            cat_soup = get_soup(category)
            sub_cats = _get_links(cat_soup, 'div', 'col-xs-6 col-md-3')
            if not sub_cats: # subCats is empty for this category
                _send_products(cat_soup, cat_name)
            else:
                for subcat in sub_cats:
                    subcat_name = strip_name(subcat, 'www.costco.ca/', '.html')
                    product_soup = get_soup(subcat)
                    _send_products(product_soup, subcat_name)
    return

if __name__ == '__main__':
    parse()

    # todo:
    # send timestamp along with json doc to server
