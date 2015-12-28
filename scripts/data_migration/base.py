import MySQLdb
from MySQLdb import cursors

from scripts.data_migration import logger


class DBWrapper(object):
    def __init__(self, host, user, password, db_name):
        self.db_name = db_name
        self.password = password
        self.user = user
        self.host = host
        self.conn = None
        self.cursor = None

    def execute(self, sql, params=None, is_many=False):
        # logger.debug(sql)
        # logger.debug(params)
        cursor = self.get_cursor()
        if is_many:
            result = cursor.executemany(sql, params)
        else:
            result = cursor.execute(sql, params)
        return result, cursor

    def get_cursor(self):
        if not self.conn:
            self.conn = MySQLdb.connect(host=self.host,
                                        user=self.user,
                                        passwd=self.password, db=self.db_name, charset='utf8',
                                        cursorclass=cursors.DictCursor)
            if not self.cursor:
                self.cursor = self.conn.cursor()
        return self.cursor

    def commit(self):
        self.conn.commit()

    def close(self):
        self.cursor.close()
        self.conn.close()


class BaseMigrate(object):
    SELECT_SQL = ""
    INSERT_SQL = ""
    START = None
    COUNT = None

    def __init__(self, old_db, new_db, start=None, count=None):
        self.new_db = new_db
        self.old_db = old_db
        if start:
            self.START = int(start)
        if count:
            self.COUNT = int(count)

    def migrate(self):
        logger.info("Start migrate {0}".format(self.__class__.__name__))
        self.before()
        rows = self.fetch_old_data()
        if self.START:
            self._index=self.START
        for row in rows:
            if row:
                params = self.generate_params(row)
                self.insert_data(params)
        self.new_db.commit()
        self.new_db.close()
        self.old_db.close()
        self.after()
        logger.info("Done for {0}".format(self.__class__.__name__))

    def fetch_old_data(self):
        if self.COUNT:
            _, cursor = self.old_db.execute(self.SELECT_SQL, (self.START, self.COUNT))
        else:
            _, cursor = self.old_db.execute(self.SELECT_SQL)

        row = cursor.fetchone()
        while row:
            yield row
            row = cursor.fetchone()

    def generate_params(self, row):
        raise NotImplementedError

    def insert_data(self, params):
        self.new_db.execute(self.INSERT_SQL, params, type(params) is list)

    def before(self):
        self.new_db.execute('set foreign_key_checks=0')

    def after(self):
        pass
