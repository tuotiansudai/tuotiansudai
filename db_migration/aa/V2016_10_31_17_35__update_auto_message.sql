BEGIN;

UPDATE `aa`.`message`
SET `message`.`message_category` = 'SYSTEM'
WHERE `message`.type = 'EVENT';

UPDATE `aa`.`message`
SET `message`.`deleted` = '1'
WHERE `message`.type = 'EVENT';

INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES
  ('5888元体验金已存入您的账户，请查收！', '5888元体验金已存入您的账户，请查收！', '哇，您终于来啦！初次见面，岂能无礼？5888元体验金双手奉上，【立即体验】再拿588元红包和3%加息券！',
                           '哇，您终于来啦！初次见面，岂能无礼？5888元体验金双手奉上，【立即体验】再拿588元红包和3%加息券！', 'EVENT', 'REGISTER_USER_SUCCESS',
                           'ALL_USER',
                           'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/loan-list', 'INVEST_NORMAL',
    'APPROVED',
    '0', 'sidneygao',
    '2016-11-01 00:00:00',
    '9999-12-31 23:59:59',
    'sidneygao',
    '2016-11-01 00:00:00',
    'sidneygao',
    '2016-11-01 00:00:00', '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('恭喜您认证成功', '恭喜您认证成功', '尊敬的{0}女士/先生，恭喜您认证成功，您的支付密码已经由联动优势发送至注册手机号码中,马上【绑定银行卡】开启赚钱之旅吧！',
                   '尊敬的{0}女士/先生，恭喜您认证成功，您的支付密码已经由联动优势发送至注册手机号码中,马上【绑定银行卡】开启赚钱之旅吧！', 'EVENT', 'REGISTER_ACCOUNT_SUCCESS',
                   'ALL_USER', 'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/personal-info',
                   'PERSON_CENTER_HOME',
                   'APPROVED', '0', 'sidneygao', '2016-11-01 00:00:00', '9999-12-31 23:59:59', 'sidneygao',
        '2016-11-01 00:00:00',
        'sidneygao', '2016-11-01 00:00:00', '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您的{0}元提现已到账,请查收', '您的{0}元提现已到账,请查收', '尊敬的用户，您提交的{0}元提现申请已成功通过审核，请及时查收款项，感谢您选择拓天速贷。',
                           '尊敬的用户，您提交的{0}元提现申请已成功通过审核，请及时查收款项，感谢您选择拓天速贷。', 'EVENT', 'WITHDRAW_SUCCESS', 'ALL_USER',
                           'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/user-bill', 'ASSESS_ADMINISTER',
  'APPROVED',
  '0',
  'sidneygao',
  '2016-11-01 00:00:00',
  '9999-12-31 23:59:59',
  'sidneygao',
  '2016-11-01 00:00:00',
  'sidneygao',
  '2016-11-01 00:00:00',
  '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('恭喜您成功投资{0}元', '恭喜您成功投资{0}元', '尊敬的用户，您已成功投资房产/车辆抵押借款{0}元，独乐不如众乐，马上【邀请好友投资】还能额外拿1%现金奖励哦！',
                       '尊敬的用户，您已成功投资房产/车辆抵押借款{0}元，独乐不如众乐，马上【邀请好友投资】还能额外拿1%现金奖励哦！', 'EVENT', 'INVEST_SUCCESS',
                       'ALL_USER',
                       'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/referrer/refer-list',
                       'RECOMMEND_DETAIL',
                       'APPROVED',
        '0',
        'sidneygao',
        '2016-11-01 00:00:00',
        '9999-12-31 23:59:59',
        'sidneygao',
        '2016-11-01 00:00:00',
        'sidneygao',
        '2016-11-01 00:00:00',
        '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您发起的转让项目转让成功，{0}元已发放至您的账户！', '您发起的转让项目转让成功，{0}元已发放至您的账户！',
                                      '尊敬的用户，您发起的转让项目{0}已经转让成功，资金已经到达您的账户，感谢您选择拓天速贷。',
                                      '尊敬的用户，您发起的转让项目{0}已经转让成功，资金已经到达您的账户，感谢您选择拓天速贷。',
                                      'EVENT', 'TRANSFER_SUCCESS', 'ALL_USER', 'WEBSITE,APP_MESSAGE', 'SYSTEM',
                                      'https://tuotiansudai.com/transferrer/transfer-application-list',
                                      'TRANSFER_HISTORY', 'APPROVED', '0',
        'sidneygao',
        '2016-11-01 00:00:00',
        '9999-12-31 23:59:59',
        'sidneygao',
        '2016-11-01 00:00:00',
        'sidneygao',
        '2016-11-01 00:00:00',
        '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您提交的债权转让到期取消，请查看！', '您提交的债权转让到期取消，请查看！',
                             '尊敬的用户，我们遗憾地通知您，您发起的转让项目没有转让成功。如有疑问，请致电客服热线400-169-1188，感谢您选择拓天速贷。',
                             '尊敬的用户，我们遗憾地通知您，您发起的转让项目没有转让成功。如有疑问，请致电客服热线400-169-1188，感谢您选择拓天速贷。', 'EVENT',
                             'TRANSFER_FAIL',
                             'ALL_USER', 'WEBSITE,APP_MESSAGE', 'SYSTEM',
                             'https://tuotiansudai.com/transferrer/transfer-application-list',
                             'TRANSFER_HISTORY', 'APPROVED', '0', 'sidneygao', '2016-11-01 00:00:00',
        '9999-12-31 23:59:59',
        'sidneygao', '2016-11-01 00:00:00', 'sidneygao', '2016-11-01 00:00:00', '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您投资的{0}已经满额放款，预期年化收益{1}%', '您投资的{0}已经满额放款，预期年化收益{1}%', '尊敬的用户，您投资的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。',
                                    '尊敬的用户，您投资的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。', 'EVENT', 'LOAN_OUT_SUCCESS',
                                    'ALL_USER',
                                    'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/investor/invest-list',
                                    'MY_INVEST_REPAYING',
                                    'APPROVED', '0', 'sidneygao', '2016-11-01 00:00:00', '9999-12-31 23:59:59',
        'sidneygao', '2016-11-01 00:00:00',
        'sidneygao', '2016-11-01 00:00:00', '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您投资的{0}已回款{1}元，请前往账户查收！', '您投资的{0}已回款{1}元，请前往账户查收！', '尊敬的用户，您投资的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。',
                                   '尊敬的用户，您投资的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。', 'EVENT', 'REPAY_SUCCESS', 'ALL_USER',
                                   'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/investor/invest-list',
                                   'MY_INVEST_REPAYING',
                                   'APPROVED', '0', 'sidneygao', '2016-11-01 00:00:00', '9999-12-31 23:59:59',
        'sidneygao', '2016-11-01 00:00:00',
        'sidneygao', '2016-11-01 00:00:00', '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您推荐的好友 {0} 已成功注册', '您推荐的好友 {0} 已成功注册', '尊敬的用户，您推荐的好友 {0} 已成功注册，【邀请好友投资】您还能再拿1%现金奖励哦！',
                            '尊敬的用户，您推荐的好友 {0} 已成功注册，【邀请好友投资】您还能再拿1%现金奖励哦！', 'EVENT', 'RECOMMEND_SUCCESS', 'ALL_USER',
                            'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/referrer/refer-list',
                            'RECOMMEND_MY_REWARD',
                            'APPROVED', '0', 'sidneygao', '2016-11-01 00:00:00', '9999-12-31 23:59:59', 'sidneygao',
        '2016-11-01 00:00:00',
        'sidneygao', '2016-11-01 00:00:00', '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('{0}元推荐奖励已存入您的账户，请查收！', '{0}元推荐奖励已存入您的账户，请查收！', '尊敬的用户，您推荐的好友{0}投资成功，您已获得{1}元现金奖励。',
                                '尊敬的用户，您推荐的好友{0}投资成功，您已获得{1}元现金奖励。', 'EVENT', 'RECOMMEND_AWARD_SUCCESS', 'ALL_USER',
                                'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/referrer/refer-list',
                                'RECOMMEND_MY_REWARD',
                                'APPROVED', '0', 'sidneygao', '2016-11-01 00:00:00', '9999-12-31 23:59:59', 'sidneygao',
        '2016-11-01 00:00:00',
        'sidneygao', '2016-11-01 00:00:00', '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您有一张{0}即将失效', '您有一张{0}即将失效', '尊敬的用户，您有一张{0}即将失效（有效期至：{1}），请尽快使用！',
                            '尊敬的用户，您有一张{0}即将失效（有效期至：{1}），请尽快使用！', 'EVENT', 'COUPON_5DAYS_EXPIRED_ALERT',
                            'ALL_USER',
                            'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/my-treasure',
                            'MY_TREASURE_UNUSED',
                            'APPROVED', '0',
        'sidneygao',
        '2016-11-01 00:00:00',
        '9999-12-31 23:59:59',
        'sidneygao',
        '2016-11-01 00:00:00',
        'sidneygao',
        '2016-11-01 00:00:00',
        '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您的V5会员已到期，请前去购买', '您的V5会员已到期，请前去购买',
                           '尊敬的用户，您的V5会员已到期，V5会员可享受服务费7折优惠，平台也将会在V5会员生日时送上神秘礼包哦。请及时续费以免耽误您获得投资奖励！',
                           '尊敬的用户，您的V5会员已到期，V5会员可享受服务费7折优惠，平台也将会在V5会员生日时送上神秘礼包哦。请及时续费以免耽误您获得投资奖励！', 'EVENT',
                           'MEMBERSHIP_EXPIRED', 'ALL_USER', 'WEBSITE,APP_MESSAGE', 'SYSTEM',
                           'https://tuotiansudai.com/membership',
                           'MY_MEMBERSHIP', 'APPROVED', '0', 'sidneygao', '2016-11-01 00:00:00', '9999-12-31 23:59:59',
        'sidneygao', '2016-11-01 00:00:00', 'sidneygao', '2016-11-01 00:00:00', '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您的{0}元提现申请已提交成功', '您的{0}元提现申请已提交成功', '尊敬的用户，您提交了{0}元提现申请，联动优势将会在1个工作日内进行审批，请耐心等待。',
                           '尊敬的用户，您提交了{0}元提现申请，联动优势将会在1个工作日内进行审批，请耐心等待。', 'EVENT', 'WITHDRAW_APPLICATION_SUCCESS',
                           'ALL_USER',
                           'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/user-bill', 'ASSESS_ADMINISTER',
  'APPROVED',
  '0',
  'sidneygao',
  '2016-11-01 00:00:00',
  '9999-12-31 23:59:59',
  'sidneygao',
  '2016-11-01 00:00:00',
  'sidneygao',
  '2016-11-01 00:00:00',
  '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('您投资的{0}提前还款，{1}元已返还至您的账户！', '您投资的{0}提前还款，{1}元已返还至您的账户！',
                                     '尊敬的用户，您在{0}投资的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】',
                                     '尊敬的用户，您在{0}投资的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】', 'EVENT',
                                     'ADVANCED_REPAY',
                                     'ALL_USER', 'WEBSITE,APP_MESSAGE', 'SYSTEM',
                                     'https://tuotiansudai.com/investor/invest-list',
                                     'MY_INVEST_REPAYING', 'APPROVED', '0', 'sidneygao', '2016-11-01 00:00:00',
        '9999-12-31 23:59:59',
        'sidneygao', '2016-11-01 00:00:00', 'sidneygao', '2016-11-01 00:00:00', '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('恭喜您会员等级提升至V{0}', '恭喜您会员等级提升至V{0}', '尊敬的用户，恭喜您会员等级提升至V{0}，拓天速贷为您准备了更多会员特权，快来查看吧。',
                          '尊敬的用户，恭喜您会员等级提升至V{0}，拓天速贷为您准备了更多会员特权，快来查看吧。', 'EVENT', 'MEMBERSHIP_UPGRADE', 'ALL_USER',
                          'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/membership', 'MY_MEMBERSHIP',
  'APPROVED',
  '0',
  'sidneygao',
  '2016-11-01 00:00:00',
  '9999-12-31 23:59:59',
  'sidneygao',
  '2016-11-01 00:00:00',
  'sidneygao',
  '2016-11-01 00:00:00',
  '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('拓天速贷为您送上生日祝福，请查收！', '拓天速贷为您送上生日祝福，请查收！', '尊敬的{0}先生/女士，我猜今天是您的生日，拓天速贷在此送上真诚的祝福，生日当月投资即可享受收益翻倍哦！',
                             '尊敬的{0}先生/女士，我猜今天是您的生日，拓天速贷在此送上真诚的祝福，生日当月投资即可享受收益翻倍哦！', 'EVENT', 'BIRTHDAY', 'ALL_USER',
                             'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/loan-list', 'INVEST_NORMAL',
  'APPROVED',
  '0',
  'sidneygao',
  '2016-11-01 00:00:00',
  '9999-12-31 23:59:59',
  'sidneygao',
  '2016-11-01 00:00:00',
  'sidneygao',
  '2016-11-01 00:00:00',
  '0');
INSERT INTO `aa`.`message` (`title`, `app_title`, `template`, `template_txt`, `type`, `event_type`, `user_groups`, `channels`, `message_category`, `web_url`, `app_url`, `status`, `read_count`, `activated_by`, `activated_time`, `expired_time`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`)
VALUES ('恭喜您已成功购买{0}个月V5会员！', '恭喜您已成功购买{0}个月V5会员！', '尊敬的用户，恭喜您已成功购买V5会员，有效期至{0}日，【马上投资】享受会员特权吧！',
                              '尊敬的用户，恭喜您已成功购买V5会员，有效期至{0}，【马上投资】享受会员特权吧！', 'EVENT', 'MEMBERSHIP_BUY_SUCCESS',
                              'ALL_USER',
                              'WEBSITE,APP_MESSAGE', 'SYSTEM', 'https://tuotiansudai.com/membership', 'MY_MEMBERSHIP',
  'APPROVED',
  '0',
  'sidneygao',
  '2016-11-01 00:00:00',
  '9999-12-31 23:59:59',
  'sidneygao',
  '2016-11-01 00:00:00',
  'sidneygao',
  '2016-11-01 00:00:00',
  '0');

COMMIT;