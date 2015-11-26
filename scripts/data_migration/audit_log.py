from scripts.data_migration.base import BaseMigrate


class AuditLogMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "select obj_id, user_id, ip, operate_time, description from user_info_log where is_success=1"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO audit_log(`login_name`, `operator_login_name`, `ip`, `operation_time`, `description`) VALUES(%s, %s, %s, %s, %s)"

    def generate_params(self, old_row):
        return old_row['obj_id'], old_row['user_id'], old_row['ip'], old_row['operate_time'], old_row['description']
