BEGIN;

UPDATE help_center SET content='进入我的—设置—登录密码修改，按照提示操作即可。' WHERE id='100008';
UPDATE help_center SET content='进入我的，点击设置，点击支付密码，按指示操作即可。' WHERE id='100017';
UPDATE help_center SET content='进入我的，点击左上角个人资料—银行卡管理—更换银行卡，按照提示操作即可。' WHERE id='100021';
UPDATE help_center SET content='您在拓天平台完成一笔投资，平台即为您开通此功能；或您在我的—设置—-安心签中按照提示进行开启。' WHERE id='100047';
UPDATE help_center SET content='登录官方APP，点击banner下面的“邀请好友”，分享给朋好友或分享至朋友圈。' WHERE id='100057';
UPDATE help_center SET content='推荐好友投资即可获得好友年化投资1%的奖励，奖励在好友投资的项目满标放款后即发放到账户中。您邀请的好友再邀请人投资，您依然可以获得1%的奖励。
查看方式：平台官网：【我的账户】-【推荐管理】，即可进行查看。
手机客户端：进入首页banner下的“邀请好友”后，点击右上角的“我的奖励”即可查看。' WHERE id='100058';

COMMIT;

