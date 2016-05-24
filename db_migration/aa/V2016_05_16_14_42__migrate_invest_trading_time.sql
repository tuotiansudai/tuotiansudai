BEGIN;

UPDATE invest
SET trading_time = created_time
WHERE status = 'SUCCESS';

COMMIT;