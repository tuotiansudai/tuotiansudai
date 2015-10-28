<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="充值" pageCss="${css.global}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="mainFrame">
    <aside class="menuBox fl">
        <ul class="menu-list">
        <li><a href="javascript:" class="active">账户总览</a></li>
        <li><a href="javascript:">投资记录</a></li>
        <li><a href="javascript:">资金管理</a></li>
        <li><a href="javascript:">个人资料</a></li>
        <li><a href="javascript:">自动投标</a></li>
        <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>
    <div class="recharge-container fr autoHeight">
        <h4 class="title"><em class="tc">我要充值</em></h4>

<<<<<<< HEAD
        <div class="recharge-content">
            <ul class="payment-mode clear">
                <li>
                    <i class="hot-flag"></i>
=======
        <div class="recharge-wrapper">
            <ul>
                <li class="fast-recharge-tab <#if isFastPayOn>active</#if>">
                    <span class="hot-flag">
                        <img src="${requestContext.getContextPath()}/images/recharge/hot.jpg" alt=""/>
                    </span>
>>>>>>> master
                    快捷支付
                </li>
                <li class="e-bank-recharge-tab <#if !isFastPayOn>active</#if>">个人网银</li>
            </ul>

            <div class="recharge-content">

                <div class="fast-recharge <#if isFastPayOn>active</#if>">

                <#if !isBindCard>
                    <div class="bind-card-nav">
                        <span>您尚未绑定银行卡，请先绑定银行卡！</span>
                        <input type="submit" class="submit" value="立即绑卡" data-url="${requestContext.getContextPath()}/bind-card"/>
                    </div>
                </#if>

                <#if !isFastPayOn && isBindCard>
                    <div class="turn-on-fast-form">
                        <form>
                            <p><label>姓名：</label><span>${userName}</span></p>

                            <p><label>身份证：</label><span>${identityNumber}</span></p>

                            <p><label>开户行：</label><span>${bank}</span></p>

                            <p><label>银行卡：</label><span>${bankCard}</span></p>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="submit" class="submit" value="开通快捷支付" />
                        </form>

                    </div>
                </#if>

                <#if isFastPayOn>
                    <div class="fast-recharge-form">
                        <form action="/recharge" method="post" target="_blank">
                            <p>账户可用余额：<i>${balance}</i> 元</p>

                            <p>输入充值金额：<input type="text" class="amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00">元</p>
                            <input type="hidden" name="bankCode" value="${bankCode}"/>
                            <input type="hidden" name="amount" value=""/>
                            <input type="hidden" name="source" value="WEB"/>
                            <input type="hidden" name="fastPay" value="true"/>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="submit grey" disabled="disabled">确认充值</button>
                        </form>
                    </div>
                </#if>
                </div>

                <div class="e-bank-recharge <#if !isFastPayOn>active</#if>">
                    <ol>
                        <p>请选择银行：</p>
<<<<<<< HEAD
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
=======
                    <#list banks as bank>
                        <li <#if (bank_index + 1) % 4 == 0>class="new-line"</#if>>
                            <input data-name="${bank}" type="radio" id="bank-${bank}" name="bank" <#if bank_index == 0>checked="checked"</#if>>
                            <label for="bank-${bank}"><img src="${requestContext.getContextPath()}/images/bank/${bank}.jpg" alt=""></label>
>>>>>>> master
                        </li>
                    </#list>
                    </ol>
                    <div class="recharge-form">
                        <form action="/recharge" method="post" target="_blank">
                            <p>账户可用余额：<i>${balance}</i> 元</p>

                            <p>输入充值金额：<input type="text" class="amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00">元</p>
                            <input class="selected-bank" type="hidden" name="backCode" value="CMB"/>
                            <input type="hidden" name="amount" value=""/>
                            <input type="hidden" name="source" value="WEB"/>
                            <input type="hidden" name="fastPay" value="false"/>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="submit" class="submit grey" disabled="disabled" value="确认充值"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="recharge-tips">
            <b>温馨提示:</b> <br/>
            1、为了您的账户安全，请在充值前进行实名认证。<br/>
            2、您的账户资金将通过第三方资金托管平台联动优势进行充值。<br/>
            3、请注意您的银行卡充值限制，以免造成不便。<br/>
            4、禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。<br/>
            5、如果充值金额没有及时到账，请联系客服。<br/>
            6、如果您的借记卡是中国工商银行、中国农业银行、中国建设银行、华夏银行、中国银行、中国邮政储蓄银行、浦发银行、交通银行、民生银行、广发银行、中信银行、兴业银行、光大银行、招商银行和平安银行，方可开通快捷支付。<br/>
        </div>
    </div>
</div>

<<<<<<< HEAD
    <div class="btn-box-layer">
        <a href="" class="now">立即去绑定</a>
        <a href="javascript:" class="cancel">取消</a>
    </div>
=======
<!--bind-card end-->
>>>>>>> master

    <div id="popRecharge">
        <p>请在新打开的联动优势页面充值完成后选择：</p>

<<<<<<< HEAD
        <div class="ret">
            <p>充值成功：<a href="" class="g-btn g-btn-medium-major tongji"  data-category="确认成功" data-label="recharge">确认成功</a></p>
            <p>充值失败：<a href="" class="g-btn g-btn-medium-minor tongji js-close-btn" data-category="重新充值" data-label="recharge">重新充值</a>
                <span class="help">查看<a href="" class="tongji" target="_blank" data-category="查看帮助中心" data-label="recharge">帮助中心</a></span>
            </p>
            <p style="font-size:14px">遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-22:00）</p>
=======
            <div class="ret">
                <p>充值成功：<a href="${requestContext.getContextPath()}/account" class="g-btn g-btn-medium-major tongji"
                           data-category="确认成功" data-label="recharge">确认成功</a></p>

                <p>充值失败：<a href="${requestContext.getContextPath()}/recharge" class="g-btn g-btn-medium-minor tongji js-close-btn" data-category="重新充值"
                           data-label="recharge">重新充值</a>
                    <span class="help">查看<a href="" class="tongji" target="_blank" data-category="查看帮助中心"
                                            data-label="recharge">帮助中心</a></span>
                </p>

                <p style="font-size:14px">遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-22:00）</p>
            </div>
>>>>>>> master
        </div>
    </div>
</div>
<!--bind-card end-->

<#include "footer.ftl">
<@global.javascript pageJavascript="${js.recharge}">
</@global.javascript>
</body>
</html>