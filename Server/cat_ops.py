import traceback

class CatOps:
    @staticmethod
    def insert_cat(cat_db, collection):
        try:
            msg = {}
            msg['department'] = collection
            data = list(cat_db['categories'].find({'department': {'$eq': collection}}))
            # if you get a valid ID, you know that the item exists, so update
            if len(data) == 0:
                cat_db['categories'].insert(msg)
        
            return True
            
        except Exception: 
            traceback.print_exc()
            return False

    @staticmethod
    def return_categories(cat_db):
        db_result = list(cat_db['categories'].find())
        
        categories = []
        
        for cat in db_result:
            categories.append(cat['department'])
        
        return categories 
