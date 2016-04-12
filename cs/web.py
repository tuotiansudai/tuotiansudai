import hashlib

from concurrent.futures import ThreadPoolExecutor
from tornado import gen
from tornado.options import define, options, parse_command_line
import tornado.web
import tornado.ioloop
from tornado.web import RequestHandler, HTTPError
import torndb


define("port", default=8888, help="run on the given port", type=int)
define("mysql_host", default="192.168.1.249:3306", help="database host")
define("mysql_database", default="aa", help="database name")
define("mysql_user", default="root", help="database user")
define("mysql_password", default="", help="database password")
define("access_key", default="sug5lXn8uUiJFc", help="user private access key")

executor = ThreadPoolExecutor(5)


class Error404Handler(tornado.web.ErrorHandler):
    def write_error(self, *args, **kwargs):
        self.write({'error': "Url not found"})


class BaseHandler(RequestHandler):

    @gen.coroutine
    def prepare(self):
        req_sign = self.request.query_arguments.get('sign', [None])[-1]
        params = filter(lambda (k, v): k != 'sign', self.request.query_arguments.items())
        key = reduce(lambda ret, (k, v): ret + k + v[-1], params, '')

        def build_sign():
            sign = hashlib.md5('({}, {})'.format(key, options.access_key)).hexdigest()
            return sign

        signed = yield executor.submit(build_sign)
        if signed != req_sign:
            raise HTTPError(403)

    def write_error(self, status_code, **kwargs):
        known_errors = {400: "Parameters error", 403: "Sign validation error"}
        if status_code in known_errors:
            self.write({'error': known_errors.get(status_code)})


class UsersHandler(BaseHandler):
    SQL = '''
    SELECT u.`login_name`,
           a.`user_name`,
           u.`mobile`,
           u.`register_time`,
           u.`last_modified_time`,
           b.`card_number` ,
           u.`source`,
           u.`channel`,
           u.`province`,
           u.`city`,
           a.`identity_number`,
           IFNULL((SELECT 1 FROM `user_role` ur WHERE ur.`login_name` = u.`login_name` AND ur.`role` = 'STAFF'), 0) AS is_self_staff,
           u.`referrer` ,
           IFNULL((SELECT 1 FROM `user_role` ur WHERE ur.`login_name`  = u.`referrer`  AND ur.`role` = 'STAFF'), 0) AS is_referrer_staff
      FROM `user` u
      LEFT JOIN `account` a on u.`login_name`= a.`login_name`
      LEFT JOIN `bank_card` b ON u.`login_name`= b.`login_name` AND b.`status` = 'PASSED'
    WHERE u.`last_modified_time` BETWEEN %s AND %s ORDER BY `register_time`
'''

    def get(self):
        start_time, end_time = self.get_argument('start_time', None), self.get_argument('end_time', None)
        if start_time and end_time:
            rows = self.settings['db'].query(self.SQL, start_time, end_time)
            for row in rows:
                row['register_time'], row[
                    'last_modified_time'] = row.register_time.isoformat(), row.last_modified_time.isoformat()
            self.write({'users': rows})
        else:
            raise HTTPError(400)


class RechargesHandler(BaseHandler):
    SQL = '''
    SELECT `id`,
           `login_name` as user_id,
           `created_time`,
           `fast_pay`,
           `source`,
           `channel`,
           `bank_code`,
           `amount`
      FROM `recharge`
     WHERE `created_time` BETWEEN %s
       AND %s
'''

    def get(self):
        start_time, end_time = self.get_argument('start_time', None), self.get_argument('end_time', None)
        if start_time and end_time:
            rows = self.settings['db'].query(self.SQL, start_time, end_time)
            for row in rows:
                row['created_time'] = row.created_time.isoformat()
            self.write({'recharges': rows})
        else:
            raise HTTPError(400)


class WithdrawsHandler(BaseHandler):
    SQL = '''
    SELECT `id`,
           `login_name`,
           `created_time`,
           `apply_notify_time`,
           `notify_time` as `final_notify_time`,
           `source`,
           `status`,
           `amount`
      FROM `withdraw`
     WHERE `created_time` BETWEEN %(start_time)s
       AND %(end_time)s
        OR `apply_notify_time` BETWEEN %(start_time)s
       AND %(end_time)s
        OR `notify_time` BETWEEN %(start_time)s
       AND %(end_time)s
'''

    def get(self):
        start_time, end_time = self.get_argument('start_time', None), self.get_argument('end_time', None)
        if start_time and end_time:
            rows = self.settings['db'].query(self.SQL, **{'start_time': start_time, 'end_time': end_time})
            for row in rows:
                row['created_time'] = row.created_time.isoformat()
                row['apply_notify_time'] = row.apply_notify_time.isoformat() if row.apply_notify_time else None
                row['final_notify_time'] = row.final_notify_time.isoformat() if row.final_notify_time else None
            self.write({'withdraws': rows})
        else:
            raise HTTPError(400)


