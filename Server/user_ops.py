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
                users_db[collection].insert(json_query)
            else:
                return False

        except Exception:
            traceback.print_exc()
            return False

        return True

    @staticmethod
    def del_acc(users_db, json_query):
        try:
            email = json_query['email']

            result = users_db[email].delete_many({})

            if result.deleted_count != 0:
                return True
            else:
                return False

        except Exception:
            traceback.print_exc()
            return False

    @staticmethod
    def log_in(users_db, json_query):
        try:
            email = json_query['email']
            collection = email.replace('@', '')
            auth = json_query['password']

            user_data = list(users_db[collection].find())

            if user_data[0]['password'] == auth:
                return True
            else:
                return False

        except Exception:
            traceback.print_exc()
            return False
    """
    @staticmethod
    def update_acc(users_db, json_query):
        try:
            data = []
            email = json_query['email']
            collection = email.replace('@', '')

            data.append(collection)
            data.append(json_query['password'])
            data.append(json_query['name'])
            data.append(json_query['list'])

            db_res = list(users_db[collection].find())

            user_id = db_res[0]['_id']

            user_cat = ['email', 'pass', 'name', 'list']

            message = {}
            message['category'] = 'email'
            message['email'] = email
            message['_id'] = user_id
            users_db[data[0]].save(message)

            for i in range(1, len(data)):
                message = {}
                message['category'] = user_cat[i]
                message[user_cat[i]] = data[i]
                user_id = db_res[i]['_id']
                message['_id'] = user_id
                users_db[data[0]].save(message)

        except Exception:
            traceback.print_exc()
            return False

        return True
    """

    @staticmethod
    def add_list(users_db, json_query):
        try:
            email = json_query['email']
            list_name = json_query['list_name']
            collection = email.replace('@', '')

            user_data = list(users_db[collection].find())[0]
            user_data['list_names'].append(list_name)
            user_data['list'].append(json_query['list'])
            users_db[collection].save(user_data)

        except Exception :
            traceback.print_exc()
            return False
        return True

    @staticmethod
    def retrieve_lists(users_db, json_query):
        try:
            ret_list = []
            email = json_query['email']
            collection = email.replace('@', '')
            list_name = json_query['list_name']

            user_data = list(users_db[collection].find())[0]
            shopping_lists = user_data['list']
            names_list = user_data['list_names']
            list_index = names_list.index(list_name)

            ret_list = shopping_lists[list_index]

        except Exception :
            traceback.print_exc()
            return []
        return ret_list

    @staticmethod
    def delete_list(users_db, json_query):
        try:
            email = json_query['email']
            collection = email.replace('@', '')
            list_name = json_query['list_name']

            user_data = list(users_db[collection].find())[0]
            updated_list = user_data['list']
            updated_names = user_data['list_names']
            list_index = updated_names.index(list_name)
            del updated_names[list_index]
            del updated_list[list_index]

            user_data['list'] = updated_list
            user_data['list_names'] = updated_names
            users_db[collection].save(user_data)

        except Exception :
            traceback.print_exc()
            return False
        return True

