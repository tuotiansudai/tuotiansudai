#coding:utf-8

import redis
import uuid

from concurrent.futures import ThreadPoolExecutor
from tornado import gen
from tornado.options import define, options, parse_command_line
import tornado.ioloop
from tornado.web import RequestHandler, HTTPError
import torndb

define("port", default=8888, help="run on the given port", type=int)
define("mysql_host", default="192.168.1.151:3306", help="database host")
define("mysql_database", default="aa", help="database name")
define("mysql_user", default="root", help="database user")
define("mysql_password", default="root", help="database password")
define("access_key", default="sug5lXn8uUiJFc", help="user private access key")

define("wdzj_username", default="test", help="wdzj api username")
define("wdzj_password", default="test", help="wdzj api password")
define("redis_host", default="192.168.33.10", help="redis host")
define("redis_port", default="6379", help="redis port")

executor = ThreadPoolExecutor(5)

_redis = redis.StrictRedis(host=options.redis_host, port=options.redis_port, db=0)


class Error404Handler(tornado.web.ErrorHandler):
    def write_error(self, *args, **kwargs):
        self.write({'error': "Url not found"})


class RefreshTokenHanlder(RequestHandler):
    def get(self):
        username, password = self.get_argument('username', None), self.get_argument('password', None)
        if username and password:

            if username != options.wdzj_username or password != options.wdzj_password:
                raise HTTPError(403)
            else:
                uuid1 = uuid.uuid1()
                _redis.set("token", uuid1, 3600)
                self.write({'data': {'token': str(uuid1)}})
        else:
            raise HTTPError(400)


class BaseHandler(RequestHandler):
    @gen.coroutine
    def prepare(self):
        token = self.request.query_arguments.get('token', [None])[-1]
        cached_token = _redis.get("token")
        if not token or cached_token != token:
            raise HTTPError(403)

    def write_error(self, status_code, **kwargs):
        known_errors = {400: "Parameters error", 403: "Token validation error"}
        if status_code in known_errors:
            self.write({'error': known_errors.get(status_code)})


class LoanDetailHandler(BaseHandler):
    totalCountSQL = "select count(1) as totalCount from loan where DATE_FORMAT(raising_complete_time, '%%Y-%%m-%%d') = %s"

    totalAmountSQL = "select IFNULL(sum(loan_amount),0) totalAmount from loan where DATE_FORMAT(raising_complete_time, '%%Y-%%m-%%d') = %s"

    loanDetailSQL = '''
        select id as projectId, `name` as title, loan_amount as amount, 100 as schedule, CONCAT((base_rate + activity_rate)*100,'%%') as interestRate, periods as deadline,
        '月' as deadlineUnit, 0 as reward, '抵押标' as type, type as repaymentType, agent_login_name as userName, raising_complete_time as successTime
        from loan where DATE_FORMAT(raising_complete_time,'%%Y-%%m-%%d') = %s limit %s, %s;
    '''

    investDetailSQL= '''
        select UPPER(md5(login_name)) as subscribeUserName, CAST(truncate(amount/100,2) as char(10)) as amount, cast(truncate(amount/100,2) as char(10)) as validAmount,
        CAST(invest_time as CHAR(19)) as addDate, 1 as status, 0 as type from invest where loan_id = %s and status = 'SUCCESS'
    '''

    def get(self):
        currentPage = int(self.request.query_arguments.get('page', [None])[-1])
        pageSize = int(self.request.query_arguments.get('pageSize', [None])[-1])
        date = self.request.query_arguments.get('date', [None])[-1]

        start = (currentPage - 1) * pageSize

        totalCount = int(self.settings['db'].get(self.totalCountSQL, date)["totalCount"])
        totalAmount = float(self.settings['db'].get(self.totalAmountSQL, date)["totalAmount"]) / 100
        loanDetailRows = self.settings['db'].query(self.loanDetailSQL, date, start, pageSize)

        for row in loanDetailRows:
            loanId = str(row['projectId'])
            if row['repaymentType'] == 'LOAN_INTEREST_MONTHLY_REPAY' or row[
                'repaymentType'] == 'INVEST_INTEREST_MONTHLY_REPAY':
                row['repaymentType'] = 5
            else:
                row['repaymentType'] = 1
            row['loanUrl'] = 'https://tuotiansudai.com/loan/' + loanId
            row['successTime'] = row['successTime'].strftime("%Y-%m-%d %H:%M:%S")
            invest_rows = self.settings['db'].query(self.investDetailSQL, loanId)
            row['subscribes'] =  invest_rows

        totalPage = (totalCount - 1) / pageSize + 1
        totalPage = totalPage if totalPage > 0 else 1

        return self.write(
            {'totalPage': totalPage, 'currentPage': currentPage, 'totalCount': totalCount, 'totalAmount': totalAmount,
             'date': date, 'borrowList': loanDetailRows})


if __name__ == '__main__':
    parse_command_line()

    # Have one global connection to the blog DB across all handlers
    db = torndb.Connection(
        host=options.mysql_host, database=options.mysql_database,
        user=options.mysql_user, password=options.mysql_password)

    settings = {'debug': True, 'db': db, 'default_handler_class': Error404Handler,
                'default_handler_args': dict(status_code=404)}
    app = tornado.web.Application([
        (r'/wdzj/refreshToken', RefreshTokenHanlder),
        (r'/wdzj/loanDetail', LoanDetailHandler)], **settings)
    app.listen(options.port)
    tornado.ioloop.IOLoop.current().start()
