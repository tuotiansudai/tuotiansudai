<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="充值" pageCss="${css.recharge}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="content"><!--content begin-->
    <ul class="email-nav">
        <li><a href="javascript:;">账户总览</a></li>
        <li><a href="javascript:;">投资记录</a></li>
        <li><a href="javascript:;">债权转让</a></li>
        <li><a href="javascript:;">资金管理</a></li>
        <li><a href="javascript:;">个人资产</a></li>
        <li><a href="javascript:;">积分红包</a></li>
        <li><a href="javascript:;">我们推广</a></li>
    </ul>
    <div class="content-recharge">
        <p class="p-e"><em>我要充值</em></p>

        <div class="banking">
            <ul>
                <li class="active">个人网银</li>
                <li>快捷支付</li>
            </ul>
            <div class="box-banking">
                <ol style="display: block;">
                    <p>充值银行</p>
                    <li>
                        <input data-name="CMB" type="radio" name="bank" id="bank-zs" checked="checked">
                        <label for="bank-zs"><img src="/images/recharge/bank-zs.jpg" alt=""></label>
                    </li>
                    <li>
                        <input data-name="ICBC" type="radio" name="bank" id="bank-gs">
                        <label for="bank-gs"><img src="/images/recharge/bank-gs.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="CMBC" type="radio" name="bank" id="bank-ms">
                        <label for="bank-ms"><img src="/images/recharge/bank-ms.jpg" alt=""> </label>
                    </li>
                    <li class="m-right-0">
                        <input data-name="CCB" type="radio" name="bank" id="bank-js">
                        <label for="bank-js"><img src="/images/recharge/bank-js.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="BOC" type="radio" name="bank" id="bank-zg">
                        <label for="bank-zg"><img src="/images/recharge/bank-zg.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="SPDB" type="radio" name="bank" id="bank-pf">
                        <label for="bank-pf"><img src="/images/recharge/bank-pf.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="CIB" type="radio" name="bank" id="bank-xy">
                        <label for="bank-xy"><img src="/images/recharge/bank-xy.jpg" alt=""> </label>
                    </li>
                    <li class="m-right-0">
                        <input data-name="COMM" type="radio" name="bank" id="bank-jt">
                        <label for="bank-jt"><img src="/images/recharge/bank-jt.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="CEB" type="radio" name="bank" id="bank-gd">
                        <label for="bank-gd"><img src="/images/recharge/bank-gd.jpg" alt=""> </label>
                    </li>
                    <li><input data-name="PSBC" type="radio" name="bank" id="bank-yz">
                        <label for="bank-yz"><img src="/images/recharge/bank-yz.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="HXB" type="radio" name="bank" id="bank-hx">
                        <label for="bank-hx"><img src="/images/recharge/bank-hx.jpg" alt=""> </label>
                    </li>
                    <li class="m-right-0">
                        <input data-name="BJBANK" type="radio" name="bank" id="bank-bj">
                        <label for="bank-bj"><img src="/images/recharge/bank-bj.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="CITIC" type="radio" name="bank" id="bank-zx">
                        <label for="bank-zx"><img src="/images/recharge/bank-zx.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="WZCB" type="radio" name="bank" id="bank-wz"><label for="bank-wz"><img
                            src="/images/recharge/bank-wz.jpg" alt=""> </label></li>
                    <li>
                        <input data-name="SHRCB" type="radio" name="bank" id="bank-s">
                        <label for="bank-s"><img src="/images/recharge/bank-sh.jpg" alt=""> </label>
                    </li>
                    <li class="m-right-0">
                        <input data-name="ABC" type="radio" name="bank-n" id="bank-ny">
                        <label for="bank-ny"><img src="../images/recharge/bank-ny.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="GDB" type="radio" name="bank" id="bank-gf">
                        <label for="bank-gf"><img src="/images/recharge/bank-gf.jpg" alt=""> </label>
                    </li>
                    <li>
                        <input data-name="BEA" type="radio" name="bank" id="bank-dy">
                        <label for="bank-dy"><img src="/images/recharge/bank-dy.jpg" alt=""> </label>
                    </li>
                </ol>

                <ol class="fast-pay">
                    <li>
                        <#--用户尚未绑定银行卡快捷支付-->
                        <form action="">
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


                    </li>

                </ol>


            </div>

        </div>
        <div class="recharge-bank">
            <form action="/recharge" method="post" target="_blank">
                <p>账户可用余额:<i>0.00</i>元<b>(注：项目起投金额为100的整数倍)</b></p>

                <p>输入充值金额：<input name="amount" type="text" value="" class="recharge-cz" placeholder="0.00">元 </p>
                <input class="jq-bank" type="hidden" name="bank" value="CMB"/>

                <p class="p-h"><span>充值费用：<em>0.00</em> 元</span> <span>实际支付金额：<em>0.00 </em>元</span></p>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <button type="submit" class="recharge-qr grey">确认充值</button>
            </form>
        </div>
        <div class="cash-description">
            <p>温馨提示</p>

            <p>1、为了您的账户安全，请在充值前进行身份认证、手机绑定以及提现密码设置。</p>

            <p>2、您的账户资金将通过第三方平台进行充值。</p>

            <p>3、请注意您的银行卡充值限制，以免造成不便。</p>

            <p>4、禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</p>

            <p>5、如果充值金额没有及时到账，请联系客服。</p>
        </div>
    </div>
</div>
<div class="bind-card"><!--bind-card begin-->
    <span class="close2">X</span>
    <h4>安全验证</h4>

    <p>您还未绑定银行卡，请您绑定银行卡！</p>
    <a href="javascript:" class="now">立即去绑定</a>
</div>
<!--bind-card end-->
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.recharge}">
</@global.javascript>
</body>
</html>