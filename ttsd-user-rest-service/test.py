# coding=utf-8
import json
from datetime import datetime, timedelta
from unittest import TestCase, main

import redis

import web
from models import User, db, UserRole
from service import SessionManager, TOKEN_FORMAT
from settings import REDIS_HOST, REDIS_PORT, REDIS_DB, WEB_TOKEN_EXPIRED_SECONDS, MOBILE_TOKEN_EXPIRED_SECONDS, \
    LOGIN_FAILED_MAXIMAL_TIMES


class TestSessionManager(TestCase):
    def setUp(self):
        self.connection = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)
        self.connection.flushdb()

    def test_should_set_data_with_new_token_and_remove_old_token(self):
        manager = SessionManager(source='WEB')
        old_token_id = manager.create()
        old_token = TOKEN_FORMAT.format(old_token_id)
        self.connection.set(old_token, "{}")
        new_data = {'data': 'new_data'}
        new_token_id = manager.set(new_data, old_token_id)

        self.assertFalse(self.connection.exists(old_token))
        self.assertIsNotNone(new_token_id)
        self.assertEqual(new_data, manager.get(new_token_id))

    def test_should_generate_new_token_with_WEB_source(self):
        manager = SessionManager(source='WEB')
        old_token_id = manager.create()
        old_token = TOKEN_FORMAT.format(old_token_id)
        self.connection.set(old_token, "{}")
        new_data = {'data': 'new_data'}
        new_token_id = manager.set(new_data, old_token_id)
        new_token = TOKEN_FORMAT.format(new_token_id)
        new_token_ttl = self.connection.ttl(new_token)
        self.assertEqual(new_token_ttl, WEB_TOKEN_EXPIRED_SECONDS)

    def test_should_generate_new_token_with_IOS_source(self):
        manager = SessionManager(source='IOS')
        old_token_id = manager.create()
        old_token = TOKEN_FORMAT.format(old_token_id)
        self.connection.set(old_token, "{}")
        new_data = {'data': 'new_data'}
        new_token_id = manager.set(new_data, old_token_id)
        new_token = TOKEN_FORMAT.format(new_token_id)
        new_token_ttl = self.connection.ttl(new_token)
        self.assertEqual(new_token_ttl, MOBILE_TOKEN_EXPIRED_SECONDS)

    def test_should_generate_new_token_with_ANDROID_source(self):
        manager = SessionManager(source='android')
        old_token_id = manager.create()
        old_token = TOKEN_FORMAT.format(old_token_id)
        self.connection.set(old_token, "{}")
        new_data = {'data': 'new_data'}
        new_token_id = manager.set(new_data, old_token_id)
        new_token = TOKEN_FORMAT.format(new_token_id)
        new_token_ttl = self.connection.ttl(new_token)
        self.assertEqual(new_token_ttl, MOBILE_TOKEN_EXPIRED_SECONDS)

    def test_replace_should_generate_new_token(self):
        manager = SessionManager(source='IOS')
        data = {'data': 'test data', 'login_name': 'sidneygao'}
        token_id = manager.set(data, 'fake_session_id')
        new_token_id = manager.refresh(token_id)
        self.assertEqual(data, manager.get(new_token_id))
        self.assertIsNone(manager.get(token_id))

    def test_replace_should_return_none_given_session_id_not_exist(self):
        manager = SessionManager(source='android')
        new_token_id = manager.refresh("not_exist_session_id")
        self.assertIsNone(new_token_id)

    def test_refresh_should_update_last_login_time_source(self):
        manager = SessionManager(source='IOS')
        user_name = 'sidneygao'
        data = {'data': 'test data', 'login_name': user_name}
        token_id = manager.set(data, 'fake_session_id')
        manager.refresh(token_id)
        user = User.query.filter(User.login_name == user_name).first()
        self.assertIsNotNone(user.last_login_time)
        self.assertEqual(user.last_login_source, "IOS")


