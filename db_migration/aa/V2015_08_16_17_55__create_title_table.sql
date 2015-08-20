CREATE TABLE `${aa}`.`title` (
  `id`        BIGINT        NOT NULL,
  `type`      varchar(10)   NOT NULL,/***标题类型base:基础标题，new:新增标题***/
  `title`     varchar(255)  NOT NULL,/***标题名称***/
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;