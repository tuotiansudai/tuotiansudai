from unittest import TestCase, main

import redis

from service import SessionManager, TOKEN_FORMAT
from settings import REDIS_HOST, REDIS_PORT, REDIS_DB, WEB_TOKEN_EXPIRED_SECONDS, MOBILE_TOKEN_EXPIRED_SECONDS


class TestSessionManager(TestCase):
    def setUp(self):
        self.connection = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)

    def test_should_set_data_with_new_token_and_remove_old_token(self):
        manager = SessionManager()
        old_token_id = manager.create()
        old_token = TOKEN_FORMAT.format(old_token_id)
        self.connection.set(old_token, "{}")
        new_data = {'data': 'new_data'}
        new_token_id = manager.set(new_data, old_token_id, source='WEB')

        self.assertFalse(self.connection.exists(old_token))
        self.assertIsNotNone(new_token_id)
        self.assertEqual(new_data, manager.get(new_token_id))

    def test_should_generate_new_token_with_WEB_source(self):
        manager = SessionManager()
        old_token_id = manager.create()
        old_token = TOKEN_FORMAT.format(old_token_id)
        self.connection.set(old_token, "{}")
        new_data = {'data': 'new_data'}
        new_token_id = manager.set(new_data, old_token_id, source='WEB')
        new_token = TOKEN_FORMAT.format(new_token_id)
        new_token_ttl = self.connection.ttl(new_token)
        self.assertEqual(new_token_ttl, WEB_TOKEN_EXPIRED_SECONDS)

    def test_should_generate_new_token_with_IOS_source(self):
        manager = SessionManager()
        old_token_id = manager.create()
        old_token = TOKEN_FORMAT.format(old_token_id)
        self.connection.set(old_token, "{}")
        new_data = {'data': 'new_data'}
        new_token_id = manager.set(new_data, old_token_id, source='IOS')
        new_token = TOKEN_FORMAT.format(new_token_id)
        new_token_ttl = self.connection.ttl(new_token)
        self.assertEqual(new_token_ttl, MOBILE_TOKEN_EXPIRED_SECONDS)

    def test_should_generate_new_token_with_ANDROID_source(self):
        manager = SessionManager()
        old_token_id = manager.create()
        old_token = TOKEN_FORMAT.format(old_token_id)
        self.connection.set(old_token, "{}")
        new_data = {'data': 'new_data'}
        new_token_id = manager.set(new_data, old_token_id, source='android')
        new_token = TOKEN_FORMAT.format(new_token_id)
        new_token_ttl = self.connection.ttl(new_token)
        self.assertEqual(new_token_ttl, MOBILE_TOKEN_EXPIRED_SECONDS)

    def test_replace_should_generate_new_token(self):
        manager = SessionManager()
        data = {'data': 'test data'}
        token_id = manager.set(data, 'fake_session_id', source='IOS')
        new_token_id = manager.refresh(token_id)
        self.assertEqual(data, manager.get(new_token_id))
        self.assertIsNone(manager.get(token_id))

    def test_replace_should_return_none_given_session_id_not_exist(self):
        manager = SessionManager()
        new_token_id = manager.refresh("not_exist_session_id")
        self.assertIsNone(new_token_id)


if __name__ == '__main__':
    main()
