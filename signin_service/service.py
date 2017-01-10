# coding=utf-8
import hashlib
import json
import uuid
import redis
import time
from models import User
import settings
from logging_config import logger
from producer import producer


pool = redis.ConnectionPool(host=settings.REDIS_HOST, port=settings.REDIS_PORT, db=settings.REDIS_DB)
CAPTCHA_FORMAT = "CAPTCHA:LOGIN:{0}"
TOKEN_FORMAT = "TOKEN:{0}"
LOGIN_FAILED_TIMES_FORMAT = 'LOGIN_FAILED_TIMES:{0}'


class SessionManager(object):
    def __init__(self, source='WEB'):
        self.connection = redis.Redis(connection_pool=pool)
        self.expire_seconds = settings.WEB_TOKEN_EXPIRED_SECONDS if source.upper() == 'WEB' else settings.MOBILE_TOKEN_EXPIRED_SECONDS

    def get(self, session_id):
        token_key = TOKEN_FORMAT.format(session_id)
        user_info = self.connection.get(token_key)
        if user_info:
            self.connection.expire(token_key, self.expire_seconds)
            return json.loads(user_info)

    def set(self, data, old_session_id):
        new_token_id = self._generate_token_id()
        token_key = TOKEN_FORMAT.format(new_token_id)
        self.connection.setex(token_key, json.dumps(data), self.expire_seconds)
        old_token_key = TOKEN_FORMAT.format(old_session_id)
        self.connection.delete(old_token_key)
        return new_token_id

    def _generate_token_id(self):
        return uuid.uuid4()

    def create(self):
        return self._generate_token_id()

    def delete(self, session_id):
        user_info = self.get(session_id)
        self.connection.delete(TOKEN_FORMAT.format(session_id))
        return user_info

    def refresh(self, session_id):
        """
        Only used by app
        """
        old_token = TOKEN_FORMAT.format(session_id)
        data = self.connection.get(old_token)
        if data:
            new_token_id = self._generate_token_id()
            new_token = TOKEN_FORMAT.format(new_token_id)
            self.connection.setex(new_token, data, self.expire_seconds)
            self.connection.delete(old_token)
            return new_token_id


class UsernamePasswordError(Exception):
    pass


class UserNotExistedError(Exception):
    pass


class UserBannedError(Exception):
    pass


class LoginManager(object):
    def __init__(self, form, ip_address="127.0.0.1"):
        self.ip_address = ip_address.split(',')[0] if ip_address else None
        self.form = form
        self.connection = redis.Redis(connection_pool=pool)
        self.session_manager = SessionManager(source=self.form.source.data)
        logger.debug("x-forwarded-for:{}".format(ip_address))

    def _is_password_valid(self, user):
        return user and user.password == hashlib.sha1('%s{%s}' % (hashlib.sha1(self.form.password.data).hexdigest(), user.salt)).hexdigest()

    def _load_user(self):
        user = User.query.filter(
            (User.username == self.form.username.data) | (User.mobile == self.form.username.data)).first()
        if not user:
            logger.debug("{} not exist".format(self.form.username.data))
            raise UserNotExistedError()
        return user

    def _success(self, user):
        user_info = {'login_name': user.username, 'mobile': user.mobile, 'roles': [role.role for role in user.roles]}
        new_token_id = self.session_manager.set(user_info, self.form.token.data)
        logger.info("{} login successful. source: {}, token_id: {}, user_info: {}".format(self.form.username.data,
                                                                                          self.form.source.data,
                                                                                          new_token_id, user_info))
        return new_token_id, user_info

    def _increase_failed_times(self):
        login_failed_times_key = LOGIN_FAILED_TIMES_FORMAT.format(self.form.username.data)
        self.connection.incr(login_failed_times_key)
        self.connection.expire(login_failed_times_key, settings.LOGIN_FAILED_EXPIRED_SECONDS)
        logger.info("{} login failed. source: {}".format(self.form.username.data, self.form.source.data))

    def _get_user_info(self):
        user = self._load_user()
        if self._is_password_valid(user):
            return self._success(user)

    def _normal_login(self):
        login_failed_times_key = LOGIN_FAILED_TIMES_FORMAT.format(self.form.username.data)
        failed_times = int(self.connection.get(login_failed_times_key)) if self.connection.get(login_failed_times_key) else 0
        if failed_times >= settings.LOGIN_FAILED_MAXIMAL_TIMES:
            logger.debug("{} have been locked. source: {}".format(self.form.username.data, self.form.source.data))
            raise UserBannedError()

        info = self._get_user_info()
        if not info:
            raise UsernamePasswordError()
        return info

    def _fail_login(self, message, save_log=True):
        if save_log:
            self._save_log(False)
        self._increase_failed_times()
        return self._render(False, message=message)

    def _success_login(self, user_info, token):
        self._save_log(True)
        return self._render(True, user_info=user_info, token=token)

    @staticmethod
    def _render(is_success, message=None, user_info=None, token=None):
        return {'result': is_success, 'message': message, 'user_info': user_info, 'token': token}

    def login(self):
        try:
            token_id, user_info = self._normal_login()
            return self._success_login(user_info, token_id)
        except UsernamePasswordError:
            return self._fail_login(message='用户名或密码错误')
        except UserNotExistedError:
            return self._fail_login(message='用户名或密码错误', save_log=False)
        except UserBannedError:
            return self._fail_login(message='用户已被禁用')

    def no_password_login(self):
        try:
            user = self._load_user()
            token_id, user_info = self._success(user)
            return self._success_login(user_info, token_id)
        except UserNotExistedError:
            return self._fail_login('用户不存在', save_log=False)

    def _save_log(self, is_success):
        login_log = dict(
            loginName=self.form.username.data,
            source=self.form.source.data,
            ip=self.ip_address,
            device=self.form.device_id.data,
            loginTime=int(time.time() * 1000),
            success=is_success
        )
        producer.send_message(json.dumps(login_log))


def active(username):
    login_failed_times_key = LOGIN_FAILED_TIMES_FORMAT.format(username)
    conn = redis.Redis(connection_pool=pool)
    conn.delete(login_failed_times_key)
