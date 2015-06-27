CREATE TABLE ${aa}.`sms_captcha` (
  id                    int(32)      NOT NULL AUTO_INCREMENT,
  code                  varchar(20)  DEFAULT NULL,
  mobile                varchar(18)  DEFAULT NULL,
  deadline              datetime     DEFAULT NULL,
  generation_time       datetime     DEFAULT NULL,
  status                varchar(100) DEFAULT NULL,
  captcha_type                  varchar(200) NOT NULL,
  user_id               int(32) ,
  PRIMARY KEY (id),
  CONSTRAINT FK_SMS_CAPTCHA_USER FOREIGN KEY(user_id) REFERENCES ${aa}.`user`(id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;