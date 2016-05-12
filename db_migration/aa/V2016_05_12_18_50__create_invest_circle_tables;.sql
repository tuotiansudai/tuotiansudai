CREATE TABLE `aa`.`article_list` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `article_id` bigint(20) unsigned NOT NULL COMMENT '文章内容ID',
  `creator` varchar(30) NOT NULL COMMENT '创建人',
  `checker` varchar(30) NOT NULL COMMENT '审核人',
  `like_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '点赞数',
  `read_count` int(10) unsigned NOT NULL COMMENT '阅读数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='已发布文章详情列表';

CREATE TABLE `aa`.`article_content` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '文章标题',
  `author` varchar(30) NOT NULL DEFAULT '' COMMENT '文章作者',
  `source` varchar(60) DEFAULT '' COMMENT '文章来源',
  `content` longblob NOT NULL COMMENT '文章内容',
  `section` tinyint(4) NOT NULL COMMENT '文章所属栏目，0-平台活动，1-平台新闻，2-行业资讯',
  `carousel` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否轮播，0-不轮播，1-轮播',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_id_refer_article_id` FOREIGN KEY (`id`) REFERENCES `article_list` (`article_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章内容详情';