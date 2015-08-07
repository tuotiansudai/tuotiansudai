CREATE TABLE ${aa}.`sms_captcha` (
  id           INT(32)      NOT NULL AUTO_INCREMENT,
  mobile       VARCHAR(11)  NOT NULL,
  captcha      VARCHAR(20)  NOT NULL,
  created_time DATETIME     NOT NULL,
  expired_time DATETIME     NOT NULL,
  captcha_type VARCHAR(200) NOT NULL,
  PRIMARY KEY (id),
  INDEX SMS_CAPTCHA_MOBILE_INDEX(mobile)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;