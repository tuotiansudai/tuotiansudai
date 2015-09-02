<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="标的详情" pageCss="${css.loan_detail}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main">
    <div class="item-block bg-loan">
        <div class="news-share">
            <h2 class="hd">
                <span class="hot"></span>
                【活动标：逢十有礼】个人资金周转4.8万 一个月标aa
            </h2>

            <div class="chart-box">
                <div class="box">
                    <div class="bg"></div>
                    <div class="rount"></div>
                    <div class="bg2"></div>
                    <div class="rount2" style="display: none;"></div>
                    <span class="sub-percent">+1.00%</span>

                    <div id="num" class="num">15.00%</div>
                    <span class="title">年化收益率</span>
                </div>
            </div>
            <div class="chart-info">
                <p>已投：<span class="point">45%</span></p>

                <p>代理人： zhaozf132228</p>

                <p>借款人：hxanze</p>

                <p>项目期限：270天 </p>

                <p>还款方式：按月付息，到期还本</p>

                <p>起息时间：投资次日起息</p>
                <a href="">借款协议样本</a>
            </div>
        </div>
        <div class="account-info">
            <div class="ttsd-tips">拓天速贷提醒您：理财非存款，投资需谨慎！</div>
            <div class="item-block">
                <span class="sub-hd">项目金额：</span>
                <span class="num"><i>600000</i>元</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">可投金额：</span>
                <span class="num">600000元</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">账户余额：</span>
                <span class="num"><i class="red">600000</i>元</span>
            </div>
            <div class="item-block clearfix">
                <input type="text" value="" class="text-input"/>
                <span class="bg-yellow">最大可投金额</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">预计总收益：</span>
                <span class="num">600000元</span>
            </div>
            <div class="item-block">
                <button class="btn-pay" type="button">马上投资</button>
            </div>
        </div>
    </div>

    <div class="item-block bg-loan loan-box">
        <div class="nav">
            <ul>
                <li class="current"><span>借款详情</span></li>
                <li><span>出借纪录</span></li>
            </ul>
        </div>
        <div class="loan-list">
            <div class="loan-list-con" style="display: block;">
                <div class="loan-detail">
                    <h3>借款详情：</h3>

                    <p>1、借款人介绍：</p>

                    <p>1.1 借款人在京和朋友合伙经营某石业有限公司（大理石），经公司风控人员上门
                        实地考察；</p>

                    <p>1.2 核实客户年流水2100万；</p>

                    <p>1.3 核实客户月收入35万；</p>

                    <p>2、借款用途：短期资金周转</p>

                    <p>3、借款金额： 续借，230万；</p>
                </div>

                <div class="loan-material">
                    <h3>申请材料：</h3>

                    <div class="pic-list">
                        <div class="title">身份证：</div>
                        <ul class="img-list">
                            <li><img src="../../images/loan/tpl-1.jpg" alt=""/></li>
                            <li><img src="../../images/loan/tpl-1.jpg" alt=""/></li>
                            <li><img src="../../images/loan/tpl-1.jpg" alt=""/></li>
                        </ul>

                        <div class="title">房屋资料：</div>
                        <ul class="img-list">
                            <li><img src="../../images/loan/tpl-1.jpg" alt=""/></li>
                            <li><img src="../../images/loan/tpl-1.jpg" alt=""/></li>
                            <li><img src="../../images/loan/tpl-1.jpg" alt=""/></li>
                        </ul>

                        <div class="title">身份证：</div>
                        <ul class="img-list">
                            <li><img src="../../images/loan/tpl-1.jpg" alt=""/></li>
                            <li><img src="../../images/loan/tpl-1.jpg" alt=""/></li>
                            <li><img src="../../images/loan/tpl-1.jpg" alt=""/></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="loan-list-con">
                <table class="table-list">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>出借人</th>
                        <th>出借金额（元）</th>
                        <th>出借方式</th>
                        <th>预期利息（元）</th>
                        <th>出借时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1</td>
                        <td>ya**</td>
                        <td>8200.00</td>
                        <td>手动 <span class="icon-loan-ie"></span></td>
                        <td>738.00</td>
                        <td>2015-08-08 15:30:00</td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>ya**</td>
                        <td>8200.00</td>
                        <td>手动 <span class="icon-loan-Android"></span></td>
                        <td>738.00</td>
                        <td>2015-08-08 15:30:00</td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>ya**</td>
                        <td>8200.00</td>
                        <td>手动 <span class="icon-loan-ios"></span></td>
                        <td>738.00</td>
                        <td>2015-08-08 15:30:00</td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>ya**</td>
                        <td>8200.00</td>
                        <td>手动 <span class="icon-loan-Android"></span></td>
                        <td>738.00</td>
                        <td>2015-08-08 15:30:00</td>
                    </tr>
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
    </div>
</div>
<#--弹出层-->
<div class="layer-box">
    <div class="layer-wrapper"></div>
    <div class="content">
        <img src="images/house-1.jpg" alt="" width="760" height="450"/>
    </div>
</div>
<#--弹出层-->
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.loan_detail}">
</@global.javascript>
</body>
</html>