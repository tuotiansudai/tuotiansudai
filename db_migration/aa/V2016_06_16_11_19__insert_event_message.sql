BEGIN;
INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '终于等到你，欢迎来到拓天速贷平台。',
    'EVENT',
    'REGISTER_USER_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '实名认证成功，您的支付密码已经由联动优势发送至注册手机号码，请牢记。',
    'EVENT',
    'REGISTER_ACCOUNT_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '充值成功：您已成功充值 {0} 元，及时<a href="/loan-list">投资赚取更多</a>哦。',
    'EVENT',
    'RECHARGE_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '提现成功：您已成功提现 {0} 元，选择拓天，共赢财富。',
    'EVENT',
    'WITHDRAW_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '投标成功：您在<a href="/loan/{0}">{1}</a>项目成功投资 {2} 元，不日即将放款。',
    'EVENT',
    'INVEST_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '您发起的转让项目<a href="/transfer/{0}">{1}</a>已经转让成功，资金已经到达您的账户。',
    'EVENT',
    'TRANSFER_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '很遗憾，您发起的转让项目<a href="/transfer/{0}">{1}</a>没有转让成功。',
    'EVENT',
    'TRANSFER_FAIL',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '您投资的<a href="/loan/{0}">{1}</a>项目已经满额放款，快来<a href="/user-bill">查看收益</a>吧。',
    'EVENT',
    'LOAN_OUT_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '您投资的<a href="/loan/{0}">{1}</a>项目还款啦，赶快<a href="/user-bill">查看收益</a>吧。',
    'EVENT',
    'REPAY_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '您推荐的好友 {0} 成功注册，若该好友进行投资，您即可获取现金奖励哦',
    'EVENT',
    'RECOMMEND_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '您推荐的好友 {0} 成功进行了投资，您获得了 {1} 元现金奖励，<a href="/user-bill">立即查看</a>。',
    'EVENT',
    'RECOMMEND_AWARD_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '您获得了{0}，有效期{1}至{2}，<a href="/my-treasure">立即查看</a>。',
    'EVENT',
    'ASSIGN_COUPON_SUCCESS',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');

INSERT INTO `message` (`title`, `type`, `event_type`, `user_groups`, `channels`, `status`, `activated_by`, `activated_time`, `expired_time`, `updated_by`, `updated_time`, `created_by`, `created_time`)
  SELECT
    '您的{0}即将到期，使用可以获取额外收益哦，<a href="/my-treasure">立即使用</a>。',
    'EVENT',
    'COUPON_5DAYS_EXPIRED_ALERT',
    'ALL_USER',
    'WEBSITE',
    'APPROVED',
    'sidneygao',
    now(),
    '9999-12-31 23:59:59',
    'sidneygao',
    now(),
    'sidneygao',
    now()
  FROM dual
  WHERE EXISTS(SELECT 1
               FROM `user`
               WHERE login_name = 'sidneygao');
