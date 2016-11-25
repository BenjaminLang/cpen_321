import traceback
import calendar
import time


class CacheOps:
    @staticmethod
    def insert_cache(cache_db, query, cat_list):
        words = sorted(query.split())
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
                if len(time_res) >= 1000:
                    updated_item = list(cache_db['cache'].find().sort('time', 1).limit(1))[0]
                    updated_item['cats'] = list(cat_list)
                    updated_item['query'] = words
                    updated_item['time'] = curr
                    cache_db['cache'].save(updated_item)
                else:
                    db_res[0]['time'] = curr
                    cache_db['cache'].save(db_res[0])
            
        except Exception:
            traceback.print_exc()

    @staticmethod
    def read_cache(cache_db, query):
        words = sorted(query.split())
        ret_cat = []
        try:
            db_res = list(cache_db['cache'].find({'query' : {'$eq': words}}))

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
