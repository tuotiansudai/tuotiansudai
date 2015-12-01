from scripts.data_migration.base import BaseMigrate


class AnnounceMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "select n.title,nb.body,removeHtml(nb.body) as content_text, n.create_time, update_time from node n left join node_body nb on n.body=nb.id where n.node_type='article'"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO announce(`title`, `content`, `content_text`, `created_time`, `update_time`) VALUES(%s, %s, %s, %s, %s)"

    def generate_params(self, old_row):
        return old_row['title'], old_row['body'], old_row['content_text'], old_row['create_time'], old_row['update_time']
