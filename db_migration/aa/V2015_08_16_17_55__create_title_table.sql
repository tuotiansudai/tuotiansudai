CREATE TABLE ${aa}.`title` (
  `id` varchar(32) NOT NULL,
  `type` varchar(10) NOT NULL,/***标题类型base:基础标题，new:新增标题***/
  `title` varchar(255) NOT NULL,/***标题名称***/
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;