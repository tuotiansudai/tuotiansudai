import redis
from scripts.data_migration.base import DBWrapper
from scripts.data_migration.constants import OLD_HOST, USERNAME, PASSWORD, ORIGINAL_DB

if __name__ == '__main__':
    CREATE_TEMP_TABLE = '''CREATE TABLE IF NOT EXISTS withdraw_cash_temp AS
                        SELECT
                            w.bank_card_id,
                            w.user_id,
                            w.money,
                            w.fee,
                            w.verify_message,
                            w.verify_time,
                            w.recheck_message,
                            w.recheck_time,
                            w.time,
                            w.status,
                            w.source
                        FROM
                            withdraw_cash w
                        JOIN
                            bank_card_temp b
                        ON
                            w.`bank_card_id` = b.`id`
                        WHERE
                            w.is_withdraw_by_admin IS NULL

                        UNION ALL

                        SELECT
                            bt.id AS bank_card_id,
                            m.user_id,
                            m.money,
                            m.fee,
                            m.verify_message,
                            m.verify_time,
                            m.recheck_message,
                            m.recheck_time,
                            m.time,
                            m.status,
                            m.source
                        FROM
                            withdraw_cash m
                        JOIN
                            bank_card_temp bt
                        ON
                            m.user_id = bt.user_id
                        WHERE
                            m.is_withdraw_by_admin IS NULL
                            and m.bank_card_id NOT IN (SELECT bb.id FROM bank_card_temp bb)
                        '''

    old_db = DBWrapper(host=OLD_HOST, user=USERNAME, password=PASSWORD, db_name=ORIGINAL_DB)

    old_db.execute(CREATE_TEMP_TABLE)
    old_db.commit()
    old_db.close()

