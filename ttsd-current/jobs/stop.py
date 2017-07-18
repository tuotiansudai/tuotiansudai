import argparse
import importlib
import inspect
import os
from time import sleep

import settings
from jobs import redis_conn
from jobs.base import BaseTask

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Stop current task queues', usage='python stop task1 task2...')
    parser.add_argument('tasks', nargs='+',
                        help='task names which need to stop')

    args = parser.parse_args()
    for task_name in args.tasks:
        module = importlib.import_module("jobs.{}".format(task_name))
        for name, obj in inspect.getmembers(module):
            if inspect.isclass(obj) and issubclass(obj, BaseTask) and obj.name != 'currentBase':
                redis_conn.sadd(settings.STOP_QUEUE_NAME, obj.name)

    for task_name in args.tasks:
        print "Stopping '{}' task process".format(task_name)
        while redis_conn.sismember(settings.STOP_QUEUE_NAME, task_name):
            sleep(1)
        os.system('pkill -f {}'.format(task_name))
        print "'{}' has been stopped".format(task_name)
