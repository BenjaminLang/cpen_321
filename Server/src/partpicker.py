from crawl_lib import *

from selenium import webdriver

def _send_product(link, cat_name, base_url):
    soup = get_soup(link)

    data = {}
    data['image'] = ''

    for name in soup.find_all('h1', 'name', limit=1):
        data['name'] = strip_name(str(name), '<h1 class="name">', '</h1>')

    for a in soup.find_all('a', 'item', limit=1):
        for img in a.find_all('img', limit=1):
            data['image'] = img['src']

    for tbody in soup.find_all('tbody', limit=1):
        for tr in tbody.find_all('tr'):
            merchant = tr.find_all('td', 'merchant', limit=1)
            if not merchant:
                continue # this is a 'note' divider, not an actual store/price

            for img in merchant[0].find_all('img', limit=1):
                data['store'] = strip_name(str(img), 'alt="', '"')

            for base in tr.find_all('td', 'base', limit=1):
                for a in base.find_all('a', limit=1):
                    price = strip_name(str(a), 'target="_blank">', '</a>').replace('$', '')
                    price = '%.2f' % float((price.replace(',', '')))
                    data['price'] = price

                    data['url'] = base_url + a['href']

            send_to_db(cat_name, data)

    return

# Takes in a soup and sends the products to the db (including price, url, name, image)
def _send_products(link, cat_name, base_url):
    print(link)

    data = {} # player, quality, stuff

    browser = webdriver.PhantomJS(executable_path=r'/home/ryangroup/node_modules/phantomjs-prebuilt/bin/phantomjs')
    browser.set_window_size(112, 55)
    browser.set_page_load_timeout(60)
    browser.get(link)

    prod_list = browser.find_element_by_tag_name('tbody')

    prods = prod_list.find_elements_by_tag_name('tr')

    for prod in prods:
        tdname = prod.find_element_by_class_name('tdname')
        a = tdname.find_element_by_tag_name('a')
        prod_link = a.get_attribute('href')
        _send_product(prod_link, cat_name, base_url)
        time.sleep(5)

    return

# Parses starting from the base_url and sends the data to the db
def parse():
    base_url = 'http://ca.pcpartpicker.com'
    soup = get_soup(base_url + '/products')

    for section in soup.find_all('ul', 'inside'):
        for cat in section.find_all('a'):
            cat_link = base_url + cat['href']
            _send_products(cat_link, strip_name(cat['href'], '/products/', '/'), base_url)
    return

if __name__ == '__main__':
    parse()

    # todo:
    # send timestamp along with json doc to server
