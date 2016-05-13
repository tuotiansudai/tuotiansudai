BEGIN;

UPDATE invest
SET invest_time = created_time
WHERE transfer_invest_id IS NULL;

UPDATE invest
SET invest_time = (select origin_invest.created_time from invest origin_invest where origin_invest.id = invest.transfer_invest_id)
WHERE transfer_invest_id IS NOT NULL;

COMMIT;