import traceback

class UserOps:
    @staticmethod
    def create_acc(users_db, json_query):
        try:
            data = []
            email = json_query['email']
            collection = email.replace('@', '')

            db_res = list(users_db[collection].find())

            if len(db_res) == 0:
                json_query['list_names'] = []
                json_query['lists'] = []
                users_db[collection].insert(json_query)
                return 'success'
            else:
                return 'failed'

        except Exception:
            traceback.print_exc()
            return 'exception'

    @staticmethod
    def del_acc(users_db, json_query):
        try:
            email = json_query['email']
            collection = email.replace('@', '')

            result = users_db[collection].drop()

            if result is None:
                return 'success'
            else:
                return 'failed'

        except Exception:
            traceback.print_exc()
            return 'exception'

    @staticmethod
    def log_in(users_db, json_query):
        try:
            email = json_query['email']
            collection = email.replace('@', '')
            auth = json_query['password']

            user_data = list(users_db[collection].find())

            if not user_data:
                return ('DNE', '')
            elif user_data[0]['password'] == auth:
                return ('success', user_data[0]['name'])
            else:
                return ('failed', '')

        except Exception:
            traceback.print_exc()
            return ('exception', '')

    @staticmethod
    def get_list_names(users_db, json_query):
        try:
            collection = json_query['email'].replace('@', '')
            user_data = list(users_db[collection].find())[0]

            if len(user_data) == 0:
                return 'failed'
            else:
                return list.sort(user_data['list_names'])

        except Exception :
            traceback.print_exc()
            return 'exception'

    @staticmethod
    def update_acc(users_db, json_query):
        try:
            data = []
            email = json_query['email']
            auth = json_query['old_password']
            collection = email.replace('@', '')

            db_res = list(users_db[collection].find())

            if db_res:
                if db_res[0]['password'] == auth:
                    del json_query['old_password']
                    user_id = db_res[0]['_id']
                    json_query['_id'] = user_id
                    users_db[collection].save(json_query)
                    return 'success'
                else:
                    return 'failed'
            else:
                return 'DNE'

        except Exception:
            traceback.print_exc()
            return 'exception'

    @staticmethod
    def add_list(users_db, json_query):
        try:
            email = json_query['email']
            list_name = json_query['list_name']
            collection = email.replace('@', '')

            user_data = list(users_db[collection].find())[0]
            user_data['list_names'].append(list_name)
            user_data['lists'].append(json_query['list'])
            users_db[collection].save(user_data)
            return 'success'

        except Exception :
            traceback.print_exc()
            return 'exception'

    @staticmethod
    def get_list(users_db, json_query):
        try:
            ret_list = []
            email = json_query['email']
            collection = email.replace('@', '')
            list_name = json_query['list_name']

            user_data = list(users_db[collection].find())[0]
            shopping_lists = user_data['lists']
            names_list = user_data['list_names']
            list_index = names_list.index(list_name)

            ret_list = shopping_lists[list_index]
            return ret_list

        except ValueError:
            return []

        except Exception :
            traceback.print_exc()
            return []

    @staticmethod
    def delete_list(users_db, json_query):
        try:
            email = json_query['email']
            collection = email.replace('@', '')
            list_name = json_query['list_name']

            user_data = list(users_db[collection].find())[0]
            updated_list = user_data['lists']
            updated_names = user_data['list_names']
            list_index = updated_names.index(list_name)
            del updated_names[list_index]
            del updated_list[list_index]

            user_data['lists'] = updated_list
            user_data['list_names'] = updated_names
            users_db[collection].save(user_data)
            return 'success'

        except ValueError:
            return 'failed'

        except Exception :
            traceback.print_exc()
            return 'exception'
