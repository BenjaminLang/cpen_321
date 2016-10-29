from urllib.request import urlopen
from bs4 import BeautifulSoup
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
                    continue # no price for this item

                list_description = caption.find_all('p', 'description')
                if list_description:
                    description= list_description[0]
                    name = str(list_description[0]).split('>')[1].split('<')[0]
                else:
                    continue # no name for this item
            else:
                continue # no info for this item

            data = {}
            data['url'] = url
            data['image'] = image
            data['price'] = price
            data['name'] = name

            retList.append(data)
        
    return retList


# take a costco.ca url and return the part after the slash (usually a category name)
def strip_name(url):
    return url.split('costco.ca/')[1].split('.html')[0]


if __name__ == '__main__':
    soup = get_soup('http://www.costco.ca')
    departments = get_links(soup, 'li', 'category-level-1')
    
    mega_list = {}

    for department in departments:
        dep_name = strip_name(department)
        mega_list[dep_name] = {}

        depSoup = get_soup(department)
        categories = get_links(depSoup, 'div', 'col-xs-6 col-md-3')
        for category in categories:
            cat_name = strip_name(category)

            catSoup = get_soup(category)
            subCats = get_links(catSoup, 'div', 'col-xs-6 col-md-3')
            if not subCats: # subCats is empty for this category
                mega_list[dep_name][cat_name] = get_products(catSoup)
            else:
                mega_list[dep_name][cat_name] = {}
                for subCat in subCats:
                    subCat_name = strip_name(subCat)
                    productSoup = get_soup(subCat)
                    mega_list[dep_name][cat_name][subCat_name] = get_products(productSoup)
        break
        # uncomment this break ^ to quickly see the output for 1 department to figure out how it's laid out

    json_mega_list = json.dumps(mega_list, indent=2)

    print(json_mega_list)

    # todo:
    # add category exclusion list
    # send timestamp along with json doc to server
    # server should parse doc and update prices in the database??

    # change get_products to return a dictionary too, with key being id of product??