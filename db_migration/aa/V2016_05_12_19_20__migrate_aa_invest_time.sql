BEGIN;

UPDATE invest
SET invest_time = created_time
WHERE transfer_invest_id IS NULL;

UPDATE invest
  JOIN invest origin_invest ON origin_invest.id = invest.transfer_invest_id
SET invest.invest_time = origin_invest.created_time
WHERE invest.transfer_invest_id IS NOT NULL;

COMMIT;