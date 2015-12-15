from scripts.data_migration.base import BaseMigrate


class InvestReferrerRewardMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT invest_id, bonus, referrer_id, status, role_name, time FROM invest_userreferrer where invest_id not in (select id from invest where status='test') order by time ASC "
    # insert sql which is executed on aa db
    INSERT_SQL = '''INSERT INTO invest_referrer_reward(`id`, `invest_id`, `amount`, `referrer_login_name`, `referrer_role`, `status`, `created_time`)
                    VALUES(%s, %s, %s, %s, %s, %s, %s)'''

    STATUS_MAPPING = {
        'fail': 'FAILURE',
        'success': 'SUCCESS'
    }

    ROLE_MAPPING = {'INVESTOR': 'INVESTOR',
                    'ROLE_MERCHANDISER': 'STAFF'}

    _index = 0

    def generate_params(self, old_row):
        self._index += 1

        return (self._index,
                old_row['invest_id'],
                int(round(old_row['bonus'] * 100)),
                old_row['referrer_id'].lower(),
                self.ROLE_MAPPING[old_row['role_name']],
                self.STATUS_MAPPING[old_row['status']],
                old_row['time'])

    def before(self):
        pass
