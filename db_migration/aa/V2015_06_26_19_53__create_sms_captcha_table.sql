CREATE TABLE ${aa}.`sms_captcha` (
  id              INT(32)      NOT NULL AUTO_INCREMENT,
  code            VARCHAR(20)           DEFAULT NULL,
  mobile          VARCHAR(18)           DEFAULT NULL,
  deadline        DATETIME              DEFAULT NULL,
  generation_time DATETIME              DEFAULT NULL,
  status          VARCHAR(100)          DEFAULT NULL,
  captcha_type    VARCHAR(200) NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;