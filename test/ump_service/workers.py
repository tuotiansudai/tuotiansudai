import inspect
import logging
import random
from time import sleep
import sys
import requests
from test.ump_service.store_helper import Store

logger = logging.getLogger(__name__)
store = Store()


def get_random_trad_id():
    return random.Random().randint(100000000000000, 999999999999999)


def _process(order_id=None, mer_date=None, mer_id=None, url=None, special_payload={}):
    common_payload = {'sign_type': 'RSA',
                      'sign': 'VfvDAlA8V5QqkZh9xuRSE0eJDhoTcp2htx6FSL9YX8ehpBbT7hsUD2Iu6Qe3Du71V/nIwOTZbHQbdtU0vPvfKR5P65QpZhdxbpzSisTJHzaQqI+AXH4h5bMsLN0uMo5xpFbydiPQRAA7L78Gl8y752A+dmnSZ0Y9CAv9leeYlvM=',
                      'charset': 'UTF-8', 'mer_id': mer_id, 'version': '1.0',
                      'order_id': order_id, 'mer_date': mer_date}

    special_payload.update(common_payload)
    response = requests.get(url, params=special_payload)
    logger.info("request url:{0}\nstatus:{1}".format(response.url, response.status_code))
    return response


def mer_bind_card_apply_notify(user_id, order_id, mer_date, mer_id, url):
    special_payload = {'user_id': user_id, 'ret_code': '0000',
                       'service': 'mer_bind_card_apply_notify'}
    return _process(order_id, mer_date, mer_id, url, special_payload)


def mer_bind_card_notify(user_id, order_id, mer_date, mer_id, url):
    special_payload = {'user_id': user_id, 'ret_code': '0000',
                       'service': 'mer_bind_card_notify', 'gate_id':'ICBC'}
    return _process(order_id, mer_date, mer_id, url, special_payload)


def mer_recharge_person(order_id, mer_date, mer_id, url):
    special_payload = {'ret_code': '0000', 'service': 'recharge_notify'}
    return _process(order_id, mer_date, mer_id, url, special_payload)


def ptp_mer_replace_card(user_id, order_id, mer_date, mer_id, url):
    return mer_bind_card(user_id, order_id, mer_date, mer_id, url)


def ptp_mer_bind_agreement(user_id, mer_id, user_bind_agreement_list, url):
    special_payload = {'ret_code': '0000', 'user_id': user_id,
                       'service': 'mer_bind_agreement_notify', 'user_bind_agreement_list': user_bind_agreement_list}
    return _process(mer_id=mer_id, special_payload=special_payload, url=url)


def project_transfer(order_id, mer_date, mer_id, url):
    special_payload = {'trade_no': get_random_trad_id(), 'ret_code': '0000',
                       'mer_check_date': mer_date, 'service': 'project_transfer_notify'}
    return _process(order_id, mer_date, mer_id, url, special_payload)


def withdraw_apply_notify(order_id, mer_date, mer_id, amount, url):
    special_payload = {'trade_no': get_random_trad_id(), 'ret_code': '0000', 'amount': amount,
                       'mer_check_date': mer_date, 'service': 'withdraw_apply_notify'}
    return _process(order_id, mer_date, mer_id, url, special_payload)


def withdraw_apply_final(order_id, mer_date, mer_id, amount, url):
    special_payload = {'trade_no': get_random_trad_id(), 'ret_code': '0000', 'amount': amount, 'trade_state': 4,
                       'mer_check_date': mer_date}
    return _process(order_id, mer_date, mer_id, url, special_payload)


def queue_daemon(channel_name):
    logger.info("Start to listen on {0}".format(channel_name))
    while True:
        msg = store.wait_until_new_msg(channel_name)
        logger.info("Get new message: %s", msg)
        params = msg.split('::')
        sleep(3)
        func_maps = dict(inspect.getmembers(sys.modules[__name__], inspect.isfunction))
        logger.info('All support functions are:%s\nTarget Method:%s', func_maps, params[0])
        try:
            func_maps.get(params[0])(*params[1:])
        except Exception:
            logger.exception("error")

if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser(description='Fake UMP Worker daemon')
    parser.add_argument('channel', choices=('ret_urls', 'notify_urls'), help='url channel')
    args = parser.parse_args()
    queue_daemon(args.channel)