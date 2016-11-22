from crawl_lib import *

# Takes in a soup and sends the product to the db (including price, url, name, image)
def _send_product(prod, cat_name):
    data = {}
    name = ''
    for span in prod.find_all('span'):
        if 'js-product-entry' in str(span):
            name = name + strip_name(str(span), '>', '</')

    if name == '':
        return # name was not found for this product
    data['name'] = name

    for img in prod.find_all('img', limit=1):
        data['image'] = img['src']

    price = 0
    for span in prod.find_all('span'):
        if 'Regular Price:' in str(span):
            price = strip_name(span['aria-label'], 'Regular Price:', None).strip()
            break

    if price == 0:
        return # price was not found for this product
    data['price'] = '%.2f' % float(price)

    for link in prod.find_all('a', limit=1):
        data['url'] = 'https://www.realcanadiansuperstore.ca' + link['href']

    data['store'] = 'Superstore'

    send_to_db(cat_name, data)

    return

# Parses starting from the base_url and sends the data to the db
def parse():
    base_url = 'https://www.realcanadiansuperstore.ca'
    soup = get_soup(base_url)

    set_links = set()
    for cat in soup.find_all('li'):
        if "data-level" in cat.attrs and cat['data-level'] in ['2', '3']:
            for cat_link in cat.find_all('a', limit=1):
                set_links.add(base_url + cat_link['href'])

    for link in set_links:
        cat_soup = get_soup(link)
        for prod in cat_soup.find_all('div', 'product-page-hotspot'):
            cat_name = strip_name(link, 'superstore.ca', '/plp').split('/c/')[0].split('/')
            cat_name = cat_name[len(cat_name) - 2].replace('%26', '&')
            _send_product(prod, cat_name)

    return

if __name__ == '__main__':
    parse()

    # todo:
    # send timestamp along with json doc to server
