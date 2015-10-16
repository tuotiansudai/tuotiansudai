<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="提现" pageCss="${css.withdraw}">
</@global.head>
<body>
<#include "header.ftl" />

<div class="main">
    <ul class="email-nav">
        <li><a href="javascript:">账户总览</a></li>
        <li><a href="javascript:">投资记录</a></li>
        <li><a href="javascript:">债权转让</a></li>
        <li><a href="javascript:">资金管理</a></li>
        <li><a href="javascript:">个人资产</a></li>
        <li><a href="javascript:">自动投标</a></li>
        <li><a href="javascript:">积分红包</a></li>
        <li><a href="javascript:">推荐管理</a></li>
    </ul>

    <div class="withdraw">
        <h2 class="title"><span>我要提现</span></h2>

        <div class="container">
            <p>提现额度：<i>${balance}</i>元</p>

            <p>
                提现金额：<input type="text" class="amount-display" data-d-group="4" data-l-zero="deny" data-v-min="0.00" data-v-max="${balance}" placeholder="0.00">元
                <span class="error"><em>金额必须大于3.00元</em></span>
            </p>

            <div class="calculate">
                <span>提现费用：<em>3.00</em> 元（每笔）</span>
                <span>实际到账：<em class="actual-amount">0.00</em> 元</span>
            </div>

            <button class="withdraw-submit inactive" disabled="disabled">确认提现</button>

            <form action="/withdraw" method="post" target="_blank">
                <input name="amount" type="hidden"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>

        <div class="tips">
            <h3>提现说明：</h3>
            <p>1、每笔提现，手续费3.00元。</p>
            <p>2、提现操作，T+1日内完成，均受节假日影响。</p>
            <p>3、提现银行卡姓名应与实名认证身份一致，才可提现。</p>
        </div>
    </div>
</div>

<div class="overlay"></div>
<div class="overlay-content">
    <div class="dg_wrapper dialog-chongzhi">
        <div class="hd">
            <h3>登录到联动优势支付平台充值</h3>
        </div>
        <div class="bd">
            <p>请在新打开的联动优势页面充值完成后选择：</p>

            <div class="ret">
                <p>充值成功：<a href="" class="g-btn g-btn-medium-major tongji"  data-category="确认成功" data-label="recharge">确认成功</a></p>
                <p>充值失败：<a href="" class="g-btn g-btn-medium-minor tongji js-close-btn" data-category="重新充值" data-label="recharge">重新充值</a>
                    <span class="help">查看<a href="" class="tongji" target="_blank" data-category="查看帮助中心" data-label="recharge">帮助中心</a></span>
                </p>
                <p style="font-size:14px">遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-22:00）</p>
            </div>
        </div>
        <a href="javascript:" class="js-close close tongji" data-category="关闭弹层" data-label="recharge"></a>
    </div>
</div>

<#include "footer.ftl">
<#--<@global.javascript pageJavascript="${js.withdraw}">-->
<#--</@global.javascript>-->
<script src="${requestContext.getContextPath()}/js/dest/config.min.js"></script>
<script src="${requestContext.getContextPath()}/js/libs/require-2.1.20.min.js"
        defer
        async="true"
        data-main="${requestContext.getContextPath()}/js/withdraw.js"></script>
</body>
</html>