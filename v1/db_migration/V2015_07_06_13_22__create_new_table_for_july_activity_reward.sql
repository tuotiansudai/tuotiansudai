CREATE TABLE `july_activity_reward` (
  id                                  INT(16)               NOT NULL AUTO_INCREMENT,
  user_id                             VARCHAR(32)           NOT NULL,
  referrer_id                         VARCHAR(32),
  certified_reward                    BOOLEAN,
  certified_reward_time               DATETIME,
  referrer_certified_reward           BOOLEAN DEFAULT FALSE NOT NULL,
  referrer_certified_reward_time      DATETIME,
  first_recharge_reward               BOOLEAN DEFAULT FALSE NOT NULL,
  first_recharge_reward_time          DATETIME,
  referrer_first_recharge_reward      BOOLEAN DEFAULT FALSE NOT NULL,
  referrer_first_recharge_reward_time DATETIME,
  referrer_first_invest_reward        BOOLEAN DEFAULT FALSE NOT NULL,
  referrer_first_invest_reward_time   DATETIME,


  PRIMARY KEY (id),
  CONSTRAINT JULY_ACTIVITY_USER_FK FOREIGN KEY (user_id) REFERENCES `user` (id),
  CONSTRAINT JULY_ACTIVITY_REFERRER_FK FOREIGN KEY (referrer_id) REFERENCES `user` (id),
  UNIQUE KEY JULY_ACTIVITY_USER_REFERRER_UNIQUE (user_id, referrer_id),
  INDEX JULY_ACTIVITY_USER_INDEX(user_id),
  INDEX JULY_ACTIVITY_REFERRER_INDEX(referrer_id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 100001
  DEFAULT CHARSET = utf8;