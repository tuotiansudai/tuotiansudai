import hashlib
import uuid
from scripts.data_migration.base import BaseMigrate


class UserMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT id, password, email, mobile_number, register_time, referrer, channel, source FROM user limit %s, %s"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO user(`login_name`, `password`, `email`, `mobile`, `register_time`, `referrer`, `status`, `salt`, `channel`, `source`) VALUES(%s, %s, %s,%s, %s, %s,%s, %s, %s, %s)"

    def generate_params(self, old_row):
        salt = uuid.uuid4().hex
        new_password = hashlib.sha1('%s{%s}' % (old_row['password'], salt)).hexdigest()

        return old_row['id'], new_password, old_row['email'], old_row['mobile_number'], old_row['register_time'], old_row['referrer'], 'ACTIVE', salt, old_row['channel'], old_row['source']
