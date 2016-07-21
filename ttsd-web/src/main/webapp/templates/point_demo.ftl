<#import "macro/global-dev.ftl" as global>
<@global.main pageCss="${css.account_mybean}" >

<div class="swiper-container">
    <ul class="left-nav swiper-wrapper">
        <li class="swiper-slide"><a href="/account">账户总览</a></li>
        <li class="swiper-slide"><a href="/investor/invest-list">我的投资</a></li>
        <li class="swiper-slide"><a href="/transferrer/transfer-application-list/TRANSFERABLE">债权转让</a></li>
        <li class="swiper-slide"><a href="/loaner/loan-list">我的借款</a></li>
        <li class="swiper-slide"><a href="/user-bill">资金管理</a></li>
        <li class="swiper-slide"><a class="active" href="/point">我的财豆</a></li>
        <li class="swiper-slide"><a href="/personal-info">个人资料</a></li>
        <li class="swiper-slide"><a href="/auto-invest">自动投标</a></li>
        <li class="swiper-slide"><a href="/referrer/refer-list">推荐管理</a></li>
        <li class="swiper-slide"><a href="/my-treasure">我的宝藏</a></li>
    </ul>
</div>

<div class="content-container my-choi-beans">
    <h4 class="column-title">
        <em class="tc title-navli active">我的财豆</em>
        <em class="tc title-navli">任务中心</em>
        <em class="tc title-navli">财豆兑换</em>
        <em class="tc title-navli">财豆明细</em>
    </h4>
    <div class="content-list">
		<div class="choi-beans-list ">

		</div>
        <div class="choi-beans-list active">
            <div class="title-task clearfix"><span class="active">新手任务</span></div>
            <div class="notice-tip">任务提醒：快去完成实名认证任务，领取200财豆奖励吧！</div>

            <div class="task-frame clearfix">
                <div class="task-box">
                    <span class="serial-number">01</span>
                    <dl class="step-content">
                        <dt>实名认证</dt>
                        <dd>说明：完成实名认证开通个人账户。</dd>
                        <dd class="reward">奖励：<span>200财豆</span></dd>
                        <dd><button type="button" class="btn-normal" disabled>已完成</button> </dd>
                    </dl>
                </div>
                <div class="task-box">
                    <span class="serial-number">02</span>
                    <dl class="step-content">
                        <dt>绑定银行卡</dt>
                        <dd>说明：绑定常用银行卡，赚钱快人一步。</dd>
                        <dd class="reward">奖励：<span>200财豆</span></dd>
                        <dd><button type="button" class="btn-normal">立即去完成</button> </dd>
                    </dl>
                </div>
                <div class="task-box">
                    <span class="serial-number">03</span>
                    <dl class="step-content">
                        <dt>首次充值</dt>
                        <dd>说明：完成实名认证开通个人账户。</dd>
                        <dd class="reward">奖励：<span>500财豆</span></dd>
                        <dd><button type="button" class="btn-normal" >立即去完成</button> </dd>
                    </dl>
                </div>
                <div class="task-box">
                    <span class="serial-number">04</span>
                    <dl class="step-content">
                        <dt>首次投资</dt>
                        <dd>说明：在拓天平台首次成功投资。</dd>
                        <dd class="reward">奖励：<span>1000财豆</span></dd>
                        <dd><button type="button" class="btn-normal">立即去完成</button> </dd>
                    </dl>
                </div>
            </div>

            <div class="title-task clearfix"><span>进阶任务</span><span class="active">进阶任务</span></div>

            <div class="task-status " style="display: none;">
                <div class="border-box ">
                    <dl class="fl">
                        <dt>累计投资满5000元奖励<span class="color-key"> 1000财豆</span></dt>
                        <dd>已累计投资<span class="color-key">10050.00元</span>，再投<span class="color-key">4050.00元</span>即可获得奖励</dd>
                    </dl>
                    <button class="fr btn-normal">去完成</button>
                </div>
                <div class="border-box">
                    <dl class="fl">
                        <dt>累计投资满5000元奖励<span class="color-key"> 1000财豆</span></dt>
                    </dl>
                    <button class="fr btn-normal">去完成</button>
                </div>
                <div class="border-box">
                    <dl class="fl">
                        <dt>累计投资满5000元奖励<span class="color-key"> 1000财豆</span></dt>
                        <dd>已邀请10名好友注册，获得了<span class="color-key">10000财豆</span>的奖励 <a href="#">查看邀请详情</a> </dd>
                    </dl>
                    <button class="fr btn-normal">去完成</button>
                </div>
                <div class="border-box">
                    <dl class="fl">
                        <dt>首次邀请好友投资奖励<span class="color-key">5000财豆</span></dt>
                    </dl>
                    <button class="fr btn-normal">去完成</button>
                </div>

                <div class="tc button-more">
                    <span class="btn-more"><a href="javascript:void(0);">点击查看更多任务 </a></span>
                </div>
            </div>
            <div class="task-status active" >
                <div class="border-box ">
                    <dl class="fl">
                        <dt>累计投资满15000元奖励2000财豆</dt>
                    </dl>
                    <button class="fr btn-normal" disabled>已完成</button>
                </div>
                <div class="border-box">
                    <dl class="fl">
                        <dt>累计投资满15000元奖励2000财豆</dt>
                    </dl>
                    <button class="fr btn-normal" disabled>已完成</button>
                </div>
                <div class="border-box">
                    <dl class="fl">
                        <dt>累计投资满15000元奖励2000财豆</dt>
                    </dl>
                    <button class="fr btn-normal" disabled>已完成</button>
                </div>
                <div class="border-box">
                    <dl class="fl">
                        <dt>累计投资满15000元奖励2000财豆</dt>
                    </dl>
                    <button class="fr btn-normal" disabled>已完成</button>
                </div>
                <div class="tc button-more">
                    <span class="btn-more"><a href="javascript:void(0);">点击查看更多任务 </a></span>
                </div>
            </div>

        </div>
    	<div class="choi-beans-list">

    	</div>

    	<div class="choi-beans-list invest-list-content">
    		<div class="item-block date-filter">
		        <span class="sub-hd">起止时间:</span>
		        <input type="text" id="date-picker" class="input-control" size="35"/>
		        <span class="select-item" data-day="1">今天</span>
		        <span class="select-item" data-day="7">最近一周</span>
		        <span class="select-item current" data-day="30">一个月</span>
		        <span class="select-item" data-day="180">六个月</span>
		        <span class="select-item" data-day="">全部</span>
		    </div>
		    <div class="item-block status-filter">
		        <span class="sub-hd">往来类型:</span>
		        <span class="select-item current" data-status="">全部</span>
		        <span class="select-item" data-status="SIGN_IN">已获取</span>
		        <span class="select-item" data-status="EXCHANGE">已使用</span>
		    </div>
		    <div class="clear-blank"></div>
		    <div class="point-bill-list">
		    </div>
            <div class="pagination" data-url="/point/point-bill-list-data" data-page-size="10">
		    </div>
    	</div>
    </div>

</div>
</@global.main>