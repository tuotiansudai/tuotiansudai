from scripts.data_migration.base import BaseMigrate


class InvestReferrerRewardMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT invest_id, bonus, referrer_id, status, role_name, time FROM invest_userReferrer"
    # insert sql which is executed on aa db
    INSERT_SQL = '''INSERT INTO invest_referrer_reward(`id`, `invest_id`, `amount`, `referrer_login_name`, `referrer_role`, `status`, `created_time`)
                    VALUES(%s, %s, %s, %s, %s, %s, %s)'''

    STATUS_MAPPING = {
        'wait_verify': 'WAIT_PAY',
        'recheck': 'APPLY_SUCCESS',
        'verify_fail': 'APPLY_FAIL',
        'success': 'SUCCESS',
        'recheck_fail': 'FAIL',
    }

    _index = 0

    def generate_params(self, old_row):
        self._index += 1

        _, cursor = self.new_db.execute("(select id from invest where old_id='%s')" % old_row['invest_id'])

        temp_row = cursor.fetchone()

        return (self._index,
                temp_row['id'],
                old_row['user_id'].lower(),
                int(round(old_row['money'] * 100)),
                int(round(old_row['fee'] * 100)),
                old_row['verify_message'],
                old_row['verify_time'],
                old_row['recheck_message'],
                old_row['recheck_time'],
                old_row['time'],
                self.STATUS_MAPPING[old_row['status']].upper(),
                old_row['source'].upper())

    def before(self):
        pass
