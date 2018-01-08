ALTER TABLE `channel_point`
  modify COLUMN `total_point` BIGINT  NOT NULL DEFAULT 0   ;


ALTER TABLE `channel_point_detail`
  modify COLUMN `point` BIGINT  NOT NULL  DEFAULT 0 ;


