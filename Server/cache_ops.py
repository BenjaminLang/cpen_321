import traceback
import calendar
import time

class CacheOps:
    @staticmethod
    def insert_cache(cache_db, query, cat_list):
        if len(cat_list) == 0:
            return

        words = query.split()
        try:
            db_res = list(cache_db['cache'].find({'query': {'$eq': words}}))
            time_res = list(cache_db['cache'].find().sort('time', 1))
            curr = calendar.timegm(time.gmtime())

            data = {}
            data['cats'] = list(cat_list)
            data['query'] = words
            data['time'] = curr
            if len(time_res) < 1000 and len(db_res) == 0:
                cache_db['cache'].insert(data)
            else:
                updated_item = cache_db['cache'].find().sort('time', 1).limit(1)
                updated_item['cats'] = list(cat_list)
                updated_item['query'] = words
                updated_item['time'] = curr
                cache_db['cache'].save(updated_item)
            
        except Exception:
            traceback.print_exc()

    @staticmethod
    def read_cache(cache_db, query):
        words = query.split()
        ret_cat = []
        try:
            db_res = list(cache_db['cache'].find({'query' : {'$all': words}}))

            if len(db_res) == 0:
                return 'Not found'
            else:
                print('cache hit')
                for res in db_res:
                    del res['query']
                    del res['time']
                    del res['_id']
                    ret_cat.extend(res['cats'])

        except Exception:
            traceback.print_exc()
            return 'Error'

        return ret_cat
