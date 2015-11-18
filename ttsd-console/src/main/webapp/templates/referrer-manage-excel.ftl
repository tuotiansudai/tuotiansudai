<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
</head>
<body>
<div>
    <table border="1">
        <thead>
        <tr>
            <th>项目名称</th>
            <th>期数</th>
            <th>投资人</th>
            <th>投资人姓名</th>
            <th>投资金额</th>
            <th>投资时间</th>
            <th>推荐人</th>
            <th>推荐人姓名</th>
            <th>推荐人是否业务员</th>
            <th>推荐层级</th>
            <th>推荐奖励</th>
            <th>奖励状态</th>
            <th>奖励时间</th>
        </tr>
        </thead>
        <tbody>
        <#list referrerManageViews as referrerManageView>
        <tr>
            <td>${referrerManageView.loanName!}</td>
            <td>${referrerManageView.periods?string('0')}</td>
            <td>${referrerManageView.investLoginName!}</td>
            <td>${referrerManageView.investName!}</td>
            <td>${referrerManageView.investAmount/100}</td>
            <td>${referrerManageView.investTime?string('yyyy-MM-dd HH:mm:ss')}</td>
            <td>${referrerManageView.referrerLoginName!}</td>
            <td>${referrerManageView.referrerName!}</td>
            <td><#if referrerManageView.role?? && referrerManageView.role == 'STAFF'>是<#else>否</#if></td>
            <td>${referrerManageView.level?string('0')}</td>
            <td>${referrerManageView.rewardAmount/100}</td>
            <td><#if referrerManageView.status?? && referrerManageView.status == 'SUCCESS'>已入账<#else>入账失败</#if></td>
            <td>${referrerManageView.rewardTime?string('yyyy-MM-dd HH:mm:ss')}</td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>