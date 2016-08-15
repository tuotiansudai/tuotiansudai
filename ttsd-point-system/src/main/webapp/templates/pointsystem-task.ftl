<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.pointsystem_task}" pageJavascript="${js.pointsystem_task}" activeNav="积分任务" activeLeftNav="" title="积分任务" site="membership">

<div class="global-member-task">
    <div class="wp clearfix">
        <div class="choi-beans-list ">
            <div class="title-task clearfix">
                <span class="active">新手任务</span>
            </div>
            <!-- <div class="notice-tip">
                任务提醒：快去完成绑定银行卡任务，领取200财豆奖励吧！
                <i class="fr fa fa-chevron-up"></i>
            </div> -->
            <div class="task-frame clearfix" id="taskFrame">
                <div class="task-box">
                    <span class="serial-number">1</span>
                    <dl class="step-content">
                        <dt>实名认证</dt>
                        <dd>说明：完成实名认证开通个人账户。</dd>
                        <dd class="reward">奖励：<span>200财豆</span></dd>
                        <dd>
                            <a class="btn-normal" href="javascript:void(0)" disabled="disabled">
                            已完成
                            </a>
                        </dd>
                    </dl>
                </div>
                <div class="task-box">
                    <span class="serial-number">2</span>
                    <dl class="step-content">
                        <dt>绑定银行卡</dt>
                        <dd>说明：绑定常用银行卡，赚钱快人一步。</dd>
                        <dd class="reward">奖励：<span>200财豆</span></dd>
                        <dd>
                            <a class="btn-normal" href="/bind-card">
                            立即去完成
                            </a>
                        </dd>
                    </dl>
                </div>
                <div class="task-box">
                    <span class="serial-number">3</span>
                    <dl class="step-content">
                        <dt>首次充值</dt>
                        <dd>说明：首次充值成功。</dd>
                        <dd class="reward">奖励：<span>500财豆</span></dd>
                        <dd>
                            <a class="btn-normal" href="/recharge">
                            立即去完成
                            </a>
                        </dd>
                    </dl>
                </div>
                <div class="task-box">
                    <span class="serial-number">4</span>
                    <dl class="step-content">
                        <dt>首次投资</dt>
                        <dd>说明：首次投资成功。</dd>
                        <dd class="reward">奖励：<span>1000财豆</span></dd>
                        <dd>
                            <a class="btn-normal" href="/loan-list">
                            立即去完成
                            </a>
                        </dd>
                    </dl>
                </div>
            </div>
            <div class="title-task clearfix" id="taskStatusMenu">
                <span class="active">进阶任务</span>
                <span>已完成任务</span>
            </div>
            <div class="task-status active">
                <div class="border-box two-col">
                    <dl class="fl">
                        <dt>累计投资满5000.00元奖励<span class="color-key">1000财豆</span></dt>
                        <dd>还差<span class="color-key">5000.00元</span>即可获得奖励</dd>
                    </dl>
                    <a href="/loan-list" class="fr btn-normal">去完成</a>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>单笔投资满10000.00元奖励<span class="color-key">2000财豆</span></dt>
                    </dl>
                    <a href="/loan-list" class="fr btn-normal">去完成</a>
                </div>
                <div class="border-box two-col">
                    <dl class="fl">
                        <dt>每邀请1名好友注册奖励<span class="color-key">200财豆</span></dt>
                        <dd>已邀请<span class="color-key">10名</span>好友注册，获得了<span class="color-key">10000财豆</span>的奖励 <a href="#" class="color-key">查看邀请详情</a></dd>
                    </dl>
                    <a href="/referrer/refer-list" class="fr btn-normal">去完成</a>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>首次邀请好友投资奖励<span class="color-key">5000财豆</span></dt>
                    </dl>
                    <a href="/referrer/refer-list" class="fr btn-normal">去完成</a>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>每邀请好友投资满1000元奖励<span class="color-key">1000财豆</span></dt>
                    </dl>
                    <a href="/referrer/refer-list" class="fr btn-normal">去完成</a>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>首次投资180天标的奖励<span class="color-key">1000财豆</span></dt>
                    </dl>
                    <a href="/loan-list" class="fr btn-normal">去完成</a>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>首次投资360天标的奖励<span class="color-key">1000财豆</span></dt>
                    </dl>
                    <a href="/loan-list" class="fr btn-normal">去完成</a>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>首次开通免密支付奖励<span class="color-key">500财豆</span></dt>
                    </dl>
                    <a href="/nopwdpay" class="fr btn-normal">去完成</a>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>首次开通自动投标奖励<span class="color-key">500财豆</span></dt>
                    </dl>
                    <a href="/auto-invest/agreement" class="fr btn-normal">去完成</a>
                </div>
                <div class="tc button-more">
                    <a href="javascript:void(0);"><span>点击查看更多任务</span> <i class="fa fa-chevron-circle-down"></i></a>
                </div>
            </div>
            <div class="task-status">
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>实名认证奖励<span class="color-key">200财豆</span></dt>
                    </dl>
                    <button class="fr btn-normal" disabled="">已完成</button>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>绑定邮箱奖励<span class="color-key">50财豆</span></dt>
                    </dl>
                    <button class="fr btn-normal" disabled="">已完成</button>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>实名认证奖励<span class="color-key">200财豆</span></dt>
                    </dl>
                    <button class="fr btn-normal" disabled="">已完成</button>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>绑定邮箱奖励<span class="color-key">50财豆</span></dt>
                    </dl>
                    <button class="fr btn-normal" disabled="">已完成</button>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>实名认证奖励<span class="color-key">200财豆</span></dt>
                    </dl>
                    <button class="fr btn-normal" disabled="">已完成</button>
                </div>
                <div class="border-box one-col">
                    <dl class="fl">
                        <dt>绑定邮箱奖励<span class="color-key">50财豆</span></dt>
                    </dl>
                    <button class="fr btn-normal" disabled="">已完成</button>
                </div>
                <div class="tc button-more">
                    <a href="javascript:void(0);"><span>点击查看更多任务</span> <i class="fa fa-chevron-circle-down"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>




</@global.main>