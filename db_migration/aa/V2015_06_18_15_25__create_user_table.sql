CREATE TABLE ${aa}.`user` (
  id                 INT(32)      NOT NULL AUTO_INCREMENT,
  login_name         VARCHAR(25)  NOT NULL,
  password           VARCHAR(100) NOT NULL,
  email              VARCHAR(100),
  mobile             VARCHAR(18)  NOT NULL,
  register_time      DATETIME     NOT NULL,
  last_login_time    DATETIME,
  last_modified_time DATETIME,
  last_modified_user VARCHAR(25),
  avatar             VARCHAR(256),
  referrer           VARCHAR(25),
  status             VARCHAR(20)  NOT NULL,
  salt               VARCHAR(32)  NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_USER_FOR_REFERRER FOREIGN KEY (referrer) REFERENCES ${aa}.`user` (login_name),
  CONSTRAINT FK_USER_FOR_MODIFIED_USER FOREIGN KEY (last_modified_user) REFERENCES ${aa}.`user` (login_name),
  UNIQUE KEY USER_LOGIN_NAME_UNIQUE (login_name),
  UNIQUE KEY USER_MOBILE_UNIQUE (mobile),
  INDEX USER_LOGIN_NAME_INDEX(login_name),
  INDEX USER_REFERRER_INDEX(referrer),
  INDEX USER_EMAIL_INDEX(email),
  INDEX USER_MOBILE_INDEX(mobile)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE ${aa}.`account` (
  id              INT(32)     NOT NULL AUTO_INCREMENT,
  login_name      VARCHAR(50) NOT NULL,
  user_name       VARCHAR(50) NOT NULL,
  identity_number VARCHAR(20) NOT NULL,
  pay_user_id     VARCHAR(32),
  pay_account_id  VARCHAR(15),
  register_time   DATETIME    NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ACCOUNT_LOGIN_NAME_UNIQUE (login_name),
  INDEX ACCOUNT_LOGIN_NAME_INDEX(login_name),
  CONSTRAINT FK_ACCOUNT_FOR_USER_LOGIN_NAME FOREIGN KEY (login_name) REFERENCES ${aa}.`user` (login_name)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;