from our_threads import MyThread


def main():
    thread_stack = list()
    thread_stack.append(MyThread('server'))
    thread_stack[0].start()

    thread_stack.append(MyThread('crawler'))
    thread_stack[1].start()

    # thread_stack.append(MyThread('send'))
    # thread_stack[2].start()

if __name__ == "__main__":
    main()

