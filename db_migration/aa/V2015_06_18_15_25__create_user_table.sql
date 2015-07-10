CREATE TABLE ${aa}.`user` (
  id                 INT(32)      NOT NULL AUTO_INCREMENT,
  login_name         VARCHAR(50)  NOT NULL,
  password           VARCHAR(100) NOT NULL,
  email              VARCHAR(100) NOT NULL,
  address            VARCHAR(100),
  mobile_number      VARCHAR(18)  NOT NULL,
  last_login_time    DATETIME,
  register_time      DATETIME     NOT NULL,
  last_modified_time DATETIME,
  last_modified_user INT(32),
  forbidden_time     DATETIME,
  avatar             VARCHAR(500),
  referrer_id        INT(32),
  status             VARCHAR(20)  NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_USER_FOR_REFERRER FOREIGN KEY (referrer_id) REFERENCES ${aa}.`user` (id),
  CONSTRAINT FK_USER_FOR_MODIFIED_USER FOREIGN KEY (last_modified_user) REFERENCES ${aa}.`user` (id),
  UNIQUE KEY USER_EMAIL_UNIQUE (email),
  UNIQUE KEY USER_LOGIN_NAME_UNIQUE (login_name),
  UNIQUE KEY USER_MOBILE_NUMBER_UNIQUE (mobile_number),
  INDEX USER_LOGIN_NAME_INDEX(login_name),
  INDEX USER_EMAIL_INDEX(email),
  INDEX USER_MOBILE_NUMBER_INDEX(mobile_number)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;

CREATE TABLE ${aa}.`account` (
  id             INT(32)     NOT NULL AUTO_INCREMENT,
  identity_id    VARCHAR(20) NOT NULL,
  user_name      VARCHAR(50) NOT NULL,
  ump_user_id    VARCHAR(32),
  ump_account_id VARCHAR(15),
  create_time    DATETIME    NOT NULL,
  user_id        INT(32)     NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ACCOUNT_IDENTITY_UNIQUE (identity_id),
  INDEX ACCOUNT_USER_NAME_INDEX(user_name),
  INDEX ACCOUNT_IDENTITY_INDEX(identity_id),
  CONSTRAINT FK_ACCOUNT_FOR_USER FOREIGN KEY (user_id) REFERENCES ${aa}.`user` (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;