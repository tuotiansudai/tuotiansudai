# -*- coding: utf-8 -*-
import datetime
import random
from flask import Flask, request, abort
from flask.templating import render_template
from markupsafe import Markup
from tests.ump_service.constants import WEB_HOST
from tests.ump_service.store_helper import Store

app = Flask(__name__)


def get_random_user_id():
    return random.Random().randint(100000000000000, 999999999999999)


def build_common_params():
    common_ret = {'mer_id': request.values.get('mer_id'),
                  'sign_type': 'RSA',
                  'version': '1.0',
                  'sign': 'qCq9+6lxcCE06rPF679LT+qxfs5O91w3iQOy2bilph55iChxp2hEbxAgQ8S4ZQYV5GcWbbKpDd9eNW863rN4tG3uOAJA63aNd9J+bVO9EBob5mqxv9S4tqV/Pq8UE+7GoF/DCZXbojt3UDcDbUllQ+V+xGMe1RwKpFVH6AHY5p0='}
    return common_ret


def build_register_params():
    register_ret = {'reg_date': datetime.datetime.today().date().strftime('%Y%m%d'), 'user_id': get_random_user_id(),
                    'ret_code': '0000', 'account_id': get_random_user_id()}
    return register_ret


def build_transfer_params():
    transfer_ret = {'mer_date': datetime.datetime.today().date().strftime('%Y%m%d'), 'ret_code': '0000',
                    'order_id': request.values.get('order_id'), 'trade_no': get_random_user_id()}
    return transfer_ret


def format_result(params_dict):
    return reduce(lambda ret, item: '{0}={1}&{2}'.format(item[0], item[1], ret), params_dict.items(), '')[:-1]


def mer_register_person():
    """
    http://pay.soopay.net/spay/pay/payservice.do?charset=UTF-8&identity_code=WehHC8aOTEtMzi1aQVPDuH6XwZ5FYeAR9EtnzUCg%2B9MjxIZpubwOBqC7naiT5chexvG1PiOkHe53x8FB3nAvy%2FOnpKzKMXBV8nbB72NjRZTkYPf3TJKkAmEZ1lLaoOWUx%2FXbPV%2FLvfjyXKAdVyOkFAkFy3dwBedntwjS2Pfv0gU%3D&identity_type=IDENTITY_CARD&mer_cust_id=7099088&mer_cust_name=Ocj20DX0lBBupUGjm5GeJyS17NQGQ9fyT7jG%2BYrPj0xd3w2hTqxXtldSlFZ%2FNV1jjt%2B8e7b0K6wwuXdRQ451tmWlysXopjhXoQoo5MrBNBUqm%2BdHI5J8QO9QxQ4c8V736sn%2BwRHhywvH%2FPZwhBTU3WLpwjrtfupDSTONTMJbun4%3D&mer_id=7099088&mobile_id=13699248263&res_format=HTML&service=mer_register_person&sign_type=RSA&version=1.0&sign=qCq9%2B6lxcCE06rPF679LT%2Bqxfs5O91w3iQOy2bilph55iChxp2hEbxAgQ8S4ZQYV5GcWbbKpDd9eNW863rN4tG3uOAJA63aNd9J%2BbVO9EBob5mqxv9S4tqV%2FPq8UE%2B7GoF%2FDCZXbojt3UDcDbUllQ%2BV%2BxGMe1RwKpFVH6AHY5p0%3D

    :return:
    <html>
      <head>
        <META NAME="MobilePayPlatform" CONTENT="sign=qCq9+6lxcCE06rPF679LT+qxfs5O91w3iQOy2bilph55iChxp2hEbxAgQ8S4ZQYV5GcWbbKpDd9eNW863rN4tG3uOAJA63aNd9J+bVO9EBob5mqxv9S4tqV/Pq8UE+7GoF/DCZXbojt3UDcDbUllQ+V+xGMe1RwKpFVH6AHY5p0=&sign_type=RSA&account_id=249794341333482&user_id=869222383953483&version=1.0&mer_id=7099088&reg_date=20150430&">
      </head>
      <body>
      </body>
    </html>
    """
    common_params = build_common_params()
    register_ret = build_register_params()
    register_ret.update(common_params)
    result = format_result(register_ret)
    return result


