<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#--<@global.head title="充值" pageCss="${css.recharge}">-->
<#--</@global.head>-->
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title></title>
    <link rel="stylesheet" type="text/css" href="${requestContext.getContextPath()}/style/global.css">

</head>
<body>
<#include "header.ftl" />
<div class="content">
    <aside class="menuBox fl">
        <ul class="menu-list">
        <li><a href="javascript:" class="actived">账户总览</a></li>
        <li><a href="javascript:">投资记录</a></li>
        <li><a href="javascript:">债权转让</a></li>
        <li><a href="javascript:">资金管理</a></li>
        <li><a href="javascript:">个人资产</a></li>
        <li><a href="javascript:">自动投标</a></li>
        <li><a href="javascript:">积分红包</a></li>
        <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>
    <div class="recharge-container fr autoHeight">
        <h4 class="title"><em class="tc">我要充值</em></h4>

        <div class="recharge-content">
            <ul class="payment-mode clear">
                <li>
                    <i class="hot-flag"></i>
                    快捷支付
                </li>
                <li class="active">个人网银</li>
            </ul>

            <div>
                <div class="fast-pay tab-box" style="display: none;">
                <#--已经绑定快捷支付-->
                    <div class="recharge-form" style="margin-top: 20px; display: none;">
                        <form action="/recharge" method="post" target="_blank">
                            <p>账户可用余额:<i>0.00</i>元</p>

                            <p>输入充值金额：<input name="amount" type="text" value="" class="recharge-cz" placeholder="0.00">元
                            </p>
                            <input class="jq-bank" type="hidden" name="bank" value="CMB"/>

                            <p class="p-h"><span>充值费用：<em>0.00</em> 元</span> <span>实际支付金额：<em>0.00 </em>元</span></p>

                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                            <button type="submit" class="recharge-submit grey" disabled="disabled">确认充值</button>
                        </form>
                    </div>
                <#--已经绑定快捷支付 end-->

                <#--绑定尚未开通快捷支付-->
                    <form action="" class="jq-open-fast-pay">
                        <div class="item-block box-user">
                            <span class="name">真实姓名：<em>皮冬</em></span>
                        </div>
                        <div class="item-block">
                            <span class="name">身份证号：<em>412829********10011</em></span>
                        </div>
                        <div class="item-block">
                            <span class="name">开户银行：<em>中国工商银行</em></span>
                        </div>
                        <div class="item-block">
                            <span class="name">银行卡：<em>65555555555888*77</em></span>
                        </div>
                        <div class="item-block">
                            <button class="btn-ok" type="submit">开通快捷支付</button>
                        </div>
                    </form>
                <#--绑定尚未开通快捷支付- end-->
                </div>

                <div class="e-bank-recharge">
                    <ol>
                        <p>请选择银行：</p>
                        <li>
                            <input data-name="CMB" type="radio" name="bank" id="bank-zs" checked="checked">
                            <label for="bank-zs"><img src="/images/bindcard/bank-zs.jpg" alt=""></label>
                        </li>
                        <li>
                            <input data-name="ICBC" type="radio" name="bank" id="bank-gs">
                            <label for="bank-gs"><img src="/images/bindcard/bank-gs.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CMBC" type="radio" name="bank" id="bank-ms">
                            <label for="bank-ms"><img src="/images/bindcard/bank-ms.jpg" alt=""> </label>
                        </li>
                        <li class="new-line">
                            <input data-name="CCB" type="radio" name="bank" id="bank-js">
                            <label for="bank-js"><img src="/images/bindcard/bank-js.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="BOC" type="radio" name="bank" id="bank-zg">
                            <label for="bank-zg"><img src="/images/bindcard/bank-zg.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="SPDB" type="radio" name="bank" id="bank-pf">
                            <label for="bank-pf"><img src="/images/bindcard/bank-pf.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CIB" type="radio" name="bank" id="bank-xy">
                            <label for="bank-xy"><img src="/images/bindcard/bank-xy.jpg" alt=""> </label>
                        </li>
                        <li class="new-line">
                            <input data-name="COMM" type="radio" name="bank" id="bank-jt">
                            <label for="bank-jt"><img src="/images/bindcard/bank-jt.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CEB" type="radio" name="bank" id="bank-gd">
                            <label for="bank-gd"><img src="/images/bindcard/bank-gd.jpg" alt=""> </label>
                        </li>
                        <li><input data-name="PSBC" type="radio" name="bank" id="bank-yz">
                            <label for="bank-yz"><img src="/images/bindcard/bank-yz.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="HXB" type="radio" name="bank" id="bank-hx">
                            <label for="bank-hx"><img src="/images/bindcard/bank-hx.jpg" alt=""> </label>
                        </li>
                        <li class="new-line">
                            <input data-name="BJBANK" type="radio" name="bank" id="bank-bj">
                            <label for="bank-bj"><img src="/images/bindcard/bank-bj.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CITIC" type="radio" name="bank" id="bank-zx">
                            <label for="bank-zx"><img src="/images/bindcard/bank-zx.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="WZCB" type="radio" name="bank" id="bank-wz"><label for="bank-wz"><img
                                src="/images/bindcard/bank-wz.jpg" alt=""> </label></li>
                        <li>
                            <input data-name="SHRCB" type="radio" name="bank" id="bank-s">
                            <label for="bank-s"><img src="/images/bindcard/bank-sh.jpg" alt=""> </label>
                        </li>
                        <li class="new-line">
                            <input data-name="ABC" type="radio" name="bank-n" id="bank-ny">
                            <label for="bank-ny"><img src="/images/bindcard/bank-ny.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="GDB" type="radio" name="bank" id="bank-gf">
                            <label for="bank-gf"><img src="/images/bindcard/bank-gf.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="BEA" type="radio" name="bank" id="bank-dy">
                            <label for="bank-dy"><img src="/images/bindcard/bank-dy.jpg" alt=""> </label>
                        </li>
                    </ol>
                    <div class="recharge-form">
                        <form action="/recharge" method="post" target="_blank">
                            <p>账户可用余额：<i>${balance}</i>元</p>
                            <p>输入充值金额：<input type="text" class="e-bank-amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00">元</p>
                            <input class="selected-bank" type="hidden" name="bank" value="CMB"/>
                            <input type="hidden" name="amount" />
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                            <button type="submit" class="recharge-submit grey" disabled="disabled">确认充值</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="recharge-tips">
            <p>温馨提示</p>
            <p>1、为了您的账户安全，请在充值前进行身份认证、手机绑定以及提现密码设置。</p>
            <p>2、您的账户资金将通过联动优势平台进行充值。</p>
            <p>3、请注意您的银行卡充值限制，以免造成不便。</p>
            <p>4、禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</p>
            <p>5、如果充值金额没有及时到账，请联系客服。</p>
        </div>
    </div>
