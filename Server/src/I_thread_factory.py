from abc import ABCMeta, abstractmethod

class IThreadFactory(metaclass=ABCMeta):
    @abstractmethod
    def create_thread(self, thread_type):
        pass
