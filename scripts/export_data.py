from collections import namedtuple
import csv

CSV_FILE = '/workspace/temp/data.csv'
Reward = namedtuple('Reward', ['user_id', 'id_reward', 'recharge_reward', 'referral', 'referral_id_reward', 'referral_recharge_reward', 'referral_invest_reward'])


def parse_csv():
    results = []
    with open(CSV_FILE, 'rb') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            _, _, user_id, _, _, _, id_reward, _, _, recharge_reward, _, referral, _, _, referral_id_reward, _, referral_recharge_reward, _, referral_invest_reward, _ = row
            if referral == '':
                referral = None
            reward = Reward(user_id, id_reward == '1', recharge_reward == '1', referral, referral_id_reward == '1',
                            referral_recharge_reward == '1', referral_invest_reward == '1')
            results.append(reward)
    return results


def build_sql(row):
    sql_template = "INSERT INTO july_activity_reward(user_id, referrer_id, certified_reward, referrer_certified_reward, first_recharge_reward, referrer_first_recharge_reward, referrer_first_invest_reward) VALUES(%s,%s,%s,%s,%s,%s,%s)"
    values = (
    row.user_id, row.referral, row.id_reward, row.referral_id_reward, row.recharge_reward, row.referral_recharge_reward,
    row.referral_invest_reward)
    return sql_template, values


def insert(data, host, user_name, password, db_name):
    import MySQLdb

    try:
        db = MySQLdb.connect(host, user_name, password, db_name)
        cursor = db.cursor()
        for row in data:
            sql, values = build_sql(row)
            cursor.execute(sql, values)
        db.commit()
    except Exception as e:
        print e
    finally:
        db.close()


if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='July event')
    parser.add_argument('--host', nargs='?', help='host name', default='localhost')
    parser.add_argument('--user', nargs='?', help='user name', default='root')
    parser.add_argument('--password', nargs='?', help='password', default='')
    parser.add_argument('--db', nargs='?', help='db name', default='tuotiansudai')
    args = parser.parse_args()

    csv_result = parse_csv()
    insert(csv_result, args.host, args.user, args.password, args.db)
