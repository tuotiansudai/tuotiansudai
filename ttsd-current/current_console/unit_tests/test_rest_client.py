# -*- coding: utf-8 -*-
import mock
import requests
from django.test import TestCase

from current_console.rest_client import RestClient


class RestClientViewTests(TestCase):
    @mock.patch('requests.get')
    def test_should_return_200_when_call_get(self, fake_requests_get):
        params = {'param': 'param'}
        call_get_response = "call get success"
        fake_requests_get.return_value.status_code = 200
        fake_requests_get.return_value.json = mock.Mock(return_value=call_get_response)
        response = RestClient("/fake-get-url").get(params=params)

        self.assertEqual(response, call_get_response)

    @mock.patch('requests.post')
    def test_should_return_200_when_call_post(self, fake_requests_post):
        call_post_request = {"content": "content"}
        call_post_response = "call post success"
        fake_requests_post.return_value.status_code = 200
        fake_requests_post.return_value.json = mock.Mock(return_value=call_post_response)

        response = RestClient("/fake-post-url").post(data=call_post_request)

        self.assertEqual(response, call_post_response)

    @mock.patch('requests.put')
    def test_should_return_200_when_call_put(self, fake_requests_put):
        call_put_request = {"content": "content"}
        call_put_response = "call put success"
        fake_requests_put.return_value.status_code = 200
        fake_requests_put.return_value.json = mock.Mock(return_value=call_put_response)
        response = RestClient("/fake-put-url").put(data=call_put_response)

        self.assertEqual(response, call_put_response)

    @mock.patch('requests.put')
    def test_should_return_time_out_when_call_post_throw_time_out(self, fake_requests_post):
        call_put_request = {"content": "content"}
        call_put_response = "call put success"
        fake_requests_post.return_value.status_code = 200
        fake_requests_post.return_value.json = mock.Mock(return_value=call_put_response)
        fake_requests_post.side_effect = requests.Timeout
        response = RestClient("/fake-put-url").put(data=call_put_response)

        self.assertEqual(fake_requests_post.call_count, 3)
