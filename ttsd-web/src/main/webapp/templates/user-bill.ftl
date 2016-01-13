<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.user_bill}" activeNav="我的账户" activeLeftNav="资金管理" title="资金管理">
<div class="content-container user-bill-list-content">
    <h4 class="column-title"><em>资金管理</em></h4>
    <div class="money-box borderBox">
            <div class="balance">可用余额：<span>${balance} 元</span></div>
                <a class="btn-recharge btn-primary" href="/recharge">充值</a>
                <a class="btn-invest btn-action" href="/loan-list">投资</a>
                <a class="btn-withdraw btn-normal" href="/withdraw">提现</a>

            <p class="clearfix clear-blank-m">累计充值：<span>${rechargeAmount} 元</span></p>
            <p>累计提现：<span>${withdrawAmount} 元</span></p>
    </div>
<div class="clear-blank-m"></div>
    <div class="item-block date-filter ">
        <span class="sub-hd">起止时间:</span>
        <input type="text" id="date-picker" class="input-control" size="35"/>
        <span class="select-item current" data-day="1">今天</span>
        <span class="select-item" data-day="7">最近一周</span>
        <span class="select-item" data-day="30">一个月</span>
        <span class="select-item" data-day="180">六个月</span>
        <span class="select-item" data-day="">全部</span>
    </div>

    <div class="item-block status-filter">
        <span class="sub-hd">交易状态:</span>
        <span class="select-item current" data-status="">全部</span>
        <span class="select-item" data-status="WITHDRAW_SUCCESS,WITHDRAW_FAIL,APPLY_WITHDRAW">提现</span>
        <span class="select-item" data-status="RECHARGE_SUCCESS">充值</span>
        <span class="select-item" data-status="ACTIVITY_REWARD,REFERRER_REWARD">奖励</span>
        <span class="select-item" data-status="NORMAL_REPAY,ADVANCE_REPAY">本息</span>
        <span class="select-item" data-status="INVEST_SUCCESS">投标</span>
        <span class="select-item" data-status="NEWBIE_COUPON">体验券</span>
    </div>

    <div class="clear-blank"></div>
    <table class="user-bill-list table-striped"></table>
    <div class="pagination" data-url="/user-bill/user-bill-list-data" data-page-size="10"></div>
</div>
</@global.main>