begin;
  UPDATE help_center set content='拓天速贷是基于互联网的金融信息服务平台，由拓天伟业（北京）金融信息服务有限公司运营。
拓天速贷面向有财富增长需求、投资风格较为稳健、缺乏优质投资渠道的互联网投资用户，为其提供优质安全、收益可观的互联网投资服务。拓天速贷会在官网首页发布项目，展示经过严格风控审核的借款项目，投资人完成投资后可赚取收益。' WHERE category='KNOW_TTSD' and title='拓天速贷是做什么的？';
  UPDATE help_center set content='拓天速贷是一家稳健型的互联网金融信息服务平台，其优势有以下几个方面：
1、相比传统投资渠道，收益更高，约定年化利率8%-10%；门槛更低，50元即可投资；
2、资金银行存管，交易过程中的充值、投资、提现等页面操作都通过富滇银行进行，保证了资金流转的透明和安全；
3、提现便捷：5万元以下的提现，实时到账；5万元（包括）以上的提现，工作日8:30-17:00可申请提现（其他时间不允许提现），实时到账；
4、为用户提供安心签电子签章服务，其形成的数据电文或电子缔约文件符合中国法律规定，与纸质文件具有同样的法律效力。' where category='KNOW_TTSD' and title='拓天速贷的优势是什么？';

  UPDATE help_center set content='拓天速贷主要产品为房屋抵押借款、车辆抵押借款，50元起投；还款方式主要为：先付收益后还投资本金，按天计息，放款后生息；到期还本付息，按天计息，放款后生息。' where category='KNOW_TTSD' and title='拓天速贷有哪些产品？';

  UPDATE help_center set content='拓天速贷采用多管齐下的资金管理措施，包括通过富滇银行支付服务实现用户的资金与平台的隔离、严密风控、同卡同出以及安心签等措施，使得用户的资金安全更有保障。详见安全保障' where category='SECURITY' and title='我在拓天速贷的资金安全吗？';

  UPDATE help_center set content='实名认证，开立资金账户后，富滇银行会以短信的形式将支付密码发送到您的注册手机号上，投资标的时需要验证后方可支付成功。用户可通过身份验证或发送短信的形式修改您的支付密码。' where category='ACCOUNT_BANK_CARD' and title='什么是支付密码？';

  UPDATE help_center set content='免密支付是用户在投资过程中不需要跳转至富滇银行输入支付密码即可快速完成投资的功能。开通免密支付，可快速进行投资，抢得优质标的。' where category='ACCOUNT_BANK_CARD' and title='什么是免密支付？免密支付有什么优势？';

  UPDATE help_center set content='用户在拓天速贷平台上注册账号并实名认证后，即表示已在拓天速贷第三方支付合作方富滇银行开立了自己的资金账户。用户在交易过程中的充值、投资、提现都通过富滇银行进行，保证了资金流转的透明和安全。如果用户没有进行实名认证，则无法在富滇银行开立自己的资金账户。' where category='ACCOUNT_BANK_CARD' and title='如何开立自己的资金账户？';

  UPDATE help_center set content='充值不收费。提现每笔收手续费2.00元（富滇银行收取）。' where category='RECHARGE_WITHDRAW' and title='充值和提现收费吗？';

  UPDATE help_center set content='5万元以下的提现，实时到账；5万元（包括）以上的提现，工作日8:30-17:00可申请提现（其他时间不允许提现），实时到账。' where category='RECHARGE_WITHDRAW' and title='申请提现后，资金一般多久到账？';

  UPDATE help_center set content='有两种：按月还息，到期还本； 到期还本付息。' where category='INVEST_REPAY' and title='投资的项目如何进行回款？';

  UPDATE help_center set content='平台所有标的募集期限均为7天，如到期未筹满，该项目视为流标，投资资金将返回到第三方资金存管账户富滇银行账户中，届时您可以在账户余额中查看该笔资金。' where category='INVEST_REPAY' and title='投资项目的募集期限是多久？';

  UPDATE  help_center set content='目前会员有以下特权：
多重保障：资金银行存管、抵押模式、严格风控体系、平台信息透明、数据信息安全等保障；服务费折扣：平台向V0、V1会员收取利息的10%作为服务费，V2收取9%，V3、V4收取8%，V5仅收取7%；贵宾专线：贵宾级客服服务，投资问题，意见建议专享直达专享；理财顾问：发标时间，平台活动，投资顾问第一时间通知到您；生日福利：V5专享，平台将会在会员生日时送上神秘礼包。注：会员权益会不断提升，敬请期待。'  WHERE category='POINT_MEMBERSHIP' and title='会员有哪些特权？';

  DELETE FROM help_center WHERE  title='体验金如何投资？';

  DELETE FROM help_center WHERE  title='如何查看自己的体验金？';

  DELETE FROM help_center WHERE  title='体验金投资的收益为什么不能提现？';

  DELETE FROM help_center WHERE  category='COUPON_REFERRER';
COMMIT;
