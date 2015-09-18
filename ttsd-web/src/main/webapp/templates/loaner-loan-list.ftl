<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="借款纪录" pageCss="${css.loaner_loan_list}">
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
        <h2 class="hd"><span class="line">借款纪录</span></h2>

        <div class="item-block start-end">
            <span class="sub-hd">起止时间:</span>
            <input type="text" id="daterangepicker" class="starttime filter" size="35"/>
            <span class="jq-n rec-today current" day="1">今天</span>
            <span class="jq-n rec-week" day="7">最近一周</span>
            <span class="jq-n rec-month " day="30">一个月</span>
            <span class="jq-n rec-sixmonth" day="180">六个月</span>
            <span class="jq-n" day=""">全部</span>
        </div>
        <div class="item-block query-type">
            <span class="sub-hd">交易状态:</span>
            <span class="jq-n current" data-value="REPAYING">还款中</span>
            <span class="jq-n" data-value="COMPLETE">已结清</span>
            <span class="jq-n" data-value="CANCEL">流标</span>
        </div>
        <div id="tpl"></div>

    </div>
</div>

<#--弹出层-->
<div class="layer-box">
    <div class="layer-fix"></div>
    <div class="layer-con">
        <h2>
            <span class="hd">提前还款(本金:0.0;手续费:0.0;罚息:0.0)</span>
            <span class="close">x</span>
        </h2>

        <div class="table-list-box">
            <table class="table-list">
                <thead>
                <tr>
                    <th>期数</th>
                    <th>本金</th>
                    <th>预计利息</th>
                    <th>实际利息</th>
                    <th>罚息</th>
                    <th>总额</th>
                    <th>还款日</th>
                    <th>还款时间</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>

                </tr>
                <tr>
                    <td>1</td>
                    <td>1.00</td>
                    <td>1.00</td>
                    <td>0</td>
                    <td>0</td>
                    <td>1</td>
                    <td>200</td>
                    <td>2015-09-24</td>
                    <td>完成</td>
                    <td></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<#--弹出层end-->
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.loaner_loan_list}">
</@global.javascript>
</body>
</html>

<script>
    var API_AJAX = 'loaner/fake-loan-data';
</script>