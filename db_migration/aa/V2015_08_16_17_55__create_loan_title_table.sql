CREATE TABLE `aa`.`loan_title` (
  `id`        BIGINT UNSIGNED       NOT NULL,
  `type`      varchar(50)           NOT NULL,/***标题类型***/
  `title`     varchar(255)          NOT NULL,/***标题名称***/
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;