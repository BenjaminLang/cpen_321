from our_threads import MyThread


def main():
    thread_stack = list()
    thread_stack.append(MyThread('server'))
    thread_stack[0].start()

if __name__ == "__main__":
    main()

