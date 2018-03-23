BEGIN;
ALTER TABLE `edxask`.`question` CHANGE `question` `question` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `edxask`.`question` CHANGE `addition` `addition` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
COMMIT;


