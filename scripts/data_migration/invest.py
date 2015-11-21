from scripts.data_migration.base import BaseMigrate


class WithdrawMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT id, loan_id, user_id, invest_money, status, source, channel, time FROM invest WHERE status <> 'test'"
    # insert sql which is executed on aa db
    INSERT_SQL = '''INSERT INTO invest(`id`, `loan_id`, `login_name`, `amount`, `status`, `source`, `channel`, `created_time`)
                    VALUES(%s, %s, %s, %s, %s, %s, %s, %s)'''

    STATUS_MAPPING = {'wait_affirm': 'WAIT_PAY',
                      'bid_success': 'SUCCESS',
                      'cancel': 'CANCEL_INVEST_PAYBACK',
                      'complete': 'SUCCESS',
                      'overdue': 'SUCCESS',
                      'repaying': 'SUCCESS',
                      'unfinished': 'FAIL'}

    _index = 0

    def generate_params(self, old_row):
        self._index += 1

        _, cursor = self.new_db.execute("(select id from loan where old_id='%s')" % old_row['loan_id'])

        loan_id = cursor.fetchone()['id']

        return (self._index,
                loan_id,
                old_row['user_id'].lower(),
                int(round(old_row['invest_money'] * 100)),
                self.STATUS_MAPPING[old_row['status']].upper(),
                old_row['source'].upper(),
                old_row['channel'].upper(),
                old_row['time'].upper())

    def before(self):
        pass
