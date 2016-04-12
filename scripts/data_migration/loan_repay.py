from scripts.data_migration.base import BaseMigrate


class LoanRepayMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT loan_id, corpus, interest, default_interest, period, repay_day, status, IFNULL(time,now()) as time FROM loan_repay WHERE status <> 'test' and loan_id not in (select l.id from loan l where l.status in ('verify_fail','test') or l.type='loan_type_2')"
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
                      'wait_repay_verify': 'WAIT_PAY',
                      'repaying': 'REPAYING',
                      'overdue': 'OVERDUE'}

    def generate_params(self, old_row):
        self._index += 1

        return (self._index,
                old_row['loan_id'],
                int(round(old_row['corpus'] * 100)),
                int(round(old_row['interest'] * 100)),
                int(round(old_row['interest'] * 100)) if old_row['status'] == 'complete' else None,
                int(round(old_row['default_interest'] * 100)),
                old_row['period'],
                old_row['repay_day'],
                old_row['repay_day'] if old_row['status'] == 'complete' else None,
                self.STATUS_MAPPING[old_row['status']],
                old_row['time'])

    def before(self):
        pass
