# coding=utf-8
import json
import time
import uuid
from datetime import datetime

import redis
from sqlalchemy import func

import settings
from logging_config import logger
from models import User, db, UserRole
from producer import producer

pool = redis.ConnectionPool(host=settings.REDIS_HOST, port=settings.REDIS_PORT, db=settings.REDIS_DB)
CAPTCHA_FORMAT = u"CAPTCHA:LOGIN:{0}"
TOKEN_FORMAT = u"TOKEN:{0}"
LOGIN_FAILED_TIMES_FORMAT = u'LOGIN_FAILED_TIMES:{0}'


class SessionManager(object):
    def __init__(self, source='WEB'):
        self.connection = redis.Redis(connection_pool=pool)
        self.expire_seconds = settings.WEB_TOKEN_EXPIRED_SECONDS if source.upper() == 'WEB' else settings.MOBILE_TOKEN_EXPIRED_SECONDS
        self.source = source

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

    def update(self, data, session_id):
        token_key = TOKEN_FORMAT.format(session_id)
        self.connection.setex(token_key, json.dumps(data), self.expire_seconds)

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
            user_info = json.loads(data)
            update_last_login_time_source(user_info['login_name'], self.source)
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

    def _load_user(self):
        user = User.query.filter(
            (User.login_name == self.form.username.data) | (User.mobile == self.form.username.data)).first()
        if not user:
            logger.debug(u"{} not exist".format(self.form.username.data))
            raise UserNotExistedError()
        return user

    def _success(self, user):
        user_info = {'login_name': user.login_name, 'mobile': user.mobile, 'roles': [role.role for role in user.roles
                                                                                     if role.role != u'LOANER']}

        new_token_id = self.session_manager.set(user_info, self.form.token.data)
        logger.info(u"{} login successful. source: {}, token_id: {}, user_info: {}".format(self.form.username.data,
                                                                                           self.form.source.data,
                                                                                           new_token_id, user_info))
        return new_token_id, user_info

    def _increase_failed_times(self):
        login_failed_times_key = LOGIN_FAILED_TIMES_FORMAT.format(self.form.username.data)
        self.connection.incr(login_failed_times_key)
        self.connection.expire(login_failed_times_key, settings.LOGIN_FAILED_EXPIRED_SECONDS)
        logger.info(u"{} login failed. source: {}".format(self.form.username.data, self.form.source.data))

    def _get_user_info(self):
        user = self._load_user()
        if user and user.validate_password(self.form.password.data):
            return self._success(user)

    def _normal_login(self):
        login_failed_times_key = LOGIN_FAILED_TIMES_FORMAT.format(self.form.username.data)
        failed_times = int(self.connection.get(login_failed_times_key)) if self.connection.get(
            login_failed_times_key) else 0
        if failed_times >= settings.LOGIN_FAILED_MAXIMAL_TIMES:
            logger.debug(u"{} have been locked. source: {}".format(self.form.username.data, self.form.source.data))
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
        update_last_login_time_source(user_info['login_name'], self.form.source.data)
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


def update_last_login_time_source(username, source):
    user = User.query.filter((User.login_name == username)).first()
    user.last_login_time = func.now()
    user.last_login_source = source
    db.session.commit()


def active(username):
    login_failed_times_key = LOGIN_FAILED_TIMES_FORMAT.format(username)
    conn = redis.Redis(connection_pool=pool)
    conn.delete(login_failed_times_key)


def refresh_session_data(session, source):
    session_manager = SessionManager(source=source)
    user_info = session_manager.get(session)
    if user_info is None:
        return

    logger.info("refresh session data {}".format(user_info))
    user = User.query.filter((User.login_name == user_info.get("login_name"))).first()
    user_info = {'login_name': user.login_name, 'mobile': user.mobile, 'roles': [role.role for role in user.roles]}
    session_manager.update(user_info, session)
    return user_info


def switch_role(session, switch_to_role):
    session_manager = SessionManager()
    user_info = session_manager.get(session)
    if user_info is None:
        return

    logger.info("switch user {} to {}".format(user_info.get("login_name"), switch_to_role))
    user = User.query.filter((User.login_name == user_info.get("login_name"))).first()
    if switch_to_role not in [role.role for role in user.roles]:
        return user_info

    user_info = {'login_name': user.login_name,
                 'mobile': user.mobile,
                 'roles': [role.role for role in user.roles
                           if role.role != {'INVESTOR': 'LOANER',
                                            'LOANER': 'INVESTOR'}[switch_to_role]]}
    session_manager.update(user_info, session)
    return user_info


