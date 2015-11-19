from scripts.data_migration.base import BaseMigrate


class AccountMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = '''SELECT trusteeship_account.user_id AS login_name,
                            user.realname AS user_name,
                            user.id_card AS identity_number,
                            trusteeship_account.id AS pay_user_id,
                            trusteeship_account.account_id AS pay_account_id,
                            user_bill.balance AS balance,
                            user_bill.frozen_money AS freeze,
                            trusteeship_account.create_time AS register_time
                    FROM trusteeship_account
                    JOIN user ON trusteeship_account.user_id=user.id
                    JOIN user_bill ON user_bill.user_id=user.id AND user_bill.seq_num=(SELECT max(seq_num) FROM user_bill WHERE user_id=user.id)
     '''
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO account(`login_name`, `user_name`, `identity_number`, `pay_user_id`, `pay_account_id`, `balance`, `freeze`, `register_time`) " \
                 "VALUES(%s, %s, %s, %s, %s, %s, %s, %s)"

    def generate_params(self, old_row):
        old_row['balance'] = int(round(old_row['balance'] * 100))
        old_row['freeze'] = int(round(old_row['freeze'] * 100))

        return old_row['login_name'], old_row['user_name'], old_row['identity_number'], old_row['pay_user_id'], old_row['pay_account_id'], old_row['balance'], old_row['freeze'], old_row['register_time']

    def before(self):
        pass
