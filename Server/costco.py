from urllib.request import urlopen
from bs4 import BeautifulSoup
from query import DataHandler
import json


# take in a url and return a soup
def get_soup(url):
    html = urlopen(url).read()
    return BeautifulSoup(html, 'html.parser')


# take in a soup, tag_name, class_name and return all "a href" links in that soup
def get_links(soup, tag_name, class_name):
    links = list()
    for tag in soup.find_all(tag_name, class_name):
        for tagLink in tag.find_all('a'):
            links.append(tagLink['href'])
    return links


# take a soup and return a list of all products (including price, url, name, image)
def get_products(soup):
    retList = list()
    for prod in soup.find_all('div', 'col-xs-6 col-md-4 col-xl-3 product'):
        list_a = prod.find_all('a')
        if list_a:
            thumbnail = list_a[0]
            url = thumbnail['href']
            image = 'image_not_found_special_code'
            list_img = thumbnail.find_all('img')
            price = 0
            name = 'name'
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
                    description = list_description[0]
                    name = str(list_description[0]).split('>')[1].split('<')[0]
                else:
                    continue  # no name for this item
            else:
                continue  # no info for this item

            data = {}
            data['name'] = name
            data['price'] = price
            data['url'] = url
            data['image'] = image
            data['store'] = 'costco'

            retList.append(data)

    return retList


# take a costco.ca url and return the part after the slash (usually a category name)
def strip_name(url):
    return url.split('costco.ca/')[1].split('.html')[0]


# Sending individual categories (documents) to database
def send_to_db(dep_name, cat_name):
    item = {}
    data = {}
    sub_data = {}
    item['message_type'] = 'write'
    item['collection'] = dep_name

    catSoup = get_soup(category)
    subCats = get_links(catSoup, 'div', 'col-xs-6 col-md-3')
    if not subCats:  # subCats is empty for this category
        data = get_products(catSoup)
    else:
        data = {}
        for subCat in subCats:
            subCat_name = strip_name(subCat)
            productSoup = get_soup(subCat)
            sub_data = get_products(productSoup)
            data[subCat_name] = sub_data
    item['data'] = data
    json_data = json.dumps(item, indent = 2)
    service = DataHandler()
    service.send_data(json_data)

    return json_data


if __name__ == '__main__':
    soup = get_soup('http://www.costco.ca')
    departments = get_links(soup, 'li', 'category-level-1')

    for department in departments:
        dep_name = strip_name(department)

        depSoup = get_soup(department)
        categories = get_links(depSoup, 'div', 'col-xs-6 col-md-3')
        for category in categories:
            cat_name = strip_name(category)
            print(send_to_db(dep_name, cat_name))
            print("\n")

    # todo:
    # add category exclusion list
    # send timestamp along with json doc to server
    # server should parse doc and update prices in the database??

    # change get_products to return a dictionary too, with key being id of product??
