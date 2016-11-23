from locust import HttpLocust, TaskSet, task


class UserBehavior(TaskSet):
    def on_start(self):
        """ on_start is called when a Locust start before any task is scheduled """

    @task(1)
    def login(self):
        self.client.post("/login", {"email":"mablibsking@hotmail.com", "password":"CPEN321_ryangroup"})

    @task(1)
    def search(self):
        self.client.get("/item_searched?item=samsung")

    @task(2)
    def search_2(self):
        self.client.get("/item_searched?item=apple")

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    min_wait = 5000
    max_wait = 9000