class TestView(TestCase):
    def setUp(self):
        web.app.config['TESTING'] = True
        self.connection = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)
        self.connection.flushdb()
        self.app = web.app.test_client()

    def test_should_login_successful(self):
        data = {'username': 'sidneygao', 'source': 'WEB', 'device_id': 'device_id1',
                'token': 'fake_token', 'password': '123abc'}
        rv = self.app.post('/login/', data=data)
        response_data = json.loads(rv.data)
        user = User.query.filter(User.login_name == 'sidneygao').first()
        self.assertEqual(user.last_login_source, 'WEB')
        self.assertIsNotNone(user.last_login_time)
        self.assertEqual(200, rv.status_code)
        self.assertTrue(response_data['result'])
        self.assertEqual('sidneygao', response_data['user_info']['login_name'])
        self.assertSetEqual({'message', 'result', 'token', 'user_info'}, set(response_data.keys()))

    def test_should_return_400_if_password_wrong(self):
        data = {'username': 'sidneygao', 'source': 'WEB', 'device_id': 'device_id1',
                'token': 'fake_token', 'password': 'wrong_pwd'}
        rv = self.app.post('/login/', data=data)
        response_data = json.loads(rv.data)
        self.assertEqual(401, rv.status_code)
        self.assertFalse(response_data['result'])
        self.assertEqual(u'用户名或密码错误', response_data['message'])
        self.assertIsNone(response_data['user_info'])
        self.assertSetEqual({'message', 'result', 'token', 'user_info'}, set(response_data.keys()))

    def test_should_return_401_given_login_failed_times_over_3(self):
        data = {'username': 'sidneygao', 'source': 'WEB', 'device_id': 'device_id1',
                'token': 'fake_token', 'password': 'wrong_pwd'}
        for _ in range(LOGIN_FAILED_MAXIMAL_TIMES):
            rv = self.app.post('/login/', data=data)
            response_data = json.loads(rv.data)
            self.assertEqual(401, rv.status_code)
            self.assertFalse(response_data['result'])
            self.assertEqual(u'用户名或密码错误', response_data['message'])

        rv = self.app.post('/login/', data=data)
        response_data = json.loads(rv.data)
        self.assertEqual(401, rv.status_code)
        self.assertFalse(response_data['result'])
        self.assertEqual(u'用户已被禁用', response_data['message'])

    def test_should_active_user_given_user_was_banned(self):
        data = {'username': 'sidneygao', 'source': 'WEB', 'device_id': 'device_id1',
                'token': 'fake_token', 'password': 'wrong_pwd'}
        for _ in range(LOGIN_FAILED_MAXIMAL_TIMES):
            self.app.post('/login/', data=data)

        rv = self.app.post('/login/', data=data)
        response_data = json.loads(rv.data)
        self.assertEqual(401, rv.status_code)
        self.assertEqual(u'用户已被禁用', response_data['message'])

        rv = self.app.post('/user/sidneygao/active/')
        self.assertEqual(200, rv.status_code)

        data['password'] = '123abc'
        rv = self.app.post('/login/', data=data)
        response_data = json.loads(rv.data)
        self.assertEqual(200, rv.status_code)
        self.assertTrue(response_data['result'])
        self.assertEqual('sidneygao', response_data['user_info']['login_name'])

    def test_should_login_successful_without_password(self):
        data = {'username': 'sidneygao', 'source': 'WEB', 'device_id': 'device_id1',
                'token': 'fake_token'}
        rv = self.app.post('/login/nopassword/', data=data)
        response_data = json.loads(rv.data)
        user = User.query.filter(User.login_name == 'sidneygao').first()
        self.assertEqual(user.last_login_source, 'WEB')
        self.assertIsNotNone(user.last_login_time)
        self.assertEqual(200, rv.status_code)
        self.assertTrue(response_data['result'])
        self.assertEqual('sidneygao', response_data['user_info']['login_name'])
        self.assertSetEqual({'message', 'result', 'token', 'user_info'}, set(response_data.keys()))

    def test_should_return_401_given_user_not_exist(self):
        data = {'username': 'not_exist_user', 'source': 'WEB', 'device_id': 'device_id1',
                'token': 'fake_token'}
        rv = self.app.post('/login/nopassword/', data=data)
        response_data = json.loads(rv.data)
        self.assertEqual(401, rv.status_code)
        self.assertFalse(response_data['result'])
        self.assertEqual(u'用户不存在', response_data['message'])
        self.assertSetEqual({'message', 'result', 'token', 'user_info'}, set(response_data.keys()))

    def test_logout_should_return_new_token(self):
        rv = self.app.post('/logout/fakesessionid')
        response_data = json.loads(rv.data)
        self.assertEqual(200, rv.status_code)
        self.assertIsNotNone(response_data['token'])

    def test_should_refresh_token_success(self):
        data = {'username': 'sidneygao', 'source': 'WEB', 'device_id': 'device_id1',
                'token': 'fake_token', 'password': '123abc'}
        rv = self.app.post('/login/', data=data)
        session_id = json.loads(rv.data)['token']
        refresh_date = {'source': 'IOS'}
        ret = self.app.post('/refresh/' + session_id, data=refresh_date)
        return_data = json.loads(ret.data)
        self.assertEqual(200, ret.status_code)
        self.assertTrue(return_data['result'])
        self.assertNotEqual(return_data['token'], session_id)
        self.assertEqual(return_data['user_info']['login_name'], 'sidneygao')

    def test_should_return_401_refresh_token_with_incorrect_session_id(self):
        data = {'username': 'sidneygao', 'source': 'WEB', 'device_id': 'device_id1',
                'token': 'fake_token', 'password': '123abc'}
        self.app.post('/login/', data=data)
        refresh_date = {'source': 'IOS'}
        ret = self.app.post('/refresh/incorrect_session_id', data=refresh_date)
        return_data = json.loads(ret.data)
        self.assertEqual(401, ret.status_code)
        self.assertFalse(return_data['result'])


