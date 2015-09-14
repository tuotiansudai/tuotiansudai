<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="借款纪录" pageCss="${css.loanrecord}">
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
            <input type="text" id="daterangepicker" class="starttime filter" size="35" />
            <span class="jq-n rec-today current" day="1">今天</span>
            <span class="jq-n rec-week" day="7">最近一周</span>
            <span class="jq-n rec-month " day="30">一个月</span>
            <span class="jq-n rec-sixmonth" day="180">六个月</span>
        </div>
        <div class="item-block query-type">
            <span class="sub-hd">交易状态:</span>
            <span class="current jq-n" data-value="refund">全部</span>
            <span class="jq-n" data-value="refund">还款中</span>
            <span class="jq-n" data-value="refund">招募中</span>
            <span class="jq-n" data-value="refund">已结清</span>
        </div>
        <table class="invest-list">
            <thead>
            <tr>
                <th>项目名称</th>
                <th>放款时间</th>
                <th>借款金额</th>
                <th>待还款总额</th>
                <th>下次还款</th>
                <th>状态</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>test</td>
                <td>2015-08-24 17:56:23</td>
                <td>10000.00</td>
                <td>1.00</td>
                <td>2015-08-24 17:56:23</td>
                <td>还款中 <span class="plan">还款计划合同</span></td>
            </tr>
            <tr>
                <td>test</td>
                <td>2015-08-24 17:56:23</td>
                <td>10000.00</td>
                <td>1.00</td>
                <td>2015-08-24 17:56:23</td>
                <td>还款中 <span class="plan">还款计划合同</span></td>
            </tr>
            <tr>
                <td>test</td>
                <td>2015-08-24 17:56:23</td>
                <td>10000.00</td>
                <td>1.00</td>
                <td>2015-08-24 17:56:23</td>
                <td>还款中 <span class="plan">还款计划合同</span></td>
            </tr>
            <tr>
                <td>test</td>
                <td>2015-08-24 17:56:23</td>
                <td>10000.00</td>
                <td>1.00</td>
                <td>2015-08-24 17:56:23</td>
                <td>还款中 <span class="plan">还款计划合同</span></td>
            </tr>
            <tr>
                <td>test</td>
                <td>2015-08-24 17:56:23</td>
                <td>10000.00</td>
                <td>1.00</td>
                <td>2015-08-24 17:56:23</td>
                <td>还款中 <span class="plan">还款计划合同</span></td>
            </tr>
            <tr>
                <td>test</td>
                <td>2015-08-24 17:56:23</td>
                <td>10000.00</td>
                <td>1.00</td>
                <td>2015-08-24 17:56:23</td>
                <td>还款中 <span class="plan">还款计划合同</span></td>
            </tr>

            <#--空白纪录-->
            <tr>
                <td colspan="6" class="txtc">暂时没有借款纪录</td>
            </tr>
            <#--空白纪录 end-->
            </tbody>
        </table>

        <div class="pagination">
            <span class="total">共 <span class="subTotal">20</span>条,当前第 <span class="index-page">1</span>页</span>
            <span class="prev">上一页</span>
            <a class="current" href="">1</a>
            <a href="">2</a>
            <a href="">20</a>
            <span class="next">下一页</span>
        </div>
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
                    <th>利息</th>
                    <th>罚息</th>
                    <th>手续费</th>
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
<@global.javascript pageJavascript="${js.loanrecord}">
</@global.javascript>
</body>
</html>

<script>
    var api_list = "../js/fast-pay.json";
</script>