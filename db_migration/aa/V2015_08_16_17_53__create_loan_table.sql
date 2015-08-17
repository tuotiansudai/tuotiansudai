CREATE TABLE ${aa}.`loan` (
  `id`                          varchar(32)         NOT NULL,
  `name`                        varchar(255)        NOT NULL,/***借款项目名称***/
  `agent_login_name`            varchar(32)         NOT NULL,/***代理人***/
  `loan_login_name`             varchar(32)         NOT NULL,/***借款用户***/
  `type`                        VARCHAR (32)        NOT NULL ,/***标的类型***/
  `periods`                     INT                 NOT NULL,/***借款期限***/
  `description_text`            TEXT                NOT NULL,/***项目描述（纯文本）***/
  `description_html`            TEXT                NOT NULL,/***项目描述（带html标签）***/
  `loan_amount`                 DOUBLE              NOT NULL ,/***借款金额***/
  `invest_fee_rate`             DOUBLE              NOT NULL,/***投资手续费比例***/
  `min_invest_amount`           INT                 NOT NULL,/***最小投资金额***/
  `invest_increasing_amount`    INT                 NOT NULL,/***投资递增金额***/
  `max_invest_amount`           INT                 NOT NULL,/***单笔最大投资金额***/
  `activity_type`               VARCHAR (1000)      NOT NULL,/***活动类型***/
  `activity_rate`               DOUBLE              DEFAULT 0,/***活动利率***/
  `contract_id`                 VARCHAR (32)        NOT NULL ,/***合同***/
  `fundraising_start_time`      DATE                NOT NULL,/***筹款开始时间***/
  `fundraising_end_time`        DATE                NOT NULL,/***筹款截止时间***/
  `show_on_home`                boolean             DEFAULT TRUE
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_LOAN_CONTRACT_ID_REF_CONTRACT_ID` FOREIGN KEY (`contract_id`) REFERENCES ${aa}.`contract` (`id`),
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;