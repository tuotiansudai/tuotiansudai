<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="资金管理" pageCss="${css.fund}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main">
    <ul class="email-nav">
        <li><a href="javascript:;">账户总览</a></li>
        <li><a href="javascript:;">投资记录</a></li>
        <li><a href="javascript:;">债权转让</a></li>
        <li><a href="javascript:;">资金管理</a></li>
        <li><a href="javascript:;">个人资产</a></li>
        <li><a href="javascript:;">自动投标</a></li>
        <li><a href="javascript:;">积分红包</a></li>
        <li><a href="javascript:;">推荐管理</a></li>
    </ul>

    <div class="fund-box">
        <h2 class="hd"><span class="line">资金管理</span></h2>
        <div class="money-box">
            <div class="balance">
                您的可用余额：
            </div>
            <div class="item-block">
                <a class="bt-pay" href="">充值</a>
                <a class="bt-invest" href="">投资</a>
                <a class="bt-withdraw" href="">提现</a>
            </div>
            <p>累计充值： ￥</p>
            <p>累计提现： ￥</p>
        </div>
        <div class="item-block start-end">
            <span class="sub-hd">起止时间:</span>
            <input type="text" id="daterangepicker" class="starttime filter" size="35" />
            <span class="jq-n rec-today" day="1">今天</span>
            <span class="jq-n rec-week current" day="7">最近一周</span>
            <span class="jq-n rec-month active" day="30">一个月</span>
            <span class="jq-n rec-sixmonth" day="180">六个月</span>
        </div>
        <div class="item-block query-type">
            <span class="sub-hd">交易状态:</span>
            <span class="current jq-n" data-value="refund">全部</span>
            <span class="jq-n" data-value="refund">提现</span>
            <span class="jq-n" data-value="refund">线上充值</span>
            <span class="jq-n" data-value="refund">奖励</span>
            <span class="jq-n" data-value="refund">利息</span>
            <span class="jq-n" data-value="refund">投标</span>
        </div>
        <table class="fund-list">
            <thead>
            <tr>
                <th>交易时间</th>
                <th>交易类型</th>
                <th>收入</th>
                <th>支出</th>
                <th>冻结</th>
                <th>可用余额</th>
                <th>备注</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
        </table>


    </div>
</div>

<#include "footer.ftl">
<@global.javascript pageJavascript="${js.fund}">
</@global.javascript>
</body>
</html>


<script>

    var _API_FUND = '';
</script>