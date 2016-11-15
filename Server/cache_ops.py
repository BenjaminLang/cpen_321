import traceback
import datetime

class CacheOps:
    def insert_cache(cache_db, query, cat):
        words = set(query.split())
        try:
            db_res = list(cache_db['cache'].find({'query': {'$eq': words}})
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
                updated_item['time'] curr
                cache_db['cache'].save(updated_item)
            
        except Exception:
            traceback.print_exc()

    def read_cache(cache_db, query):
        words = set(query.split())
        try:
            db_res = cache_db['cache'].find_one({'query' : {'$eq' : words }})
            
            if db_res is not None:
                return db_res['cat']
            else:
                return 'Not found'
        
        except Exception:
            traceback.print_exc()
            return 'Error'
