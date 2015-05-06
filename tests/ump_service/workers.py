import inspect
import logging
from time import sleep
import sys
import requests
from tests.ump_service.store_helper import Store

logger = logging.getLogger(__name__)
store = Store()


def mer_bind_card(user_id, order_id, mer_date, url):
    print user_id, order_id, mer_date, url
    common_payload = {'sign_type': 'RSA',
                      'sign': 'VfvDAlA8V5QqkZh9xuRSE0eJDhoTcp2htx6FSL9YX8ehpBbT7hsUD2Iu6Qe3Du71V/nIwOTZbHQbdtU0vPvfKR5P65QpZhdxbpzSisTJHzaQqI+AXH4h5bMsLN0uMo5xpFbydiPQRAA7L78Gl8y752A+dmnSZ0Y9CAv9leeYlvM=',
                      'charset': 'UTF-8', 'mer_id': 7001088, 'version': '1.0'}
    special_payload = {'user_id': user_id, 'order_id': order_id, 'mer_date': mer_date, 'ret_code': '0000',
                       'service': 'mer_bind_card_apply_notify'}
    special_payload.update(common_payload)
    response = requests.get(url, params=special_payload)
    logger.info("request url:{0}\nstatus:{1}".format(response.url, response.status_code))


def queue_daemon(channel_name):
    logger.info("Start to listen on {0}".format(channel_name))
    while True:
        msg = store.wait_until_new_msg(channel_name)
        logger.info("Get new message: %s", msg)
        params = msg.split('::')
        sleep(3)
        func_maps = dict(inspect.getmembers(sys.modules[__name__], inspect.isfunction))
        logger.info('all support functions are:%s', func_maps)
        func_maps.get(params[0])(*params[1:])

if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser(description='Fake UMP Worker daemon')
    parser.add_argument('channel', choices=('ret_urls', 'notify_urls'), help='url channel')
    args = parser.parse_args()
    queue_daemon(args.channel)