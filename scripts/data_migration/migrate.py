import argparse
import importlib
from scripts.data_migration.base import DBWrapper
from scripts.data_migration.constants import HOST, USERNAME, PASSWORD, ORIGINAL_DB, AA_DB

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Data migration tool')
    parser.add_argument('-t', '--table', required=True, help='New table name')
    args = parser.parse_args()

    old_db = DBWrapper(host=HOST, user=USERNAME, password=PASSWORD, db_name=ORIGINAL_DB)
    new_db = DBWrapper(host=HOST, user=USERNAME, password=PASSWORD, db_name=AA_DB)

    # tables = ['user', 'account', 'announce', 'audit_log', 'bank_card', 'loan', 'loan_title_relation', 'invest', 'invest_referrer_reward', 'invest_repay', 'loan_repay', 'recharge system_bill', 'user_bill', 'user_role', 'withdraw']

    # for table in tables:
    cls = getattr(importlib.import_module(args.table), '{0}Migrate'.format(args.table.title().replace('_', '')))
    cls(old_db, new_db).migrate()

