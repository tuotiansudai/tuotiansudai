begin;
UPDATE `edxactivity`.`banner` SET `jump_to_link` = app_url, app_url = '' where `jump_to_link` is null;

UPDATE `edxactivity`.`promotion` SET `jump_to_link` = link_url, `link_url` = '' where `jump_to_link` is null;

UPDATE `edxactivity`.`activity` SET `jump_to_link` = app_activity_url, `app_activity_url` = '' where `jump_to_link` is null;

commit;

