# coding: utf-8
import time
import MySQLdb
import getopt
import sys
from MySQLdb import cursors


class DBWrapper(object):
    def __init__(self, host, user, password, db_name):
        self.db_name = db_name
        self.password = password
        self.user = user
        self.host = host
        self.conn = None
        self.cursor = None

    def execute(self, sql, params=None, is_many=False):
        # logger.debug(sql)
        # logger.debug(params)
        cursor = self.get_cursor()
        if is_many:
            cursor.executemany(sql, params)
        else:
            cursor.execute(sql, params)
        return cursor

    def get_cursor(self):
        if not self.conn:
            self.conn = MySQLdb.connect(host=self.host,
                                        user=self.user,
                                        passwd=self.password, db=self.db_name, charset='utf8')
            if not self.cursor:
                self.cursor = self.conn.cursor()
        return self.cursor

    def commit(self):
        self.conn.commit()

    def close(self):
        self.cursor.close()
        self.conn.close()


class MembershipDataFiller(object):
    def __init__(self, username, password, host, db_name):
        self.db_wrapper = DBWrapper(host=host, user=username, password=password, db_name=db_name)
        self.db_wrapper.get_cursor()
        self.level_border = []
        self.__get_vip_level_border()

    def close(self):
        self.db_wrapper.close()

    def __update_account(self, login_name, membership_point):
        sql = '''UPDATE `aa`.`account` SET membership_point = %s WHERE login_name = %s'''
        self.db_wrapper.execute(sql, (membership_point, login_name))
        self.db_wrapper.commit()

    def __set_user_membership(self, login_name, membership_id, expired_time, created_time):
        sql = '''INSERT INTO `aa`.`user_membership` (login_name, membership_id, expired_time, created_time) VALUES (%s, %s, %s, %s)'''
        self.db_wrapper.execute(sql, (login_name, membership_id, expired_time, created_time))
        self.db_wrapper.commit()

    def __set_membership_experience_bill(self, login_name, experience, total_experience, invest_time, loan_id, amount):
        description = u"%s在%s成功投资编号%s项目%s元,获得成长值%s点" % (str(login_name[0]), invest_time, loan_id, amount, experience)
        created_time = time.strftime("%Y-%m-%d %X", time.localtime())
        sql = '''INSERT INTO `aa`.`membership_experience_bill` (login_name, experience, total_experience, created_time,
description) VALUES (%s,%s,%s,%s,%s)'''
        self.db_wrapper.execute(sql, (login_name, experience, total_experience, created_time, description))
        self.db_wrapper.commit()

    def __set_membership_experience_bill_level_up(self, login_name, total_experience, invest_time, level, is_init):
        if is_init:
            description = u"%s注册成功,获得V0" % (str(login_name[0]))
        else:
            description = u"%s在%s累计获得经验值%s点,升级为V%s" % (str(login_name[0]), invest_time, total_experience, level)
        created_time = time.strftime("%Y-%m-%d %X", time.localtime())
        sql = '''INSERT INTO `aa`.`membership_experience_bill` (login_name, experience, total_experience, created_time,
        description) VALUES (%s,%s,%s,%s,%s)'''
        self.db_wrapper.execute(sql, (login_name, 0L, total_experience, created_time, description))
        self.db_wrapper.commit()

    def __find_all_invests_by_login_name(self, login_name):
        sql = '''SELECT id, loan_id, transfer_invest_id, amount, invest_time FROM `aa`.`invest` WHERE login_name = %s'''
        return self.db_wrapper.execute(sql, login_name).fetchall()

    def __get_vip_level_border(self):
        sql = '''SELECT id, level, experience FROM `aa`.`membership`'''
        for id, level, experience in self.db_wrapper.execute(sql):
            self.level_border.append((id, level, experience))

    def __get_vip_id_by_experience(self, experience):
        user_level = -1
        membership_id = 0
        for id, level, level_experience in self.level_border:
            if user_level < level and experience >= level_experience:
                user_level = level
                membership_id = id
        return membership_id, user_level

    def __fill_membership_data(self, login_name):
        invests = self.__find_all_invests_by_login_name(login_name)
        invests = sorted(invests, key=lambda x: x[-1])
        total_amount = 0
        user_level = 0
        self.__set_membership_experience_bill_level_up(login_name, 0L, None, 0L, True)
        for invest in invests:
            transfer_invest_id = invest[2]
            if transfer_invest_id is None:
                continue
            loan_id = invest[1]
            amount = invest[3]
            invest_time = invest[4]
            total_amount += (amount / 100)
            self.__set_membership_experience_bill(login_name, amount / 100, total_amount, invest_time, loan_id,
                                                  float(amount) / 100.0)
            current_level = self.__get_vip_id_by_experience(total_amount)[1]
            if current_level > user_level:
                self.__set_membership_experience_bill_level_up(login_name, total_amount, invest_time, current_level,
                                                               False)
                user_level = current_level
        membership_id = self.__get_vip_id_by_experience(total_amount)
        self.__set_user_membership(login_name, membership_id[0], '2035-12-31 0:0:0',
                                   time.strftime("%Y-%m-%d %X", time.localtime()))
        self.__update_account(login_name, total_amount)

    def __find_all_login_names(self):
        sql = '''SELECT login_name FROM `aa`.`user`'''
        return self.db_wrapper.execute(sql).fetchall()

    def run(self):
        login_names = self.__find_all_login_names()
        for login_name in login_names:
            self.__fill_membership_data(login_name)


class InvestFeeRateMover(object):
    def __init__(self, username, password, host, db_name):
        self.db_wrapper = DBWrapper(user=username, password=password, host=host, db_name=db_name)

    def close(self):
        self.db_wrapper.close()

    def __get_all_invests(self):
        sql = '''SELECT id, loan_id FROM `aa`.`invest`'''
        return self.db_wrapper.execute(sql).fetchall()

    def __get_fee_rate_from_loan(self, loan_id):
        sql = '''SELECT invest_fee_rate FROM `aa`.`loan` WHERE id = %s'''
        return self.db_wrapper.execute(sql, (loan_id,)).fetchall()

    def __set_fee_rate_in_invest(self, invest_id, fee_rate):
        sql = '''UPDATE `aa`.`invest` SET invest_fee_rate = %s WHERE id = %s'''
        self.db_wrapper.execute(sql, (fee_rate, invest_id))
        self.db_wrapper.commit()

    def __remove_loan_invest_fee_rate_column(self):
        sql = '''ALTER TABLE `aa`.`loan` DROP COLUMN `invest_fee_rate`'''
        self.db_wrapper.execute(sql)
        self.db_wrapper.commit()

    def run(self):
        invests = self.__get_all_invests()
        for invest_id, loan_id in invests:
            fee_rate = self.__get_fee_rate_from_loan(loan_id=loan_id)
            self.__set_fee_rate_in_invest(invest_id, fee_rate)
        self.__remove_loan_invest_fee_rate_column()


def main():
    opts, args = getopt.getopt(sys.argv[1:], "u:p:h:d:")
    for key, value in opts:
        if '-u' == key:
            username = value
        elif '-p' == key:
            password = value
        elif '-h' == key:
            host = value
        elif '-d' == key:
            db_name = value
    membership_data_filler = MembershipDataFiller(username=username, password=password, host=host, db_name=db_name)
    try:
        membership_data_filler.run()
    finally:
        membership_data_filler.close()

    invest_fee_rate_mover = InvestFeeRateMover(username=username, password=password, host=host, db_name=db_name)
    try:
        invest_fee_rate_mover.run()
    finally:
        invest_fee_rate_mover.close()


if __name__ == '__main__':
    main()