</div>
<div class="bind-card-layer"><!--bind-card begin-->
    <span class="close2">X</span>
    <h4>安全验证</h4>

    <p>您还未绑定银行卡，请您绑定银行卡！</p>

    <div class="btn-box-layer">
        <a href="" class="now">立即去绑定</a>
        <a href="javascript:" class="cancel">取消</a>
    </div>

    <div id="popRecharge">
        <p>请在新打开的联动优势页面充值完成后选择：</p>

        <div class="ret">
            <p>充值成功：<a href="" class="g-btn g-btn-medium-major tongji"  data-category="确认成功" data-label="recharge">确认成功</a></p>
            <p>充值失败：<a href="" class="g-btn g-btn-medium-minor tongji js-close-btn" data-category="重新充值" data-label="recharge">重新充值</a>
                <span class="help">查看<a href="" class="tongji" target="_blank" data-category="查看帮助中心" data-label="recharge">帮助中心</a></span>
            </p>
            <p style="font-size:14px">遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-22:00）</p>
        </div>
    </div>
</div>
<!--bind-card end-->

<#include "footer.ftl">
<#--<@global.javascript pageJavascript="${js.recharge}">-->
<#--</@global.javascript>-->
<script src="${requestContext.getContextPath()}/js/dest/${js.config}"></script>
<script src="${requestContext.getContextPath()}/js/libs/require-2.1.20.min.js"
        defer
        async="true"
        data-main="${requestContext.getContextPath()}/js/recharge.js"></script>

</body>
</html>