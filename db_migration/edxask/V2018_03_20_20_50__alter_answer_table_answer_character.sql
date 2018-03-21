BEGIN;
ALTER TABLE `edxask`.`answer` CHANGE `answer` `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
COMMIT;


