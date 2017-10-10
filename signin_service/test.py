# coding=utf-8
import json
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
        web.app.config['TESTING'] = True
        self.connection = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)
        self.connection.flushdb()
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

        data = {"login_name": login_name, "channel": "new_channel", "source": "IOS"}
        ret = self.app.put('/user', data=json.dumps(data), content_type='application/json')
        response_data = json.loads(ret.data)
        self.assertEqual(200, ret.status_code)
        self.assertTrue(response_data['result'])
        self.assertEqual('new_channel', response_data['user_info']['channel'])
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


if __name__ == '__main__':
    with web.app.app_context():
        main()
