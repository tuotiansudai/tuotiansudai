import hashlib
import redis
import uuid

from concurrent.futures import ThreadPoolExecutor
from tornado import gen
from tornado.options import define, options, parse_command_line
import tornado.web
import tornado.ioloop
from tornado.web import RequestHandler, HTTPError
import torndb

define("port", default=8888, help="run on the given port", type=int)
define("mysql_host", default="192.168.33.10:3306", help="database host")
define("mysql_database", default="aa", help="database name")
define("mysql_user", default="root", help="database user")
define("mysql_password", default="root", help="database password")
define("access_key", default="sug5lXn8uUiJFc", help="user private access key")

executor = ThreadPoolExecutor(5)

_username = "wdzj_api"
_password = "hazeOverAll"

_redis = redis.StrictRedis(host="192.168.33.10", port=6379, db=0)


class Error404Handler(tornado.web.ErrorHandler):
    def write_error(self, *args, **kwargs):
        self.write({'error': "Url not found"})


class RefreshTokenHanlder(RequestHandler):
    def get(self):
        username, password = self.get_argument('username', None), self.get_argument('password', None)
        if username and password:

            if username != _username or password != _password:
                raise HTTPError(403)
            else:
                uuid1 = uuid.uuid1()
                _redis.set("token", uuid1, 60)
                self.write({'data': {'token' : str(uuid1)}})
        else:
            raise HTTPError(400)


class BaseHandler(RequestHandler):
    @gen.coroutine
    def prepare(self):
        token = self.request.query_arguments.get('token', [None])[-1]
        cached_token = _redis.get("token")
        if cached_token != token:
            raise HTTPError(403)

    def write_error(self, status_code, **kwargs):
        known_errors = {400: "Parameters error", 403: "Token validation error"}
        if status_code in known_errors:
            self.write({'error': known_errors.get(status_code)})


if __name__ == '__main__':
    parse_command_line()

    # Have one global connection to the blog DB across all handlers
    db = torndb.Connection(
        host=options.mysql_host, database=options.mysql_database,
        user=options.mysql_user, password=options.mysql_password)

    settings = {'debug': True, 'db': db, 'default_handler_class': Error404Handler,
                'default_handler_args': dict(status_code=404)}
    app = tornado.web.Application([
        (r'/wdzj/refreshToken', RefreshTokenHanlder)], **settings)
    app.listen(options.port)
    tornado.ioloop.IOLoop.current().start()
