from our_threads import myThread
import server

def main():
    thread_stack = list()
    thread_stack.append(myThread('server'))
    thread_stack[0].start()
    thread_stack.append(myThread('crawler'))
    thread_stack[1].start()

if __name__ == "__main__":
    main()

