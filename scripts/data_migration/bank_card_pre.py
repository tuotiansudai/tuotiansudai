import redis
from scripts.data_migration.base import DBWrapper
from scripts.data_migration.constants import OLD_HOST, USERNAME, PASSWORD, ORIGINAL_DB

if __name__ == '__main__':
    CREATE_TEMP_TABLE = '''CREATE TABLE IF NOT EXISTS bank_card_temp AS
                            (SELECT
                                id,
                                user_id,
                                bank_no,
                                card_no,
                                status,
                                is_open_fastPayment,
                                time
                            FROM
                                bank_card
                            WHERE
                                status = 'remove'
                            )
                            UNION ALL
                            (
                            SELECT
                                b.id,
                                b.user_id,
                                b.bank_no,
                                b.card_no,
                                b.status,
                                b.is_open_fastPayment,
                                MAX(b.time) AS time
                            FROM
                                (
                                SELECT
                                    bc.id,
                                    bc.user_id,
                                    bc.bank_no,
                                    bc.card_no,
                                    bc.status,
                                    bc.is_open_fastPayment,
                                    bc.time
                                FROM
                                    bank_card bc
                                WHERE
                                    bc.status='passed'
                                ORDER BY
                                    time DESC
                                ) b
                            GROUP BY
                                b.user_id
                            )'''

    old_db = DBWrapper(host=OLD_HOST, user=USERNAME, password=PASSWORD, db_name=ORIGINAL_DB)

    old_db.execute(CREATE_TEMP_TABLE)
    old_db.commit()
    old_db.close()