class UserService(object):
    def create(self, form):
        u = User(form.mobile.data, form.referrer.data, form.channel.data, form.source.data)
        u.set_password(form.password.data)
        db.session.add(u)

        ur = UserRole(u.login_name, 'USER')
        db.session.add(ur)
        db.session.commit()
        return u.as_dict()

    def update(self, form):
        user = User.query.filter((User.login_name == form.login_name.data)).first()
        for f in form:
            # 仅当提供了某字段，才修改某字段，否则保持数据不变
            if f.data is not None:
                # 当提供了某字段，但值为空，则清空该字段
                setattr(user, f.name, f.data if f.data else None)
        db.session.commit()
        return user.as_dict()

    def find_by_login_name_or_mobile(self, login_name_or_mobile):
        user = User.query.filter(
            (User.login_name == login_name_or_mobile) | (User.mobile == login_name_or_mobile)).first()
        return user.as_dict() if user else None

    def reset_password(self, form):
        user = User.query.filter(User.login_name == form.login_name.data).first()
        if user:
            user.set_password(form.password.data)
            db.session.commit()
            return user.as_dict()
        else:
            raise UserNotExistedError()

    def change_password(self, form):
        user = User.query.filter(User.login_name == form.login_name.data).first()
        if user:
            if user.validate_password(form.ori_password.data):
                user.set_password(form.password.data)
                db.session.commit()
                return user.as_dict()
            else:
                raise UsernamePasswordError()
        else:
            raise UserNotExistedError()

    def query(self, form):

        default_select_fields = ['login_name', 'mobile', 'email', 'user_name', 'identity_number', 'register_time',
                                 'referrer', 'status', 'channel', 'province', 'city', 'source']

        def _build_query_where(_qs):
            if form.email.data:
                _qs = _qs.filter(User.email == form.email.data)
            if form.status.data:
                _qs = _qs.filter(User.status == form.status.data)
            if form.referrer.data:
                _qs = _qs.filter(User.referrer == form.referrer.data)
            if form.identity_number.data:
                _qs = _qs.filter(User.identity_number == form.identity_number.data)
            if form.channels.data:
                _qs = _qs.filter(User.channel.in_(form.channels.data))
            if form.mobile__like.data:
                _qs = _qs.filter(User.mobile.like('{}%'.format(form.mobile__like.data)))
            if form.register_time__gte.data:
                _qs = _qs.filter(User.register_time >= form.register_time__gte.data)
            if form.register_time__lte.data:
                _qs = _qs.filter(User.register_time <= form.register_time__lte.data)
            if form.referrer__hasvalue.data:
                if 'true' == form.referrer__hasvalue.data.lower():
                    _qs = _qs.filter(User.referrer.isnot(None)).filter(User.referrer != '')
                else:
                    _qs = _qs.filter((User.referrer == '') | (User.referrer.is_(None)))

            if form.role.data:
                _qs = _qs.join(UserRole).filter(UserRole.role == form.role.data)
            return _qs

        def _build_query_sort(_qs):
            if form.sort.data:
                _qs = _qs.order_by(*[__build_order_by_criterion(fn) for fn in form.sort.data])
            return _qs

        def _build_query_select(_qs):
            select_fields = form.fields.data if form.fields.data else default_select_fields
            return _qs.with_entities(*[User.lookup_field(fn) for fn in select_fields])

        def _query_with_pagination(_qs):
            if form.page_size.data:
                pagination = _qs.paginate(form.page.data, form.page_size.data, error_out=False)
                return {
                    'total_count': pagination.total,
                    'page': pagination.page,
                    'page_size': pagination.per_page,
                    'has_prev': pagination.has_prev,
                    'has_next': pagination.has_next,
                    'items': __generate_result(pagination.items)
                }
            else:
                _rows = _qs.all()
                return {
                    'total_count': len(_rows),
                    'page': 1,
                    'page_size': len(_rows),
                    'has_prev': False,
                    'has_next': False,
                    'items': __generate_result(_rows)
                }

        def __build_order_by_criterion(django_like_order_by):
            if django_like_order_by.startswith('-'):
                return User.lookup_field(django_like_order_by[1:]).desc()
            else:
                return User.lookup_field(django_like_order_by)

        def __generate_result(_data):
            select_fields = form.fields.data if form.fields.data else default_select_fields
            formatted_data = map(__fmt_row, _data)
            return [dict(zip(select_fields, row)) for row in formatted_data]

        def __fmt_row(user_item):
            return map(__fmt_cell, user_item)

        def __fmt_cell(value):
            if isinstance(value, datetime):
                return value.strftime('%Y-%m-%d %H:%M:%S')
            return value

        qs = _build_query_where(User.query)
        qs = _build_query_sort(qs)
        qs = _build_query_select(qs)
        return _query_with_pagination(qs)

    def empty_province_users(self, limit=10):
        # NOTICE `User.province == None` SHOULD NOT be refactor to `User.province is None`
        qs = User.query.filter((User.province == None) | (User.province == '')).limit(limit)
        return {'items': [u.as_dict() for u in qs.all()]}