class InvestHandler(BaseHandler):
    SQL_INVEST = '''
    SELECT `id`,
           `loan_id`,
           `source`,
           `channel`,
           `created_time`,
           `is_auto_invest`,
           `amount`,
           `status`
      FROM `invest`
     WHERE `id` = %s
    '''

    SQL_INVEST_REPAY = '''
    SELECT `period`,
           `repay_date`,
           `corpus` as `principal_balance`,
           `expected_interest`,
           `expected_fee`,
           `actual_repay_date`,
           `actual_interest`,
           `actual_fee`,
           `status`
      FROM `invest_repay`
     WHERE `invest_id`= %s
    '''

    def get(self, invest_id):
        invest = self.settings['db'].get(self.SQL_INVEST, invest_id)
        if invest:
            invest['created_time'] = invest.created_time.isoformat()
            invest_repays = self.settings['db'].query(self.SQL_INVEST_REPAY, invest_id)
            for repay in invest_repays:
                repay['repay_date'] = repay.repay_date.isoformat()
                repay['actual_repay_date'] = repay.actual_repay_date.isoformat() if repay.actual_repay_date else None
            invest['repay_plan'] = invest_repays
            self.write(invest)
        else:
            raise HTTPError(400)


class InvestsHandler(BaseHandler):
    SQL = '''
    SELECT `id`,
           `loan_id`,
           `source`,
           `channel`,
           `created_time`,
           `is_auto_invest`,
           `amount`,
           `status`
      FROM `invest`
      WHERE `created_time` BETWEEN %s AND %s
    '''

    def get(self):
        start_time, end_time = self.get_argument('start_time', None), self.get_argument('end_time', None)
        if start_time and end_time:
            rows = self.settings['db'].query(self.SQL, start_time, end_time)
            for row in rows:
                row['created_time'] = row.created_time.isoformat()
            self.write({'invests': rows})
        else:
            raise HTTPError(400)


class UserHandler(BaseHandler):
    SQL_USER = '''
    SELECT u.`login_name`,
           a.`user_name`,
           u.`mobile`,
           u.`register_time`,
           u.`last_modified_time`,
           b.`card_number` ,
           u.`source`,
           u.`channel`,
           u.`province`,
           u.`city`,
           a.`identity_number`,
           a.`balance`,
           IFNULL((SELECT 1 FROM `user_role` ur WHERE ur.`login_name` = u.`login_name` AND ur.`role` = 'STAFF'), 0) AS is_self_staff,
           u.`referrer` ,
           IFNULL((SELECT 1 FROM `user_role` ur WHERE ur.`login_name`  = u.`referrer`  AND ur.`role` = 'STAFF'), 0) AS is_referrer_staff
      FROM `user` u
      LEFT JOIN `account` a on u.`login_name`= a.`login_name`
      LEFT JOIN `bank_card` b ON u.`login_name`= b.`login_name` AND b.`status` = 'PASSED'
    WHERE u.{} = %s
    '''

    SQL_INVEST = '''
    SELECT `id`,
           `loan_id`,
           `source`,
           `channel`,
           `created_time`,
           `is_auto_invest`,
           `amount`,
           `status`
      FROM `invest`
     WHERE `login_name` = %s
    '''

    def get(self):
        query_key = filter(lambda query_key: self.get_argument(query_key, None) is not None,
                           ('login_name', 'identity_number', 'mobile'))
        if query_key:
            query_key = query_key[-1]
            value = self.get_argument(query_key)
            user = self.settings['db'].get(self.SQL_USER.format(query_key), value)
            if user:
                user['register_time'], user['last_modified_time'] = user.register_time.isoformat(), \
                                                                    user.last_modified_time.isoformat()
                invests = self.settings['db'].query(self.SQL_INVEST, user.login_name)
                for invest in invests:
                    invest['created_time'] = invest.created_time.isoformat()
                user['invests'] = invests
                return self.write(user)
        raise HTTPError(400)


class BalancesHandler(BaseHandler):
    SQL = '''
    SELECT a.`login_name`,
           a.`user_name`,
           u.`mobile`,
           a.`balance`
      FROM `account` a
      INNER JOIN `user` u on a.`login_name`= u.`login_name`
     where `balance`> 5000
     order by `balance` DESC
    '''

    def get(self):
        rows = self.settings['db'].query(self.SQL)
        return self.write({'balances': rows})


if __name__ == '__main__':
    parse_command_line()

    # Have one global connection to the blog DB across all handlers
    db = torndb.Connection(
        host=options.mysql_host, database=options.mysql_database,
        user=options.mysql_user, password=options.mysql_password)

    settings = {'debug': True, 'db': db, 'default_handler_class': Error404Handler, 'default_handler_args': dict(status_code=404)}
    app = tornado.web.Application([
        (r'/cs/users', UsersHandler),
        (r'/cs/withdraws', WithdrawsHandler),
        (r'/cs/invest/(\d+)/', InvestHandler),
        (r'/cs/invests', InvestsHandler),
        (r'/cs/user', UserHandler),
        (r'/cs/balances', BalancesHandler),
        (r'/', BaseHandler),
        (r'/cs/recharges', RechargesHandler)], **settings)
    app.listen(options.port)
    tornado.ioloop.IOLoop.current().start()
