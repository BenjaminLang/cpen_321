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
                data.append(collection)
                data.append(json_query['password'])
                data.append(json_query['name'])
                data.append(json_query['list'])
                data.append(json_query['list_names'])

                user_cat = ['email', 'pass', 'name', 'list', 'list_names']

                message = {}
                message['category'] = 'email'
                message['email'] = email
                users_db[data[0]].insert(message)

                for i in range(1, len(data)):
                    message = {}
                    message['category'] = user_cat[i]
                    message[user_cat[i]] = data[i]
                users_db[collection].insert(message)

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

            user_data = list(users_db[collection].find({'category': 'pass'}))

            if user_data[0]['pass'] == auth:
                return True
            else:
                return False

        except Exception:
            traceback.print_exc()
            return False

    @staticmethod
    def add_list(users_db, json_query):
        try:
            email = json_query['email']
            list_name = json_query['list_name']
            collection = email.replace('@', '')


            user_data = list(users_db[collection].find())[0]
            user_data['list'].append(json_query['list'])
            users_db[collection].save(user_data)

        except Exception :
            return False
        return True

    @staticmethod
    def retrieve_lists(users_db, json_query):
        try:
            email = json_query['email']
            collection = email.replace('@', '')
            retrievals = int(json_query['retrievals'])
            user_data = list(users_db[collection].find())[0]

            shopping_lists = user_data['list']
            list_length = len(shopping_lists)
            ret_lists = []
            for i in range(0,retrievals):
                if list_length - i < 0:
                    break
                ret_lists.append(shopping_lists[list_length - i])

        except Exception :
            return False

        return ret_lists

    @staticmethod
    def delete_list(users_db, json_query):

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
