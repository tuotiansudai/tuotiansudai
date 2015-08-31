<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="绑定银行卡" pageCss="${css.bind_card}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main">

    <ul class="email-nav">
        <li><a href="javascript:;">账户总览</a></li>
        <li><a href="javascript:;">投资记录</a></li>
        <li><a href="javascript:;">资金纪录</a></li>
        <li><a href="javascript:;">个人资料</a></li>
        <li><a href="javascript:;">自动投标</a></li>
        <li><a href="javascript:;">推荐管理</a></li>
    </ul>


    <div class="bind-card">
        <h2 class="hd-bind-card"><span>绑定银行卡</span></h2>

        <div class="card-list">
        <#--用户尚未绑定银行卡快捷支付-->
            <form action="" style="display: none">
                <div class="item-block">
                    <span class="name">真实姓名：皮冬</span>
                </div>
                <div class="item-block">
                    <span class="name">身份证号：412829********10011</span>
                </div>
                <div class="item-block">
                    <span class="name">银行卡：</span>
                    <input class="input-bankcard" type="text" value=""/>
                </div>
                <div class="item-block">
                    <button class="btn-ok" type="submit">确认绑定</button>
                </div>
            </form>
        <#--用户尚未绑定银行卡快捷支付-->

        <#--未开通快捷支付-->
            <div class="card-box" style="display:block ;">
                <h4 class="hd-card">
                    <span class="logo-card"><img src="${requestContext.getContextPath()}/images/bindcard/logo-ICBC.jpg"
                                                 alt=""/></span>
                    <span class="user">皮东</span>
                </h4>

                <div class="card-num">6212**** **** ***4123</div>
                <div class="options">
                    <a class="cart-edit" href="">修改</a>
                    <a class="open-fast-pay" href="">开通快捷支付</a>
                </div>
            </div>
        <#--未开通快捷支付-->
        </div>
        <div class="tips">
            <h3>温馨提示</h3>

            <p>1、不支持提现至信用卡账户。</p>

            <p>2、由于银行卡保护机制均由联动优势提供，故您的银行卡将通过拓天平台绑定到联动优势平台上进行第三方托管。</p>

            <p>3、如果您的借记卡是中国工商银行，中国农业银行，中国建设银行，中国银行，光大银行，兴业银行，深圳发展银行七家之一，
                才可开通快捷支付。
            </p>

            <p>4、当您的账户余额为零的时候，系统才会支持您更换银行卡操作。
            </p>

            <p>5、如果您已经开通快捷支付，系统不再支持您更换银行卡。</p>
        </div>
    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.bind_card}">
</@global.javascript>
</body>
</html>