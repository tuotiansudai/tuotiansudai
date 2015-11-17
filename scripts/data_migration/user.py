import hashlib
import uuid
from scripts.data_migration.base import BaseMigrate


class UserMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT * FROM user"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO user(`login_name`, `password`, `email`, `mobile`, `register_time`, `last_login_time`, `referrer`, `status`, `salt`) VALUES(%s, %s, %s,%s, %s, %s,%s, %s, %s)"

    def generate_params(self, old_row):
        salt = uuid.uuid4().hex
        new_password = hashlib.sha1('%s{%s}' % (old_row['password'], salt)).hexdigest()

        return (old_row['id'], new_password, old_row['email'], old_row['mobile_number'], old_row['register_time'],
                old_row['last_login_time'], old_row['referrer'], 'ACTIVE', salt)