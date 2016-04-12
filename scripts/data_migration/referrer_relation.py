from scripts.data_migration.base import BaseMigrate


class ReferrerRelationMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = '''SELECT referrer_id, user_id, level
                    FROM
                      (SELECT referrer_id, user_id, level FROM referrer_relation rr WHERE NOT EXISTS (SELECT 1 FROM user_role ur WHERE ur.user_id = rr.referrer_id AND ur.role_id='ROLE_MERCHANDISER') AND level<=2
                        UNION
                      SELECT referrer_id, user_id, level FROM referrer_relation rr WHERE EXISTS(SELECT 1 FROM user_role ur WHERE ur.user_id = rr.referrer_id AND ur.role_id='ROLE_MERCHANDISER') AND level<=4
                      ) rr_union
                      LIMIT %s, %s'''

    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO referrer_relation(`referrer_login_name`, `login_name`, `level`) VALUES(%s, %s, %s)"

    def generate_params(self, old_row):
        return old_row['referrer_id'].lower(), old_row['user_id'].lower(), old_row['level']

    def before(self):
        pass
