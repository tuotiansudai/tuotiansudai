import redis
from scripts.data_migration.base import BaseMigrate
from scripts.data_migration.constants import REDIS_HOST, REDIS_PORT, REDIS_DB

class BankCardMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT id, user_id, bank_no, card_no, status, is_open_fastPayment, time FROM bank_card;"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO bank_card(`id`, `login_name`, `bank_code`, `card_number`, `status`, `is_fast_pay_on`, `created_time`) " \
                 "VALUES(%s, %s, %s, %s, %s, %s, %s);"

    STATUS_MAPPING = {'passed': 'PASSED',
                      'remove': 'REMOVED',
                      'unchecked': 'UNCHECKED'}

    _index = 0

    _r = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)

    def generate_params(self, old_row):
        self._index += 1

        self._r.set('bankcard_'+old_row['id'],self._index);

        return (self._index,
                old_row['user_id'],
                old_row['bank_no'],
                old_row['card_no'],
                self.STATUS_MAPPING.get(old_row['status'], ''),
                old_row['is_open_fastPayment'],
                old_row['time'])

    def before(self):
        pass
