from scripts.data_migration.base import BaseMigrate


class UserBillMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT user_id, money, balance, frozen_money, type, type_info, time FROM user_bill;"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO user_bill(`login_name`, `amount`, `balance`, `freeze`, `operation_type`, `business_type`, `created_time`) VALUES(%s, %s, %s, %s, %s, %s, %s)"

    OPERATION_TYPE_MAPPING = {'to_balance': 'TO_BALANCE',
                              'ti_balance': 'TI_BALANCE',
                              'freeze': 'FREEZE',
                              'unfreeze': 'UNFREEZE',
                              'to_frozen': 'TO_FREEZE'}

    BUSINESS_TYPE_MAPPING = {'activity_reward': 'ACTIVITY_REWARD',
                             'admin_operation': 'ADMIN_INTERVENTION',
                             'advance_repay': 'ADVANCE_REPAY',
                             'apply_withdraw': 'APPLY_WITHDRAW',
                             'cancel_loan': 'CANCEL_INVEST_PAYBACK',
                             'give_money_to_borrower': 'LOAN_SUCCESS',
                             'invest_fee': 'INVEST_FEE',
                             'invest_success': 'INVEST_SUCCESS',
                             'normal_repay': 'NORMAL_REPAY',
                             'overdue_repay': 'OVERDUE_REPAY',
                             'recharge_success': 'RECHARGE_SUCCESS',
                             'referrer_reward': 'REFERRER_REWARD',
                             'refuse_apply_withdraw': 'WITHDRAW_FAIL',
                             'withdraw_success': 'WITHDRAW_SUCCESS'}

    def generate_params(self, old_row):
        return (old_row['user_id'].lower(),
                int(round(old_row['money'] * 100)),
                int(round(old_row['balance'] * 100)),
                int(round(old_row['frozen_money'] * 100)),
                self.OPERATION_TYPE_MAPPING[old_row['type']],
                self.BUSINESS_TYPE_MAPPING[old_row['type_info']],
                old_row['time'])

    def before(self):
        pass
