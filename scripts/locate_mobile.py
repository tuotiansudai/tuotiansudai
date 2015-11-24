#! /usr/bin/env python
# -*- coding:utf-8 -*-

import MySQLdb
import json
from urllib import urlopen

MYSQL_HOST = '192.168.33.10'
MYSQL_USER = 'root'
MYSQL_PASS = ''
MYSQL_DB = 'tuotiansudai'
MYSQL_QUERY = 'select username, mobile_number from user order by register_time desc'

SEARCH_PROVINCE = '河北'
SEARCH_CITY = '石家庄'
SEARCH_LIMIT = 500;

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
    return (json_data['data']['province'],json_data['data']['city'])

def print_if_match(hit_index, user_name, phone, (province, city)):

    if city.encode('utf-8') == SEARCH_CITY and province.encode('utf-8') == SEARCH_PROVINCE :
        print hit_index+1, phone, province, city, user_name
        return True
    else:
        return False


def do_search():
    users = query_all_user()
    hit_count = 0
    for (user_name, phone) in users:
        if hit_count >= SEARCH_LIMIT:
            break

        if len(phone) == 11:
            locate = query_locate(phone)
            if print_if_match(hit_count, user_name, phone, locate):
                hit_count = hit_count + 1

    print 'search finished, %s users found' % hit_count

if __name__ == '__main__':
    do_search()
