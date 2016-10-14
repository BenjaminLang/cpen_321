from pymongo import MongoClient

client = MongoClient('localhost', 27017)

db = client.test_database

orange = db.orange

"""new_oranges = [
    {
        "type" : "Mandarin Oranges",
        "store" : "SuperStore",
        "price" : 2.99
    },

    {
        "type" : "Mandarin Oranges",
        "store" : "Save On Foods",
        "price" : 2.50
    }
]"""

orange.save({"type" : "Mandarin Oranges", "store" : "SuperStore", "price" : 2.99})
result = orange.find()
print (result)
