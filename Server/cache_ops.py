import traceback
import datetime

class CacheOps:
    @staticmethod
    def insert_cache(cache_db, query, cat_list):
        words = set(query.split())
        try:
            for cat in cat_list:
                db_res = list(cache_db['cache'].find({'query': {'$eq': words}}))
                time_res = list(cache_db['cache'].find().sort('time', 1))
                curr = datetime.date.today()

                data = {}
                data['cat'] = cat
                data['query'] = query
                data['time'] = curr
                if len(time_res) < 1000 and len(db_res) == 0:
                    cache_db['cache'].insert(data)
                else:
                    updated_item = cache_db['cache'].find().sort('time', 1).limit(1)
                    updated_item['cat'] = cat
                    updated_item['query'] = query
                    updated_item['time'] = curr
                    cache_db['cache'].save(updated_item)
            
        except Exception:
            traceback.print_exc()

    @staticmethod
    def read_cache(cache_db, query):
        words = set(query.split())
        try:
            db_res = list(cache_db['cache'].find({'query' : {'$eq' : words }}))

            if len(db_res) == 0:
                return 'Not found'
            else:
                for res in db_res:
                    del res['query']
                    del res['time']
                return db_res
        
        except Exception:
            traceback.print_exc()
            return 'Error'