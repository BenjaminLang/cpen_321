import traceback

class ItemOps:
    def insert_items(json_data, items_db): 
        try:
            url = msg['data']['url']
            name = msg['data']['name']
            collection = msg['collection']

            if '$' in name: # unimportant items like warranties and coupons
                response['status'] = 'failed'
                return response

            words = name.split()
            msg['words'] = words
            del msg['message_type']
            del msg['collection']
            
            # insert them into the database
            data = list(items_db[collection].find({'data.url': {'$eq': url}}))
            # if you get a valid ID, you know that the item exists, so update
            if len(data) != 0:
                msg['_id'] = data[0]['_id']
                items_db[collection].save(msg)
            # otherwise make a new item
            else:
                items_db[collection].insert(msg)

            return True

        except Exception:
            traceback.print_exc()
            return False
        
    def read_items(json_query, items_db, categories):
        try:
            for cat in categories:
            # set up appropriate indexing information, json_data is a dict
                items = json_data['items']
                results = []
                result = []
                price = json_data['options']['price']
                num = int(json_data['options']['num'])

                item_words = items[0]

                query = {'words': { '$all': items }}

                if item_words is not None:
                    if(price == 'min'):
                        if(num != -1):
                            res_data = list(self.__items_db[cat].find(query).sort('data.price',1).limit(num))
                        else:
                            res_data = list(self.__items_db[cat].find(query).sort('data.price',1).limit(100))

                    elif(price == 'max'):
                        if(num != -1):
                            res_data = list(self.__items_db[cat].find(query).sort('data.price',-1).limit(num))
                        else:
                            res_data = list(self.__items_db[cat].find(query).sort('data.price',-1).limit(100))
                    
                    else:
                        if(num != -1):
                            res_data = list(self.__items_db[cat].find(query).limit(num))
                        else:
                            res_data = list(self.__items_db[cat].find(query).limit(100))

                    if res_data is not None:
                        for res in res_data:
                            del res['_id']
                            del res['words']

                    results.append(res_data)
                response['items'] = results
                
        except Exception:
            traceback.print_exc()
            response['status'] = 'fail'
        return response     
