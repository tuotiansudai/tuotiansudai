CREATE TABLE `membership_price` (
  `level`    INT UNSIGNED    NOT NULL,
  `duration` INT UNSIGNED    NOT NULL,
  `price`    BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`level`, `duration`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
