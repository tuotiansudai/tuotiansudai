<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="投资记录" pageCss="${css.global}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main-frame">
    <aside class="menu-box fl">
        <ul class="menu-list">
        <li><a href="javascript:;">账户总览</a></li>
        <li><a href="javascript:;">投资记录</a></li>
        <li><a href="javascript:;">债权转让</a></li>
        <li><a href="javascript:;">资金管理</a></li>
        <li><a href="javascript:;">个人资产</a></li>
        <li><a href="javascript:;">自动投标</a></li>
        <li><a href="javascript:;">积分红包</a></li>
        <li><a href="javascript:;">推荐管理</a></li>
        </ul>
    </aside>
    <div class="content-container fr invest-box-content">
        <h4 class="column-title"><em class="tc">投资记录</em></h4>

        <div class="item-block date-filter">
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
            <span class="select-item" data-status="RAISING">正在招募</span>
            <span class="select-item" data-status="RECHECK">招募成功</span>
            <span class="select-item" data-status="REPAYING">正在回款</span>
            <span class="select-item" data-status="COMPLETE">回款完毕</span>
        </div>
<div class="clear-blank"></div>
            <table class="invest-list table-striped">
            </table>
            <div class="pagination" data-url="/investor/invest-list-data" data-page-size="2">
            </div>

    </div>
</div>

<#include "footer.ftl">
<@global.javascript pageJavascript="${js.investor_invest_list}">
</@global.javascript>

</body>
</html>