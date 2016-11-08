BEGIN;

DELETE FROM `aa`.`jpush_alert`
WHERE
  `jpush_alert`.is_automatic = 1;

INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('5888元体验金已存入您的账户，请查收！', 'REGISTER_USER_SUCCESS', 'ALL', 'ALL', 'ENABLED',
                                '哇，您终于来啦！初次见面，岂能无礼？5888元体验金双手奉上，【立即体验】再拿588元红包和3%加息券！', 'MESSAGE_CENTER', '2016-11-1',
                                'sidneygao', '1', (SELECT id
                                                   FROM `aa`.`message`
                                                   WHERE event_type =
                                                         'REGISTER_USER_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('恭喜您认证成功', 'REGISTER_ACCOUNT_SUCCESS', 'ALL', 'ALL', 'ENABLED',
                   '尊敬的{0}女士/先生，恭喜您认证成功，您的支付密码已经由联动优势发送至注册手机号码中,马上【绑定银行卡】开启赚钱之旅吧！', 'MESSAGE_CENTER', '2016-11-1',
                   'sidneygao', '1', (SELECT id
                                      FROM
                                        `aa`.`message`
                                      WHERE
                                        event_type
                                        =
                                        'REGISTER_ACCOUNT_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('您的{0}元提现已到账,请查收', 'WITHDRAW_SUCCESS', 'ALL', 'ALL', 'ENABLED', '尊敬的用户，您提交的{0}元提现申请已成功通过审核，请及时查收款项，感谢您选择拓天速贷。',
                           'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                             FROM `aa`.`message`
                                                                             WHERE event_type = 'WITHDRAW_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('恭喜您成功投资{0}元', 'INVEST_SUCCESS', 'ALL', 'ALL', 'ENABLED',
                       '恭喜您成功投资{0}元'', ''尊敬的用户，您已成功投资房产/车辆抵押借款{0}元，独乐不如众乐，马上【邀请好友投资】还能额外拿1%现金奖励哦！', 'MESSAGE_CENTER',
                       '2016-11-1', 'sidneygao', '1',
                       (SELECT id
                        FROM `aa`.`message`
                        WHERE event_type = 'INVEST_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES
  ('您发起的转让项目转让成功，{0}元已发放至您的账户！', 'TRANSFER_SUCCESS', 'ALL', 'ALL', 'ENABLED',
                                 '尊敬的用户，您发起的转让项目{0}已经转让成功，资金已经到达您的账户，感谢您选择拓天速贷。',
                                 'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                                   FROM `aa`.`message`
                                                                                   WHERE
                                                                                     event_type = 'TRANSFER_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('您提交的债权转让到期取消，请查看！', 'TRANSFER_FAIL', 'ALL', 'ALL', 'ENABLED',
                             '尊敬的用户，我们遗憾地通知您，您发起的转让项目没有转让成功。如有疑问，请致电客服热线400-169-1188，感谢您选择拓天速贷。', 'MESSAGE_CENTER',
                             '2016-11-1', 'sidneygao', '1',
                             (SELECT id
                              FROM `aa`.`message`
                              WHERE event_type = 'TRANSFER_FAIL'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES
  ('您投资的{0}已经满额放款，预期年化收益{1}%', 'LOAN_OUT_SUCCESS', 'ALL', 'ALL', 'ENABLED', '尊敬的用户，您投资的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。',
                               'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                                 FROM `aa`.`message`
                                                                                 WHERE
                                                                                   event_type = 'LOAN_OUT_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('您投资的{0}已回款{1}元，请前往账户查收！', 'REPAY_SUCCESS', 'ALL', 'ALL', 'ENABLED', '尊敬的用户，您投资的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。',
                                   'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                                     FROM `aa`.`message`
                                                                                     WHERE
                                                                                       event_type = 'REPAY_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES
  ('您推荐的好友 {0} 已成功注册', 'RECOMMEND_SUCCESS', 'ALL', 'ALL', 'ENABLED', '尊敬的用户，您推荐的好友 {0} 已成功注册，【邀请好友投资】您还能再拿1%现金奖励哦！',
                       'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                         FROM `aa`.`message`
                                                                         WHERE event_type = 'RECOMMEND_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('{0}元推荐奖励已存入您的账户，请查收！', 'RECOMMEND_AWARD_SUCCESS', 'ALL', 'ALL', 'ENABLED', '尊敬的用户，您推荐的好友{0}投资成功，您已获得{1}元现金奖励。',
                                'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                                  FROM `aa`.`message`
                                                                                  WHERE event_type =
                                                                                        'RECOMMEND_AWARD_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES
  ('您有一张{0}元投资红包即将失效', 'COUPON_5DAYS_EXPIRED_ALERT', 'ALL', 'ALL', 'ENABLED', '尊敬的用户，您有一张{0}元投资红包即将失效（有效期至：{1}），请尽快使用！',
                       'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                         FROM `aa`.`message`
                                                                         WHERE
                                                                           event_type = 'COUPON_5DAYS_EXPIRED_ALERT'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('您的V5会员已到期，请前去购买', 'MEMBERSHIP_EXPIRED', 'ALL', 'ALL', 'ENABLED',
                           '尊敬的用户，您的V5会员已到期，V5会员可享受服务费7折优惠，平台也将会在V5会员生日时送上神秘礼包哦。请及时续费以免耽误您获得投资奖励！', 'MESSAGE_CENTER',
                           '2016-11-1', 'sidneygao', '1',
                           (SELECT id
                            FROM `aa`.`message`
                            WHERE event_type = 'MEMBERSHIP_EXPIRED'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES
  ('您的{0}元提现申请已提交成功', 'WITHDRAW_APPLICATION_SUCCESS', 'ALL', 'ALL', 'ENABLED',
                      '尊敬的用户，您提交了{0}元提现申请，联动优势将会在1个工作日内进行审批，请耐心等待。',
                      'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                        FROM `aa`.`message`
                                                                        WHERE
                                                                          event_type = 'WITHDRAW_APPLICATION_SUCCESS'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('您投资的{0}提前还款，{1}元已返还至您的账户！', 'ADVANCED_REPAY', 'ALL', 'ALL', 'ENABLED',
                                     '尊敬的用户，您在{0}投资的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】',
                                     'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1',
                                     (SELECT id
                                      FROM `aa`.`message`
                                      WHERE event_type = 'ADVANCED_REPAY'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('恭喜您会员等级提升至V{0}', 'MEMBERSHIP_UPGRADE', 'ALL', 'ALL', 'ENABLED', '尊敬的用户，恭喜您会员等级提升至V{0}，拓天速贷为您准备了更多会员特权，快来查看吧。',
                          'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                            FROM `aa`.`message`
                                                                            WHERE event_type = 'MEMBERSHIP_UPGRADE'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES
  ('拓天速贷为您送上生日祝福，请查收！', 'BIRTHDAY', 'ALL', 'ALL', 'ENABLED', '尊敬的{0}先生/女士，我猜今天是您的生日，拓天速贷在此送上真诚的祝福，生日当月投资即可享受收益翻倍哦！',
                        'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                          FROM `aa`.`message`
                                                                          WHERE event_type = 'BIRTHDAY'));
INSERT INTO `aa`.`jpush_alert` (`name`, `push_type`, `push_source`, `push_user_type`, `status`, `content`, `jump_to`, `created_time`, `created_by`, `is_automatic`, `message_id`)
VALUES ('恭喜您已成功购买{0}个月V5会员！', 'MEMBERSHIP_BUY_SUCCESS', 'ALL', 'ALL', 'ENABLED',
                              '尊敬的用户，恭喜您已成功购买V5会员，有效期至{0}日，【马上投资】享受会员特权吧！',
                              'MESSAGE_CENTER', '2016-11-1', 'sidneygao', '1', (SELECT id
                                                                                FROM `aa`.`message`
                                                                                WHERE event_type =
                                                                                      'MEMBERSHIP_BUY_SUCCESS'));

COMMIT;