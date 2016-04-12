# -*- coding: utf-8 -*-
import csv
from datetime import datetime, timedelta
import StringIO
import logging


logger = logging.getLogger(__name__)
hdlr = logging.FileHandler('/var/log/july.log')
formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')
hdlr.setFormatter(formatter)
logger.addHandler(hdlr)
logger.setLevel(logging.INFO)

RECIPIENT = ["xupengfei@tuotiansudai.com"]


SQL = """
    SELECT
      (@rownum := @rownum + 1) AS '序号',
      temp.*
    FROM
      (SELECT DISTINCT
        t.`register_time` AS '注册日期',
        t.`username` AS '注册账号',
        t.`mobile_number` AS '注册手机号',
        IF (
          m.`account_id` IS NOT NULL,
          1,
          0
        ) AS '是否实名',
        IF (n.`id` IS NOT NULL, 1, 0) AS '是否充值',
        IFNULL(investtemp.`money`, 0.00) AS '投资情况',
        t.`referrer` AS '推荐人',
        u.`mobile_number`,
        IF (
          b.`status` = 'passed',
          1,
          0
        ) AS '用户是否绑卡',
        IF (
          b1.`status` = 'passed',
          1,
          0
        ) AS '推荐人是否绑卡'
      FROM
        `user` t
        LEFT JOIN trusteeship_account m
          ON t.`id` = m.`user_id`
        LEFT JOIN
          (SELECT DISTINCT
             x.`mobile_number`,
             x.`id`
           FROM
             `user` x
           JOIN `user` y
           ON x.`id` = y.`referrer`) u
          ON t.`referrer` = u.`id`
        LEFT JOIN bank_card b
          ON t.`id` = b.`user_id` AND b.`status` = 'passed'
        LEFT JOIN `bank_card` b1
          ON t.`referrer` = b1.`user_id` AND b1.`status` = 'passed'
        LEFT JOIN recharge n
          ON t.`id` = n.`user_id`
          AND n.`status` = 'success'
        LEFT JOIN
          (SELECT
            i.`user_id`,
            ROUND(SUM(i.`invest_money`), 2) AS 'money'
          FROM
            invest i
          WHERE i.`status` NOT IN ('test', 'cancel', 'wait_affirm', 'unfinished')
          GROUP BY i.`user_id`) investtemp
          ON t.`id` = investtemp.`user_id`
      WHERE t.`register_time` BETWEEN '2015-07-01'
        AND '{0}'
      ORDER BY t.`register_time`) temp,
      (SELECT
        @rownum := 0) AS rownum
"""


def build_sql():
    now = datetime.now()
    return SQL.format(now)


def query(host, user_name, password, db_name):
    import MySQLdb

    try:
        db = MySQLdb.connect(host, user_name, password, db_name)
        cursor = db.cursor()
        sql = build_sql()
        cursor.execute(sql)
        return cursor.fetchall()
    except Exception:
        logger.exception("db error")
    finally:
        db.close()


def build_csv(data):
    csvfile = StringIO.StringIO()
    csvwriter = csv.writer(csvfile)
    [csvwriter.writerow(row) for row in data]
    return csvfile.getvalue()


def send_mail(data):
    # Send the email (this example assumes SMTP authentication is required)
    import smtplib
    from email.MIMEMultipart import MIMEMultipart
    from email.MIMEText import MIMEText

    strFrom = 'no-reply@tuotiansudai.com'
    msg = MIMEMultipart()
    msg["From"] = strFrom
    msg["To"] = ', '.join(RECIPIENT)
    if data:
        msg["Subject"] = u"[七月推广活动]{0}报告".format(datetime.now())
        attachment = MIMEText(data, _subtype='octet-stream')
        attachment.add_header("Content-Disposition", "attachment", filename="report.csv")
        msg.attach(attachment)
    else:
        msg["Subject"] = u"[七月推广活动]{0}报告, 无新用户".format(datetime.now())

    smtp = smtplib.SMTP()
    smtp.connect('smtp.exmail.qq.com')
    smtp.login('no-reply@tuotiansudai.com', 'w62CQIhM6acj')
    smtp.sendmail(strFrom, RECIPIENT, msg.as_string())
    smtp.quit()


def main(host="localhost", user_name="root", password="", db="tuotiansudai"):
    try:
        logger.info('start')
        data = query(host, user_name, password, db)
        logger.info("total new count is {0}".format(len(data)))
        csv_data = build_csv(data)
        send_mail(csv_data)
        logger.info('done')
    except Exception as e:
        logger.error(e)


if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser(description='July event')
    parser.add_argument('--host', nargs='?', help='host name', default='localhost')
    parser.add_argument('--user', nargs='?', help='user name', default='root')
    parser.add_argument('--password', nargs='?', help='password', default='')
    parser.add_argument('--db', nargs='?', help='db name', default='tuotiansudai')
    args = parser.parse_args()
    main(args.host, args.user, args.password, args.db)