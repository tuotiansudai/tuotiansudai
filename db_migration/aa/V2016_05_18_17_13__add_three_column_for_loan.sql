ALTER TABLE loan ADD COLUMN first_invest_achievement_id BIGINT UNSIGNED
AFTER `status`,
ADD FOREIGN KEY FK_LOAN_FIRST_INVEST_ACHIEVEMENT_ID_INVEST_ID(`first_invest_achievement_id`) REFERENCES invest (`id`);

ALTER TABLE loan ADD COLUMN max_amount_achievement_id BIGINT UNSIGNED
AFTER first_invest_achievement_id,
ADD FOREIGN KEY FK_LOAN_MAX_AMOUNT_ACHIEVEMENT_ID_INVEST_ID(`max_amount_achievement_id`) REFERENCES invest (`id`);

ALTER TABLE loan ADD COLUMN last_invest_achievement_id BIGINT UNSIGNED
AFTER first_invest_achievement_id,
ADD FOREIGN KEY FK_LOAN_LAST_INVEST_ACHIEVEMENT_ID_INVEST_ID(`last_invest_achievement_id`) REFERENCES invest (`id`);