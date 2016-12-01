import traceback

class ItemOps:
    @staticmethod
    def insert_items(items_db, json_query):
        try:
            url = json_query['data']['url']
            name = json_query['data']['name']
            collection = json_query['collection']

            words = [x.lower().replace(',', '') for x in name.split()]
            json_query['words'] = words
            del json_query['message_type']
            # del json_query['collection']

            # insert them into the database
            data = list(items_db[collection].find({'data.url': {'$eq': url}}))
            # if you get a valid ID, you know that the item exists, so update
            if len(data) != 0:
                json_query['_id'] = data[0]['_id']
                items_db[collection].save(json_query)
            # otherwise make a new item
            else:
                items_db[collection].insert(json_query)

        except Exception:
            traceback.print_exc()
            return False

        return True

    @staticmethod
    def read_items(items_db, json_query, categories):
        categories.sort()
        results = []
        cat_res = []
        item = [x.lower().replace(',', '') for x in json_query['items'][0].split()]
        store_list = json_query['options']['store']

        list.sort(categories)
        try:
            for cat in categories:
            # set up approp'riate indexing information, json_data is a dict
                words_query = {'words': { '$all': item }}
                if store_list:
                    store_query = []
                    for store in store_list:
                        store_query.append({'data.store' : {'$eq' : store}})

                    query = {'$and' : [words_query, {'$or' : store_query}]}
                    res_data = list(items_db[cat].find(query))
                else:
                    res_data = list(items_db[cat].find(words_query))

                if res_data is not None:
                    for res in res_data:
                        del res['_id']
                        del res['words']
                        cat_res.append(res['collection'])

                results.append(res_data)
                
        except Exception:
            traceback.print_exc()
            return None

        return results, set(cat_res)
