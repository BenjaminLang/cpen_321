# Take in Crawler Data and sends it to server
# Take in User Request and sends back item


def query():
    user_request = "Oranges"
    # print(user_request)
    return user_request


def spider():
    data_in = {
        "name": "Oranges",
        "store": "SuperStore",
        "price": 2.99
    }

    name = "Oranges"
    # print(name)
    # print(data_in)
    return name, data_in


"""
if __name__ == "__main__":
    spider()
"""