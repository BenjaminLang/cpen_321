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

                user_cat = ['email', 'pass', 'name', 'list']

                message = {}
                message['category'] = 'email'
                message['email'] = email
                users_db[data[0]].insert(message)

                for i in range(1, len(data)):
                    message = {}
                    message['category'] = user_cat[i]
                    message[user_cat[i]] = data[i]
                    users_db[data[0]].insert(message)
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