def mer_bind_card():
    """
    http://pay.soopay.net/spay/pay/payservice.do?account_name=dFW7mviI0wD5CZcNQOqkC3w4DA24nF9WNUHu3PGziYwtYG5n3wuNEjzQCQsr6pZs4VPbb5FwVljPfGsY2ziuHI4b%2F5GuVYQ7MhfLy1TBd%2FIGIq8tHsojE3TtjN7QWoqGlfi2GjwQSv7sgyfmFOUPAx68Nm47NlujHLbhtwSh%2BfA%3D&apply_notify_flag=0&card_id=RSkidX4tPpDTtO9BShePcTGGdb8ih6Pw4FIduY3GlC5cdbjvdv6EaG1L%2BJYyN4pKhLG5MDcwc2beC3bfET%2Bjb7rx125FVswB%2F8RatenMuyjKM4TfiCacnCEg4R4KNTGuZXt2G4o9n9vu2CUfEFg%2BADHWGc12yIAQJ68TVwPe1P0%3D&charset=UTF-8&identity_code=YItqH3o7zJd531qv802XOn47UJKALmecd84zJ3qfDe1jMNpDWV0jyvuazMa4M7180kAO1FjKxKEEvg5Y6Laa0gKmSFLDmSspCsu0oQqKu6Nrojz4N3WyspMCKFZgXyEu6tfu3n%2FIjyFmzC89gdpkaPh1h2usoB43laggPF%2Be70k%3D&identity_type=IDENTITY_CARD&is_open_fastPayment=0&mer_date=20150430&mer_id=7099088&notify_url=http%3A%2F%2Fbaidu1.com&order_id=302509&res_format=HTML&ret_url=http%3A%2F%2Fbaidu.com&service=mer_bind_card&sign_type=RSA&user_id=UB201504161125570000000003661793&version=1.0&sign=VfvDAlA8V5QqkZh9xuRSE0eJDhoTcp2htx6FSL9YX8ehpBbT7hsUD2Iu6Qe3Du71V%2FnIwOTZbHQbdtU0vPvfKR5P65QpZhdxbpzSisTJHzaQqI%2BAXH4h5bMsLN0uMo5xpFbydiPQRAA7L78Gl8y752A%2BdmnSZ0Y9CAv9leeYlvM%3D
    :return:
        Navigate to UMP page
    """
    user_id = request.values.get('user_id')
    order_id = request.values.get('order_id')
    mer_date = request.values.get('mer_date')
    mer_id = request.values.get('mer_id')
    ret_url = request.values.get('ret_url')
    notify_url = request.values.get('notify_url')

    store = Store(user_id)
    params = "mer_bind_card::{0}::{1}::{2}::{3}".format(user_id, order_id, mer_date, mer_id)
    store.set_frontend_notify("{0}::{1}".format(params, ret_url))
    store.set_backend_notify("{0}::{1}".format(params, notify_url))


def project_transfer():
    """
    http://pay.soopay.net/spay/pay/payservice.do?amount=12345&charset=UTF-8&mer_date=20150512&mer_id=7099088&notify_url=http%3A%2F%2Fbaidu1.com&order_id=173099&partic_acc_type=01&partic_type=01&partic_user_id=UB201504161125570000000003661793&project_account_id=4567889&project_id=123&res_format=HTML&ret_url=http%3A%2F%2Fbaidu.com&serv_type=03&service=project_transfer&sign_type=RSA&trans_action=02&version=1.0&sign=Uh9YaEXA4tqE2ZejQGuPzWGdMWXi%2Bb%2BjbS2TLwQdCxM3oHMCX48Q8MiB4zWMjIBvU2Yy2JVg0WIoQrfT4%2FJ3Df%2BY3Zi0OfEV6byMTmvDGCarHulbixIxpwLkhn4AscOom0kW8vTkmXt5NLcL3fNXrYpRzuIDxxwk%2BmwOqm8vHks%3D
    :return:
        Navigate to UMP page
    """
    order_id = request.values.get('order_id')
    mer_date = request.values.get('mer_date')
    mer_id = request.values.get('mer_id')
    ret_url = request.values.get('ret_url')
    notify_url = request.values.get('notify_url')

    store = Store()
    params = "project_transfer::{0}::{1}::{2}".format(order_id, mer_date, mer_id)
    store.set_frontend_notify("{0}::{1}".format(params, ret_url))
    store.set_backend_notify("{0}::{1}".format(params, notify_url))


def transfer():
    """
    4.4.10	普通转账（非标的转账）免密接口(商户→平台)
    http://pay.soopay.net/spay/pay/payservice.do?amount=12345&charset=UTF-8&mer_date=20150511&mer_id=7099088&notify_url=http%3A%2F%2Fbaidu1.com&order_id=173099&partic_acc_type=01&partic_account_id=02000155741809&partic_user_id=UB201504161125570000000003661793&res_format=HTML&service=transfer&sign_type=RSA&trans_action=01&version=1.0&sign=hb8tZij3AwQ12amgSb01idpzhvsjpnK6YO9%2BnoRxz8ctN%2FbpkwAU3qOCE8w8jOyYJJhMjq13ovpUw8fI9v2PhCbxs0PuiMqVcnzD5i%2B%2BbBGmq%2FY8dh2qqDVfXB1YQbZqiZj0TtaDQNjgDQuSFgUXOpm3z8aPJ8hwjVIPwcrO7VU%3D
    :return:
        <html>
          <head>
            <META NAME="MobilePayPlatform" CONTENT="mer_id=7099088&ret_code=00060700&ret_msg=转账方向[trans_action]与账户类型[partic_acc_type]不匹配&sign_type=RSA&version=1.0&sign=IG1k6CLALmAZxc0+uL37gUm8ixel0MCFlj5fpR/ounQHhF28HxPdsk+AD0RqbONnyJ3O6BE2i1DItuPp9LvrHQBYLaBGGE0b28bTk0DRn+pYKE+tYB7HufEEHP196teolfgAvoAUkksjrn2FCQWgw9+R1Ok8HxcYMSWmbEg99Rw=">
          </head>
          <body>
          </body>
        </html>
    """
    common_params = build_common_params()
    transfer_ret = build_transfer_params()
    transfer_ret.update(common_params)
    result = format_result(transfer_ret)
    return result


@app.route('/spay/pay/payservice.do', methods=['GET', 'POST'])
def index():
    service = globals().get(request.values.get('service'))
    if not service:
        abort(404)
    result = service()
    return render_template('response.html', context=Markup(result))


if __name__ == '__main__':
    app.debug = True
    app.run(host=WEB_HOST)