#! /usr/bin/env python
# -*- coding:utf-8 -*-

import MySQLdb
import json
import csv
from urllib import urlopen

MYSQL_HOST = '192.168.33.10'
MYSQL_USER = 'root'
MYSQL_PASS = ''
MYSQL_DB = 'tuotiansudai'

SEARCH_TIME_BEGIN = '2015-12-08 00:00:00'
SEARCH_TIME_END = '2015-12-09 00:00:00'

#MYSQL_QUERY = 'select username, mobile_number, realname, register_time from user order by register_time desc'
MYSQL_QUERY = 'select username, mobile_number, realname, register_time from user where register_time between \''+SEARCH_TIME_BEGIN+'\' and \''+SEARCH_TIME_END+'\' order by register_time asc'

SEARCH_PROVINCE = '河北'
SEARCH_CITY = '石家庄'
SEARCH_LIMIT = 500;

OUTPUT_ENCODING = 'GBK'
OUTPUT_FILE_PATH = 'matched_user.csv'

matched_csv_writer = None

def query_all_user():
    conn = MySQLdb.connect(host=MYSQL_HOST, user=MYSQL_USER, passwd=MYSQL_PASS, db=MYSQL_DB, charset='utf8')
    cursor = conn.cursor()
    cursor.execute(MYSQL_QUERY)
    row = cursor.fetchone()
    while row:
        yield row
        row = cursor.fetchone()

def query_locate(phone):
    url = 'http://cx.shouji.360.cn/phonearea.php?number=%s' % phone
    text = urlopen(url).read() 
    json_data = json.loads(text)
    if(json_data['code'] == 0):
        return (json_data['data']['province'],json_data['data']['city'])
    else:
        return ('','')

def is_match_city(province, city):
    return city.encode('utf-8') == SEARCH_CITY and province.encode('utf-8') == SEARCH_PROVINCE

def e(s):
    if s == None:
        return None
    else:
        return s.encode(OUTPUT_ENCODING)

def print_if_match(hit_index, (user_name, phone, realname, register_time), (province, city)):
    if is_match_city(province, city):
        print hit_index,user_name,phone,register_time
        output_matched_data((hit_index+1, e(phone), e(province), e(city), e(realname), user_name, register_time))
        return True
    else:
        return False

def output_matched_data(data):
    global matched_csv_writer
    matched_csv_writer.writerow(data)

def do_init():
    global matched_csv_writer
    matched_csv_writer = csv.writer(open(OUTPUT_FILE_PATH,'w'))
    matched_csv_writer.writerow((e(u'序号'),e(u'手机号'),e(u'省份'),e(u'城市'),e(u'姓名'),e(u'用户名'),e(u'注册时间')))

def do_search():
    users = query_all_user()
    hit_count = 0
    for user in users:
        phone = user[1]
        if len(phone) == 11:
            locate = query_locate(phone)
            if print_if_match(hit_count, user, locate):
                hit_count = hit_count + 1

        if hit_count >= SEARCH_LIMIT:
            break

    print 'search finished, %s users found' % hit_count

if __name__ == '__main__':
    do_init()
    do_search()
