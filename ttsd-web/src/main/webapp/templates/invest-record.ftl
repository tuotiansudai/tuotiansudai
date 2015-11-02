<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="投资记录" pageCss="${css.investrecord}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main">
    <ul class="email-nav">
        <li><a href="javascript:;">账户总览</a></li>
        <li><a href="/investor/invests">投资记录</a></li>
        <li><a href="javascript:;">债权转让</a></li>
        <li><a href="javascript:;">资金管理</a></li>
        <li><a href="javascript:;">个人资产</a></li>
        <li><a href="javascript:;">自动投标</a></li>
        <li><a href="javascript:;">积分红包</a></li>
        <li><a href="javascript:;">推荐管理</a></li>
    </ul>

    <div class="invest-box">
        <h2 class="hd"><span class="line">投资记录</span></h2>

        <div class="item-block start-end">
            <span class="sub-hd">起止时间:</span>
            <input type="text" id="daterangepicker" class="starttime filter" size="35" />
            <span class="jq-n rec-today current" day="1">今天</span>
            <span class="jq-n rec-week" day="7">最近一周</span>
            <span class="jq-n rec-month active" day="30">一个月</span>
            <span class="jq-n rec-sixmonth" day="180">六个月</span>
        </div>
        <div class="item-block query-type">
            <span class="sub-hd">交易状态:</span>
            <span class="current jq-n" data-value="">全部</span>
            <span class="jq-n" data-value="RAISING">正在招募</span>
            <span class="jq-n" data-value="RECHECK">招募成功</span>
            <span class="jq-n" data-value="REPAYING">正在回款</span>
            <span class="jq-n" data-value="COMPLETE">回款完毕</span>
        </div>
        <div id="tpl"></div>
    </div>

</div>
<#--弹出层-->
<div class="layer-box">
    <div class="layer-fix"></div>
    <div class="layer-con">
        <h2>
            <span class="hd"></span>
            <span class="close">x</span>
        </h2>

        <div class="table-list-box">
            <table class="table-list">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>应回款时间</th>
                    <th>应回款金额</th>
                    <th>应收本金</th>
                    <th>应收利息</th>
                    <th>利息管理费</th>
                    <th>实收金额</th>
                    <th>实收利息</th>
                    <th>实收罚息</th>
                    <th>实扣利息管理费</th>
                    <th>状态</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="11" align="left">
                        <p>应回款金额 = 应收本金 + 应收利息 - 利息管理费</p>
                        <p>实收金额 = 应收本金 + 实收利息 + 实收罚息 - 实扣利息管理费</p>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.investrecord}">
</@global.javascript>
</body>
</html>
<#--<script src="../js/libs/moment-2.10.6.min.js"></script>-->
<script>
    var API_AJAX  = "query-invests";
//    var dd = moment();
//    console.log(dd.subtract(10, 'days').format("YYYY-MM-DD"));
</script>