<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#--<@global.head title="借款记录" pageCss="${css.global}">-->
<#--</@global.head>-->
<head lang="en">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<title></title>
<link rel="stylesheet" type="text/css" href="${requestContext.getContextPath()}/style/global.css">
</head>
<body>
<#include "header.ftl" />
<div class="main-frame">
    <aside class="menu-box fl">
        <ul class="menu-list">
        <li><a href="javascript:">账户总览</a></li>
        <li><a href="javascript:">投资记录</a></li>
        <li><a href="javascript:">债权转让</a></li>
        <li><a href="javascript:">资金管理</a></li>
        <li><a href="javascript:">个人资产</a></li>
        <li><a href="javascript:">自动投标</a></li>
        <li><a href="javascript:">积分红包</a></li>
        <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>

    <div class="content-container fr loan-list-content">
        <h4 class="column-title"><em class="tc">借款记录</em></h4>

        <div class="item-block date-filter">
            <span class="sub-hd">起止时间:</span>
            <input type="text" id="date-picker" class="start-time filter" size="35"/>
            <span class="select-item current" data-day="1">今天</span>
            <span class="select-item" data-day="7">最近一周</span>
            <span class="select-item" data-day="30">一个月</span>
            <span class="select-item" data-day="180">六个月</span>
            <span class="select-item" data-day="">全部</span>
        </div>
        <div class="item-block status-filter">
            <span class="sub-hd">交易状态:</span>
            <span class="select-item current" data-status="REPAYING">还款中</span>
            <span class="select-item" data-status="COMPLETE">已结清</span>
            <span class="select-item" data-status="CANCEL">流标</span>
        </div>
        <div class="clear-blank"></div>
            <table class="loan-list table-striped">
            </table>
            <div class="pagination" data-url="/loaner/loan-list-data" data-page-size="2">
            </div>

    </div>
</div>

<#--弹出层-->
<div class="layer-container">

</div>
<#--弹出层end-->
<#include "footer.ftl">
<#--<@global.javascript pageJavascript="${js.loaner_loan_list}">-->
<#--</@global.javascript>-->

<script src="${requestContext.getContextPath()}/js/dest/${js.config}"></script>
<script src="${requestContext.getContextPath()}/js/libs/require-2.1.20.min.js"
defer
async="true"
data-main="${requestContext.getContextPath()}/js/loaner_loan_list.js"></script>
</body>
</html>

<script>
    var API_AJAX = '/loaner/loan-data';
</script>