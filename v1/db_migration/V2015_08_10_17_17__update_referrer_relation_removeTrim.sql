BEGIN ;
UPDATE `referrer_relation` SET `referrer_id` = TRIM(`referrer_id`) WHERE LENGTH(`referrer_id`) <> LENGTH(TRIM(`referrer_id`));
COMMIT ;