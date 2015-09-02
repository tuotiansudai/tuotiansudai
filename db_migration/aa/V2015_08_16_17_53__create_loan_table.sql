CREATE TABLE `aa`.`loan` (
  `id`                       BIGINT UNSIGNED NOT NULL,
  `name`                     VARCHAR(255)    NOT NULL, /***借款项目名称***/
  `agent`                    VARCHAR(32)     NOT NULL, /***代理人***/
  `loaner`                   VARCHAR(32)     NOT NULL, /***借款用户***/
  `type`                     VARCHAR(100)    NOT NULL, /***标的类型***/
  `periods`                  INT             NOT NULL, /***借款期限***/
  `description_text`         TEXT            NOT NULL, /***项目描述（纯文本）***/
  `description_html`         TEXT            NOT NULL, /***项目描述（带html标签）***/
  `loan_amount`              BIGINT UNSIGNED NOT NULL, /***借款金额***/
  `invest_fee_rate`          DOUBLE          NOT NULL, /***投资手续费比例***/
  `min_invest_amount`        BIGINT UNSIGNED NOT NULL, /***最小投资金额***/
  `invest_increasing_amount` BIGINT UNSIGNED NOT NULL, /***投资递增金额***/
  `max_invest_amount`        BIGINT UNSIGNED NOT NULL, /***单笔最大投资金额***/
  `activity_type`            VARCHAR(100)    NOT NULL, /***活动类型***/
  `activity_rate`            DOUBLE  DEFAULT 0, /***活动利率***/
  `base_rate`                DOUBLE  DEFAULT 0, /***基本利率***/
  `contract_id`              BIGINT UNSIGNED NOT NULL, /***合同***/
  `fundraising_start_time`   DATETIME        NOT NULL, /***筹款开始时间***/
  `fundraising_end_time`     DATETIME        NOT NULL, /***筹款截止时间***/
  `created_time`             DATETIME        NOT NULL, /***标的创建时间***/
  `first_trial_time`         DATETIME, /***初审时间***/
  `rehear_time`              DATETIME, /***复审时间***/
  `status`                   VARCHAR(50)     NOT NULL, /***标的状态***/
  `show_on_home`             BOOLEAN DEFAULT TRUE, /***是否显示在首页true:显示在首页，false:不显示在首页***/
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;