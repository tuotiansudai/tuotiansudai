from scripts.data_migration.base import BaseMigrate

class UserRoleMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT user_id, role_id FROM user_role WHERE user_role.role_id IN ('ADMINISTRATOR', 'custorm-service', 'INVESTOR', 'LOANER', 'MEMBER', 'ROLE_MERCHANDISER') limit %s, %s"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO user_role(`login_name`, `role`, `created_time`) VALUES(%s, %s, now())"

    ROLE_MAPPING = {'ADMINISTRATOR': 'ADMIN',
               'custorm-service': 'CUSTOMER_SERVICE',
               'MEMBER': 'USER',
               'ROLE_MERCHANDISER': 'STAFF'}

    def generate_params(self, old_row):
        return old_row['user_id'].lower(), self.ROLE_MAPPING.get(old_row['role_id'], old_row['role_id'])

    def before(self):
        pass