class TestUserView(TestCase):
    def setUp(self):
        self.app = web.app.test_client()
        self.mobile = "13888888888"

    def test_create_user_successful(self):
        # test create
        data = {"mobile": self.mobile, "password": "123abc", "channel": "test", "source": "WEB"}
        ret = self.app.post('/users', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(201, ret.status_code)
        self.assertTrue(response_data['result'])
        login_name = response_data['user_info']['login_name']
        self.assertEqual(self.mobile, response_data['user_info']['mobile'])
        self.assertEqual('test', response_data['user_info']['channel'])
        self.assertEqual('WEB', response_data['user_info']['source'])
        ur = UserRole.query.filter(UserRole.login_name == login_name).first()
        self.assertTrue(ur)
        self.assertEqual('USER', ur.role)

        ret = self.app.post('/users', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(400, ret.status_code)
        self.assertFalse(response_data['result'])

        ret_get = self.app.get('/user/{}'.format(self.mobile))
        response_data_get = json.loads(ret_get.data)
        self.assertEqual(200, ret_get.status_code)
        self.assertTrue(response_data_get['result'])
        self.assertEqual(self.mobile, response_data_get['user_info']['mobile'])
        self.assertEqual('test', response_data_get['user_info']['channel'])
        self.assertEqual('WEB', response_data_get['user_info']['source'])

    def test_update_user_successful(self):
        data = {"mobile": self.mobile, "password": "123abc", "channel": "test", "source": "WEB"}
        ret = self.app.post('/users', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(201, ret.status_code)
        self.assertTrue(response_data['result'])
        login_name = response_data['user_info']['login_name']

        data = {"login_name": login_name, "source": "IOS"}
        ret = self.app.put('/user', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(200, ret.status_code)
        self.assertTrue(response_data['result'])
        self.assertEqual('test', response_data['user_info']['channel'])
        self.assertEqual('IOS', response_data['user_info']['source'])

    def test_should_return_400_given_wrong_source(self):
        data = {"mobile": self.mobile, "password": "123abc", "channel": "test", "source": "C"}
        ret = self.app.post('/users', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(400, ret.status_code)
        self.assertFalse(response_data['result'])

    def test_should_return_404_given_user_not_exist(self):
        ret_get = self.app.get('/user/{}'.format(self.mobile))
        response_data_get = json.loads(ret_get.data)
        self.assertEqual(404, ret_get.status_code)
        self.assertFalse(response_data_get['result'])

    def test_should_return_400_given_user_not_exist(self):
        data = {"login_name": "some_login_name", "channel": "new_channel", "source": "IOS"}
        ret = self.app.put('/user', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(400, ret.status_code)
        self.assertFalse(response_data['result'])

    def tearDown(self):
        user = User.query.filter(User.mobile == self.mobile).first()
        if user:
            UserRole.query.filter(UserRole.login_name == user.login_name).delete()
            User.query.filter(User.mobile == self.mobile).delete()
            db.session.commit()


class TestModifyPassword(TestCase):
    def setUp(self):
        self.app = web.app.test_client()
        self.ori_password = "123abc"
        self.user = User("13888888888", None, None, 'WEB')
        self.user.set_password(self.ori_password)
        db.session.add(self.user)
        db.session.commit()

    def test_should_change_password(self):
        new_password = '1234abcd'
        data = {"login_name": self.user.login_name, "ori_password": self.ori_password, "password": new_password}
        ret = self.app.post('/user/change-password', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(200, ret.status_code)
        self.assertTrue(response_data['result'])

        user = User.query.filter(User.login_name == self.user.login_name).first()
        self.assertFalse(user.validate_password(self.ori_password))
        self.assertTrue(user.validate_password(new_password))

    def test_should_reset_password(self):
        new_password = '1234abcd'
        data = {"login_name": self.user.login_name, "password": new_password}
        ret = self.app.put('/user/reset-password', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(200, ret.status_code)
        self.assertTrue(response_data['result'])

        user = User.query.filter(User.login_name == self.user.login_name).first()
        self.assertFalse(user.validate_password(self.ori_password))
        self.assertTrue(user.validate_password(new_password))

    def test_should_return_400_given_user_not_exist(self):
        data = {"login_name": "some_login_name", "password": "some_password"}
        ret = self.app.put('/user/reset-password', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(400, ret.status_code)
        self.assertFalse(response_data['result'])

        data = {"login_name": "some_login_name", "ori_password": self.ori_password, "password": "some_password"}
        ret = self.app.post('/user/change-password', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(400, ret.status_code)
        self.assertFalse(response_data['result'])

    def test_should_return_401_ori_password_wrong(self):
        data = {"login_name": self.user.login_name, "ori_password": "lll111", "password": "some_password"}
        ret = self.app.post('/user/change-password', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(401, ret.status_code)
        self.assertFalse(response_data['result'])

    def tearDown(self):
        User.query.filter(User.login_name == self.user.login_name).delete()
        db.session.commit()


class TestUserQuery(TestCase):
    def _moke_user(self, seed, referrer, channel, status):
        u = User('1779900{:04d}'.format(seed), referrer, channel, 'WEB')
        u.set_password('123abc')
        u.email = 'u{:04d}@user.test'.format(seed)
        u.status = status
        u.identity_number = '11011019901010{:04d}'.format(seed),
        u.register_time = datetime(2017, 1, 1, 1, 1, 0) + timedelta(seconds=seed)
        return u

    def setUp(self):
        self.app = web.app.test_client()
        self.ref1 = User("17788000001", None, None, 'WEB')
        self.ref2 = User("17788000002", None, None, 'WEB')
        self.ref1.set_password("123abc")
        self.ref2.set_password("123abc")
        db.session.add_all((self.ref1, self.ref2))
        db.session.commit()

        self.users = [
            self._moke_user(1, self.ref1.login_name, 'C1', 'ACTIVE'),
            self._moke_user(2, self.ref1.login_name, 'C1', 'INACTIVE'),
            self._moke_user(3, self.ref1.login_name, 'C1', 'ACTIVE'),
            self._moke_user(4, self.ref1.login_name, 'C1', 'ACTIVE'),
            self._moke_user(5, self.ref1.login_name, 'C1', 'ACTIVE'),
            self._moke_user(6, self.ref1.login_name, 'C1', 'ACTIVE'),
            self._moke_user(7, self.ref1.login_name, 'C1', 'INACTIVE'),
            self._moke_user(8, self.ref1.login_name, 'C2', 'ACTIVE'),
            self._moke_user(9, self.ref1.login_name, 'C1', 'ACTIVE'),
            self._moke_user(10, self.ref1.login_name, 'C1', 'ACTIVE'),
            self._moke_user(11, self.ref2.login_name, 'C2', 'INACTIVE'),
            self._moke_user(12, self.ref2.login_name, 'C3', 'ACTIVE'),
            self._moke_user(13, self.ref2.login_name, 'C1', 'ACTIVE'),
            self._moke_user(14, self.ref2.login_name, 'C4', 'ACTIVE'),
            self._moke_user(15, self.ref2.login_name, 'C4', 'ACTIVE'),
            self._moke_user(16, self.ref2.login_name, 'C4', 'ACTIVE'),
        ]
        self.user_roles = [UserRole(u.login_name, "USER") for u in self.users]
        db.session.add_all(self.users)
        db.session.add_all(self.user_roles)
        db.session.commit()

    def test_query_by_email(self):
        ret_get = self.app.get('/users?&page_size=10&email=u0002@user.test')
        data = json.loads(ret_get.data)
        self.assertEqual(200, ret_get.status_code)
        self.assertTrue(data['result'])
        self.assertEqual(1, len(data['items']))
        self.assertEqual('17799000002', data['items'][0]['mobile'])
        self.assertEqual('u0002@user.test', data['items'][0]['email'])

    def test_query_by_identity_number(self):
        ret_get = self.app.get('/users?page_size=10&identity_number=110110199010100011')
        data = json.loads(ret_get.data)
        self.assertEqual(200, ret_get.status_code)
        self.assertTrue(data['result'])
        self.assertEqual(1, len(data['items']))
        self.assertEqual('17799000011', data['items'][0]['mobile'])
        self.assertEqual('u0011@user.test', data['items'][0]['email'])

    def test_query_by_role_mobile__like_and_sort(self):
        ret_get = self.app.get('/users?page_size=10&role=USER&mobile__like=17799&sort=-register_time')
        data = json.loads(ret_get.data)
        self.assertEqual(200, ret_get.status_code)
        self.assertTrue(data['result'])
        self.assertEqual(10, len(data['items']))
        self.assertEqual(16, data['total_count'])
        self.assertEqual('17799000016', data['items'][0]['mobile'])
        self.assertEqual('u0016@user.test', data['items'][0]['email'])
        self.assertEqual('17799000007', data['items'][9]['mobile'])
        self.assertEqual('u0007@user.test', data['items'][9]['email'])

    def test_query_by_status_register_time_with_given_fields(self):
        ret_get = self.app.get(
            '/users?page_size=10&status=ACTIVE&register_time__gte=2017-1-1 1:1:0&register_time__lte=2017-1-1 1:1:59&fields=login_name,mobile')
        data = json.loads(ret_get.data)
        self.assertEqual(200, ret_get.status_code)
        self.assertTrue(data['result'])
        self.assertEqual(10, len(data['items']))
        self.assertEqual(13, data['total_count'])
        self.assertTrue('login_name' in data['items'][0])
        self.assertTrue('mobile' in data['items'][0])
        self.assertFalse('email' in data['items'][0])
        self.assertFalse('register_time' in data['items'][0])

    def test_query_by_channel_referrer_with_paging(self):
        ret_get = self.app.get('/users?channels=C1,C2&referrer={}&page_size=3&page=2'.format(self.ref1.login_name))
        data = json.loads(ret_get.data)
        self.assertEqual(200, ret_get.status_code)
        self.assertTrue(data['result'])
        self.assertEqual(3, len(data['items']))
        self.assertEqual(2, data['page'])
        self.assertEqual(10, data['total_count'])
        self.assertTrue(data['has_next'])
        self.assertTrue(data['has_prev'])

    def tearDown(self):
        for u in self.users:
            db.session.delete(u)
        for ur in self.user_roles:
            db.session.delete(ur)
        db.session.commit()
        db.session.delete(self.ref1)
        db.session.delete(self.ref2)
        db.session.commit()


if __name__ == '__main__':
    with web.app.app_context():
        main()
