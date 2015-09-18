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
                您的可用余额：${accountModelBalance}
            </div>
            <div class="item-block">
                <a class="bt-pay" href="">充值</a>
                <a class="bt-invest" href="">投资</a>
                <a class="bt-withdraw" href="">提现</a>
            </div>
            <p>累计充值： ￥${sumRecharge}</p>
            <p>累计提现： ￥${sumWithdraw}</p>
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
            <#if userBillDtos?? >
                <#list userBillDtos as userBillDto>
                    <tr>
                        <td>${userBillDto.createdTime}<td>
                        <td>${userBillDto.businessType}<td>
                        <td>${userBillDto.income}<td>
                        <td>${userBillDto.expenditure}<td>
                        <td>${userBillDto.freeze}<td>
                        <td>${userBillDto.balance}<td>
                        <td>${userBillDto.id}<td>
                    </tr>
                </#list>
                <div class="pagination">
                    <span class="total">共 <span class="subTotal">${countNum}</span>条,当前第 <span class="index-page">${currentPage}</span>页</span>
                    <span class="prev">上一页</span>
                    <a class="current" href="">1</a>
                    <a href="">2</a>
                    <a href="">20</a>
                    <span class="next">下一页</span>
                </div>
            <#else>
                <#--空白纪录-->
                <tr>
                    <td colspan="7" class="txtc">暂时没有资金纪录</td>
                </tr>
                <#--空白纪录 end-->
            </#if>
            </tbody>
        </table>


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
<@global.javascript pageJavascript="${js.fund}">
</@global.javascript>
</body>
</html>