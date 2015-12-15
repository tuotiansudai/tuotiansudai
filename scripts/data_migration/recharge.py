from scripts.data_migration.base import BaseMigrate


class RechargeMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT user_id, actual_money, fee, recharge_way, status, source, time, channel FROM recharge ORDER BY time ASC limit %s, %s"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO recharge(`id`, `login_name`, `amount`, `fee`, `bank_code`, `status`, `source`, `fast_pay`, `created_time`, `channel`) " \
                 "VALUES(%s, %s, %s, %s, %s, %s,%s, %s, %s, %s)"

    _index = 0

    def generate_params(self, old_row):
        self._index += 1

        return (self._index, old_row['user_id'].lower(), int(round(old_row['actual_money'] * 100)), int(round(old_row['fee'] * 100)), old_row['recharge_way'].upper(),
                old_row['status'].upper(), old_row['source'].upper(), 0, old_row['time'], old_row['channel'])

    def before(self):
        pass
