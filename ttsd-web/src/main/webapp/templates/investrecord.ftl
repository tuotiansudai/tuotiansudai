<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="投资纪录" pageCss="${css.investrecord}">
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

    <div class="invest-box">
        <h2 class="hd"><span class="line">投资记录</span></h2>

        <div class="item-block">
            <span class="sub-hd">起止时间:</span>
            <input type="text" id="daterangepicker" class="starttime filter" size="35"/>
            <a href="">今天</a>
            <a href="">最近一周</a>
            <a href="">一个月</a>
            <a href="">六个月</a>
            <a class="current" href="">全部</a>
        </div>
        <div class="item-block">
            <span class="sub-hd">交易状态:</span>
            <a class="current" href="">全部</a>
            <a href="">正在招募</a>
            <a href="">招募成功</a>
            <a href="">正在回款</a>
            <a href=""> 回款完毕</a>
        </div>
        <table class="invest-list">
            <thead>
            <tr>
                <th>交易时间</th>
                <th>交易详情</th>
                <th>交易状态</th>
                <th>下次回款</th>
                <th>投资项目</th>
                <th>状态</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>2015-06-11</td>
                <td>投资成功</td>
                <td>投资成功</td>
                <td>0.00元</td>
                <td>生意周转</td>
                <td>回款中</td>
            </tr>
            <tr>
                <td>2015-06-11</td>
                <td>投资成功</td>
                <td>投资成功</td>
                <td>0.00元</td>
                <td>生意周转</td>
                <td>回款中</td>
            </tr>
            <tr>
                <td>2015-06-11</td>
                <td>投资成功</td>
                <td>投资成功</td>
                <td>0.00元</td>
                <td>生意周转</td>
                <td>回款中</td>
            </tr>
            <tr>
                <td>2015-06-11</td>
                <td>投资成功</td>
                <td>投资成功</td>
                <td>0.00元</td>
                <td>生意周转</td>
                <td>回款中</td>
            </tr>

            <tr>
                <td colspan="6" class="txtc">暂时没有投资纪录</td>
            </tr>
            </tbody>
        </table>
    </div>

</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.investrecord}">
</@global.javascript>
</body>
</html>