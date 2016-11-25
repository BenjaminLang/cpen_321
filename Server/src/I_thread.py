from abc import ABCMeta, abstractmethod

class IThread(metaclass=ABCMeta):
    @abstractmethod
    def run(self):
        pass
