BEGIN;

CREATE TABLE `zero_shopping_prize_select` (
  `id`               BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile`           VARCHAR(11)         NOT NULL,
  `user_name`        VARCHAR(50),
  `invest_amount`    BIGINT(20)          DEFAULT '0',
  `select_prize`     VARCHAR(50)         NOT NULL,
  `invest_time`      DATETIME,
  PRIMARY KEY (`id`),
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `zero_shopping_prize_config` (
  `id`                          INT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `prize`                       VARCHAR(50) NOT NULL,
  `prize_total`                 INT(5) UNSIGNED NOT NULL DEFAULT 0,
  `prize_surplus`               INT(5) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('Medihea_Mask');
INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('Thailand_VIP');
INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('Philips_Shaver');
INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('SK_II');
INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('XiaoMi_5X');
INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('XiaPu_Television');
INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('Philips_Purifier');
INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('Sony_Camera');
INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('Apple_MacBook');
INSERT INTO `edxmessage`.`zero_shopping_prize` (`prize`) VALUES ('Iphone_X');



END;
