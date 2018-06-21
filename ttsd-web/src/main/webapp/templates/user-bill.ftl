<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.user_bill}" pageJavascript="${js.user_bill}" activeNav="我的账户" activeLeftNav="资金管理" title="资金管理">
<div class="content-container user-bill-list-content" id="moneyMessagerContainer" data-bankcard="${hasBankCard?c}" data-account="${hasAccount?c}">
    <h4 class="column-title"><em>资金管理</em></h4>
    <div class="money-box clearfix">
        <div class="fl">
            <div class="balance">可用余额（元）</span><br/>
                <div class="money">${balance}</div>
            </div>

        </div>
        <div class="fl recharge-cash">
            <p><span class="icon-small icon-recharge"></span><span>累计充值(元)：</span><span class="money">${rechargeAmount} </span></p>
            <p><span class="icon-small icon-cash"></span><span>累计提现(元)：<span><span class="money">${withdrawAmount} </span></p>
        </div>
        <div class="btns">
            <a class="btn-recharge btn-list" href="/recharge">充值</a>
            <a class="btn-invest btn-list" href="/loan-list">投资</a>
            <a id="cashMoneyBtn" class="btn-withdraw btn-list" href="javascript:;">提现</a>
        </div>
    </div>
<div class="clear-blank-m"></div>
    <div class="item-block date-filter ">
        <span class="sub-hd">起止时间:</span>
        <input type="text" id="date-picker" class="input-control" size="35" readonly/>
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
        <span class="select-item" data-status="NEWBIE_COUPON,INVEST_COUPON,INTEREST_COUPON,RED_ENVELOPE,BIRTHDAY_COUPON">宝藏</span>
        <span class="select-item" data-status="INVEST_CASH_BACK">现金补贴</span>
    </div>

    <div class="clear-blank"></div>
    <table class="user-bill-list table-striped"></table>
    <div class="pagination" data-url="/user-bill/user-bill-list-data" data-page-size="10"></div>
</div>
<#-- -->

<div id="bankCardDOM" class="pad-m popLayer" style="display: none; padding-top:20px;padding-bottom: 0">

    <div class="tc text-m">您还没绑卡，请先进行绑卡</div>
<#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
    <form action="${requestContext.getContextPath()}/bank-card/bind/source/WEB" method="post" style="display: block">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="tc person-info-btn" style="margin-top:40px;">
            <button class="btn  btn-cancel btn-close" type="button">取消</button>&nbsp;&nbsp;&nbsp;
            <input id="accountBtn" class="btn btn-success" value="去绑卡" type="submit"/>
        </div>
    </form>

</div>
<script type="text/template" id="userBillTableTemplate">
    <table class="user-bill-list">
        <thead>
        <tr>
            <th>交易时间</th>
            <th>交易类型</th>
            <th class="tr">收入(元)</th>
            <th class="tr">支出(元)</th>
            <th class="tr">冻结(元)</th>
            <th class="tr">可用余额(元)</th>
            <th>备注</th>
        </tr>
        </thead>
        <tbody>
        <% for(var i = 0; i < records.length; i++) {
        var item = records[i];
        %>
        <tr>
            <td><%=item.createdTime%></td>
            <td><%=item.businessType%></td>
            <td class="tr">+<%=item.income%></td>
            <td class="tr">-<%=item.cost%></td>
            <td class="tr"><%=item.freeze%></td>
            <td class="tr"><%=item.balance%></td>
            <td>编号:<%=item.id%></td>
        </tr>
        <% } %>
        <%=records.length?'':'<tr><td colspan="7" class="no-data">暂时没有投资记录</td></tr>'%>
        </tbody>
    </table>
</script>
</@global.main>