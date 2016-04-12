#coding:utf-8
import re
from scripts.data_migration.base import BaseMigrate


class AnnounceMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "select n.title,nb.body, n.create_time, update_time from node n left join node_body nb on n.body=nb.id where n.node_type='article'"
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO announce(`title`, `content`, `content_text`, `created_time`, `update_time`) VALUES(%s, %s, %s, %s, %s)"

    _dr = re.compile(r'<[^>]+>',re.S)

    def generate_params(self, old_row):
        content_text = self._dr.sub('', old_row['body'])

        return old_row['title'], old_row['body'], content_text, old_row['create_time'], old_row['update_time']
