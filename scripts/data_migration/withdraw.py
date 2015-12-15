import redis
from scripts.data_migration.base import BaseMigrate
from scripts.data_migration.constants import REDIS_HOST, REDIS_PORT, REDIS_DB

class WithdrawMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT user_id, bank_card_id, money, fee, verify_message, verify_time, recheck_message, recheck_time, time, status, source FROM withdraw_cash_temp ORDER BY time limit %s, %s"

    # insert sql which is executed on aa db
    INSERT_SQL = '''INSERT INTO withdraw(`id`,
                                          `bank_card_id`,
                                          `login_name`,
                                          `amount`,
                                          `fee`,
                                          `apply_notify_message`,
                                          `apply_notify_time`,
                                          `notify_message`,
                                          `notify_time`,
                                          `created_time`,
                                          `status`,
                                          `source`)
                 VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)'''

    STATUS_MAPPING = {
        'wait_verify': 'WAIT_PAY',
        'recheck': 'APPLY_SUCCESS',
        'verify_fail': 'APPLY_FAIL',
        'success': 'SUCCESS',
        'recheck_fail': 'FAIL',
    }

    _index = 0
    _r = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)

    def generate_params(self, old_row):
        self._index += 1

        new_id=self._r.get('bankcard_'+old_row['bank_card_id']);

        return (self._index,
                new_id,
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
