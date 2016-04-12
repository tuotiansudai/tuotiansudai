from scripts.data_migration.base import BaseMigrate


class SystemBillMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT money, type, reason, detail, time FROM system_bill WHERE reason IN ('activity_reward', 'binding_card', 'invest_fee', 'replace_card', 'referrer_reward') order by time, seq_num limit %s, %s"
    # insert sql which is executed on aa db
    INSERT_SQL = '''INSERT INTO system_bill(`id`, `amount`, `operation_type`, `business_type`, `detail`, `created_time`)
                    VALUES(%s, %s, %s, %s, %s, %s)'''

    BUSINESS_TYPE_MAPPING = {'activity_reward': 'ACTIVITY_REWARD',
                             'binding_card': 'BIND_BANK_CARD',
                             'invest_fee': 'INVEST_FEE',
                             'replace_card': 'REPLACE_BANK_CARD',
                             'referrer_reward': 'REFERRER_REWARD'}

    _index = 0

    def generate_params(self, old_row):
        self._index += 1

        return (self._index,
                int(round(old_row['money'] * 100)),
                old_row['type'].upper(),
                self.BUSINESS_TYPE_MAPPING[old_row['reason']],
                old_row['detail'],
                old_row['time'])

    def before(self):
        pass
