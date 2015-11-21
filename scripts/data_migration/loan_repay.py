from scripts.data_migration.base import BaseMigrate


class LoanRepayMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT loan_id, corpus, interest, default_interest, period, repay_day, status, time FROM loan_repay"
    # insert sql which is executed on aa db
    INSERT_SQL = '''INSERT INTO loan_repay(`id`,
                                           `loan_id`,
                                           `corpus`,
                                           `expected_interest`,
                                           `actual_interest`,
                                           `default_interest`,
                                           `period`,
                                           `repay_date`,
                                           `actual_repay_date`,
                                           `status`,
                                           `created_time`)
                   VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)'''

    _index = 0

    STATUS_MAPPING = {'complete': 'COMPLETE',
                      'wait_repay_verify': 'CONFIRMING',
                      'repaying': 'REPAYING',
                      'overdue': 'OVERDUE'}

    def generate_params(self, old_row):
        self._index += 1
        _, cursor = self.new_db.execute("(select id from loan where old_id='%s')" % old_row['loan_id'])

        temp_row = cursor.fetchone()
        return (self._index,
                temp_row['id'],
                old_row['corpus'],
                old_row['interest'],
                old_row['interest'] if old_row['status'] == 'complete' else None,
                old_row['default_interest'],
                old_row['period'],
                old_row['repay_day'],
                old_row['repay_day'] if old_row['status'] == 'complete' else None,
                self.STATUS_MAPPING[old_row['status']],
                old_row['time'])

    def before(self):
        pass
