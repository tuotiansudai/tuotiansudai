<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="提现" pageCss="${css.withdraw}">
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


    <div class="bind-card">
        <h2 class="hd-bind-card"><span>我要提现</span></h2>

        <div class="card-list">
            <form action="/withdraw" method="post" target="_blank">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="recharge-bank">
                    <p>可提现额度:<i class="jq-total">900.00</i>元</p>
                    <p>输入提现金额：<input name="amount" type="text" value="" class="recharge-cz" placeholder="0.00">元
                        <span class="error"><img src="${requestContext.getContextPath()}/images/error.jpg" alt=""/>你最大可提现金额:<i>0.00</i>元</span>
                    </p>
                    <p class="p-h"><span>提现费用：<em>3.00</em> 元</span></p>
                    <p class="p-h jq-sj"><span>实际到账：<em>0.00</em> 元</span></p>
                    <button type="submit" class="recharge-qr grey" disabled="disabled">确认提现</button>
                </div>
            </form>
        </div>
        <div class="tips">
            <h3>提现说明：</h3>

            <p>1、每提现一笔，提现操作手续费为3元。</p>

            <p>2、提现操作，T+1日内完成，均受节假日影响。</p>

            <p>3、提现银行卡姓名应与实名认证身份一致，才可提现。</p>

        </div>
    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.withdraw}">
</@global.javascript>
</body>
</html>