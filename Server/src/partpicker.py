from crawl_lib import *

# Takes in a soup and sends the products to the db (including price, url, name, image)
def _send_products(link, cat_name, base_url):
    cat_soup = get_soup(link)
    print link
    print cat_soup
    exit()
    for td in cat_soup.find_all('td', 'tdname'):
        for prod in td.find_all('a', limit=1):
            prod_link = base_url + prod['href']
            prod_soup = get_soup(prod_link)
            print prod_soup
            exit()

            for title in prod_soup.find_all('div', 'title', limit=1):
                for name in title.find_all('h1', 'name', limit=1):
                    data['name'] = strip_name(name, '>', '</h1>')
                    print data['name']
                    break
    data = {}

    #send_to_db(cat_name, data)

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
