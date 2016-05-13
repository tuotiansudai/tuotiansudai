CREATE TABLE `aa`.`licaiquan_article` (
  `id`                 BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `title`              VARCHAR(50)         NOT NULL DEFAULT ''
  COMMENT '文章标题',
  `creator_login_name` VARCHAR(25)         NOT NULL
  COMMENT '文章创建者登录名',
  `checker_login_name` VARCHAR(25)         NOT NULL DEFAULT ''
  COMMENT '文章审核员登录名',
  `author`             VARCHAR(30)         NOT NULL DEFAULT ''
  COMMENT '文章作者',
  `section`            VARCHAR(20)         NOT NULL DEFAULT ''
  COMMENT '所属栏目',
  `source`             VARCHAR(60)                  DEFAULT ''
  COMMENT '文章来源',
  `carousel`           TINYINT(1)          NOT NULL DEFAULT '0'
  COMMENT '是否轮播，0-不轮播，1-轮播',
  `small_picture`      TEXT                NOT NULL
  COMMENT '缩略图',
  `show_picture`       TEXT                NOT NULL
  COMMENT '展示图',
  `content`            TEXT                NOT NULL
  COMMENT '文章内容',
  `favourite_count`    INT(10) UNSIGNED    NOT NULL DEFAULT '0'
  COMMENT '点赞数',
  `read_count`         INT(10) UNSIGNED    NOT NULL DEFAULT '0'
  COMMENT '阅读数',
  `created_time`       DATETIME            NOT NULL
  COMMENT '创建时间',
  `updated_time`       DATETIME            NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  CONSTRAINT FK_LICAIQUAN_ARTICLE_CREATOR_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`creator_login_name`) REFERENCES `aa`.`user` (`login_name`),
  CONSTRAINT FK_LICAIQUAN_ARTICLE_CHECKER_LOGIN_NAME_REF_USER_LOGIN_NAME FOREIGN KEY (`checker_login_name`) REFERENCES `aa`.`user` (`login_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT '理财圈文章列表';

CREATE TABLE `aa`.`licaiquan_article_check_comment` (
  `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `article_id`   BIGINT UNSIGNED NOT NULL
  COMMENT '理财圈文章ID',
  `comment`      TEXT            NOT NULL
  COMMENT '审核评论',
  `created_time` DATETIME        NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  CONSTRAINT FK_THIS_ARTICLE_ID_REF_LICAIQUAN_ARTICLE_ID
  FOREIGN KEY (`article_id`) REFERENCES `aa`.`licaiquan_article` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT '理财圈文章审核意见';