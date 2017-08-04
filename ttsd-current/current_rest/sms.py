# coding: utf-8
import datetime
import hashlib
import json
import time
import urllib
import urllib2
import uuid

from django.conf import settings

from current_rest import redis_client
from serializers import SmsLogSerializer

_config = settings.SMS_CONFIG
_url = _config['url']
_app_key = _config['app_key']
_app_secret = _config['app_secret']
_send_interval = _config['interval_seconds']

SMS_TEMPLATES = {
    'calculateInterest': {
        'id': 1111111,
        'text': u'【拓天速贷】{0}日的利息计算失败，请及时处理'
    },
    'loanMatch': {
        'id': 2222222,
        'text': u'【拓天速贷】{}日的债权匹配失败，请及时处理'
    }
}


def _send_template_sms(ip, template, mobile, params):
    redis_check_key = None
    if ip is not None:
        redis_check_key = redis_client.get_sms_send_check_key(ip)
        if redis_client.exists(redis_check_key):
            return False

    nonce = uuid.uuid4().hex
    cur_time = str(int(time.time()))
    check_sum = hashlib.sha1(_app_secret + nonce + cur_time).hexdigest()

    request = urllib2.Request(
        _url,
        headers={
            'AppKey': _app_key,
            'Nonce': nonce,
            'CurTime': cur_time,
            'CheckSum': check_sum,
            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
        },
        data=urllib.urlencode({
            'templateid': template['id'],
            'mobiles': [mobile],
            'params': json.dumps(params)
        })
    )

    content = template['text'].format(*params)
    sms_log = _sms_send_log(mobile, content)

    if settings.PRODUCT:
        result = urllib2.urlopen(request).read()
    else:
        result = 'fake result'

    if redis_check_key is not None:
        redis_client.setex(redis_check_key, _send_interval, "1")

    sms_log.result = result
    sms_log.save()

    return True


def _sms_send_log(mobile, content):
    serializer = SmsLogSerializer(data={
        'mobile': mobile,
        'content': content,
        'create_time': datetime.datetime.now()
    })
    serializer.is_valid(raise_exception=True)
    return serializer.save()


def send_calculate_Interest_info(mobile, verify_code, ip):
    template = SMS_TEMPLATES['calculateInterest']
    params = [verify_code]
    return _send_template_sms(ip, template, str(mobile), params)


def send_loan_match_info(mobile, init_password, ip):
    template = SMS_TEMPLATES['loanMatch']
    params = [init_password]
    return _send_template_sms(ip, template, str(mobile), params)
