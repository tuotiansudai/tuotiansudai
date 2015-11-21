from scripts.data_migration.base import BaseMigrate


class WithdrawMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT invest_id, period, corpus, interest, default_interest, fee, repay_day, status, time FROM invest_repay WHERE status IN ['complete', 'repaying']"
    # insert sql which is executed on aa db
    INSERT_SQL = '''INSERT INTO withdraw(`id`,
                                         `invest_id`,
                                         `corpus`,
                                         `expected_interest`,
                                         `actual_interest`,
                                         `default_interest`,
                                         `expected_fee`,
                                         `actual_fee`,
                                         `period`,
                                         `repay_date`,
                                         `actual_repay_date`,
                                         `status`,
                                         `created_time`)
                 VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)'''

    STATUS_MAPPING = {'complete': 'COMPLETE',
                      'repaying': 'REPAYING',
                      'overdue': 'OVERDUE'}

    _index = 0

    def generate_params(self, old_row):
        self._index += 1

        _, cursor = self.new_db.execute("select id from invest where old_id='%s'" % old_row['invest_id'])

        invest_id = cursor.fetchone()['id']

        return (self._index,
                invest_id,
                int(round(old_row['corpus'] * 100)),
                int(round(old_row['interest'] * 100)),
                int(round(old_row['interest'] * 100)) if old_row['status'] == 'complete' else None,
                int(round(old_row['default_interest'] * 100)),
                int(round(old_row['fee'] * 100)),
                int(round(old_row['fee'] * 100)) if old_row['status'] == 'complete' else None,
                old_row['period'],
                old_row['repay_day'],
                old_row['repay_day'] if old_row['status'] == 'complete' else None,
                self.STATUS_MAPPING[old_row['status']].upper(),
                old_row['time'])

    def before(self):
        pass
