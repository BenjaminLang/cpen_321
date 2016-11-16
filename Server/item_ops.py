import traceback


class ItemOps:
    @staticmethod
    def insert_items(items_db, json_query):
        try:
            url = json_query['data']['url']
            name = json_query['data']['name']
            collection = json_query['collection']

            words = name.split()
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
        results = []
        cat_res = []
        item = json_query['items'][0].split()
        price = json_query['options']['price']
        num = int(json_query['options']['num'])
        try:
            for cat in categories:
            # set up appropriate indexing information, json_data is a dict
                query = {'words': { '$all': item }}

                if(price == 'min'):
                    if(num != -1):
                        res_data = list(items_db[cat].find(query).sort('data.price',1).limit(num))
                    else:
                        res_data = list(items_db[cat].find(query).sort('data.price',1).limit(100))

                elif(price == 'max'):
                    if(num != -1):
                        res_data = list(items_db[cat].find(query).sort('data.price',-1).limit(num))
                    else:
                        res_data = list(items_db[cat].find(query).sort('data.price',-1).limit(100))

                else:
                    if(num != -1):
                        res_data = list(items_db[cat].find(query).limit(num))
                    else:
                        res_data = list(items_db[cat].find(query).limit(100))

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
