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
                【活动标：逢十有礼】个人资金周转4.8万 一个月标
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
        </div>
    </div>


</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.loan_detail}">
</@global.javascript>
</body>
</html>