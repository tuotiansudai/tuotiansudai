#coding:utf-8
import re
from scripts.data_migration.base import BaseMigrate

class LoanMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT \
                      loan.id, \
                      loan.name, \
                      loan.user_id AS agent_login_name, \
                      IFNULL(loan.agent,loan.user_id) AS loaner_login_name, \
                          user.realname AS loaner_user_name, \
                          user.id_card AS loaner_identity_number,  \
                      CASE \
                        WHEN loan.type = 'loan_type_1' \
                        THEN 'INVEST_INTEREST_MONTHLY_REPAY' \
                        WHEN loan.type = 'loan_type_3' \
                        THEN 'LOAN_INTEREST_MONTHLY_REPAY' \
                        WHEN loan.type = 'loan_type_4' \
                        THEN 'LOAN_INTEREST_LUMP_SUM_REPAY' \
                        WHEN loan.type = 'loan_type_5' \
                        THEN 'INVEST_INTEREST_LUMP_SUM_REPAY' \
                      END AS type, \
                      loan.deadline AS periods, \
                          loan.description AS description_html, \
                          ROUND(loan.loan_money * 100) AS loan_money, \
                          IFNULL(loan.investor_fee_rate, 0) AS invest_fee_rate, \
                          ROUND(loan.min_invest_money * 100) AS min_invest_money, \
                          ROUND(loan.max_invest_money * 100) AS max_invest_money, \
                          ROUND(loan.cardinal_number * 100) AS invest_increasing_amount, \
                          CASE \
                            WHEN loan.loan_activity_type = 'pt' \
                            THEN 'NORMAL' \
                            WHEN loan.loan_activity_type = 'xs' \
                            THEN 'NEWBIE' \
                            WHEN loan.loan_activity_type = 'jx' \
                            THEN 'PROMOTION' \
                            WHEN loan.loan_activity_type = 'dx' \
                            THEN 'EXCLUSIVE' \
                          END AS activity_type, \
                          loan.jk_rate AS base_rate, \
                          loan.hd_rate AS activity_rate, \
                          789098123 AS contract_id, \
                          IFNULL(loan.invest_begin_time, loan.commit_time) AS fundraising_start_time, \
                          IFNULL(loan.expect_time, loan.commit_time) AS fundraising_end_time, \
                          loan.verify_time AS raising_complete_time, \
                          loan.verify_time, \
                          loan.give_money_time AS recheck_time, \
                          CASE\
                            WHEN loan.status = 'waiting_verify' \
                            THEN 'WAITING_VERIFY' \
                            WHEN loan.status = 'raising' \
                            THEN 'RAISING' \
                            WHEN loan.status = 'recheck' \
                            THEN 'RECHECK' \
                            WHEN loan.status = 'cancel' \
                            THEN 'CANCEL' \
                            WHEN loan.status = 'repaying' \
                            THEN 'REPAYING' \
                            WHEN loan.status = 'overdue' \
                            THEN 'OVERDUE' \
                            WHEN loan.status = 'complete' \
                            THEN 'COMPLETE' \
                          END AS status, \
                          IF( \
                            loan_node_attr.node_attr_id = 'index', \
                            TRUE, \
                            FALSE \
                          ) AS show_on_home, \
                          loan.commit_time AS created_time, \
                          loan.commit_time AS update_time \
                        FROM \
                          loan \
                          LEFT JOIN loan_node_attr \
                            ON loan.id = loan_node_attr.loan_id  \
                          LEFT JOIN user \
                            ON loan.agent = user.id \
                        WHERE loan.status NOT IN ('test', 'verify_fail') AND loan.type != 'loan_type_2' "

    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO loan VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);"

    _dr = re.compile(r'<[^>]+>',re.S)

    def generate_params(self, old_row):

        description_text = self._dr.sub('', old_row['description_html'])

        return old_row['id'],old_row['name'],old_row['agent_login_name'],old_row['loaner_login_name'],old_row['loaner_user_name'],old_row['loaner_identity_number'],old_row['type'],old_row['periods'],description_text, \
                old_row['description_html'],old_row['loan_money'],old_row['invest_fee_rate'],old_row['min_invest_money'],old_row['max_invest_money'], \
                old_row['invest_increasing_amount'],old_row['activity_type'],old_row['base_rate'],old_row['activity_rate'],old_row['contract_id'], \
                old_row['fundraising_start_time'],old_row['fundraising_end_time'],old_row['raising_complete_time'],old_row['verify_time'],old_row['recheck_time'],\
                old_row['status'],old_row['show_on_home'],old_row['created_time'],old_row['update_time']

    def before(self):
      pass