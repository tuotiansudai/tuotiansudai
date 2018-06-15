<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.recharge}" pageJavascript="${js.recharge}" activeNav="我的账户" activeLeftNav="资金管理" title="充值">
<div class="content-container">
    <h4 class="column-title"><em class="tc">我要充值</em></h4>
    <div class="recharge-bind-card pad-s">
        <div class="bank-item">
            <div class="bank-list">
                <label>快捷支付限额一览：</label>
                <#if bankList??>
                    <i class="fa fa-sort-asc"></i>
                    <ul class="list-item" id="bankList">
                        <#list bankList as bank>
                            <li>${bank.name}:单笔${bank.singleAmount?number}元,单日${bank.singleDayAmount?number}元</li>
                        </#list>
                    </ul>
                </#if>
            </div>
        </div>
        <ul class="payment-mode clearfix">
            <li class="fast-recharge-tab active">
                <i class="hot-flag"></i>
                快捷支付
            </li>
            <li class="e-bank-recharge-tab">个人网银</li>
        </ul>
        <div class="recharge-wrapper">
            <div class="recharge-content">
                <div class="fast-recharge active">
                    <#if !bankCard??>
                        <div class="bind-card-nav">
                            <form action="${requestContext.getContextPath()}/bank-card/bind/source/WEB" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <span>您尚未绑定银行卡，请先绑定银行卡！</span>
                                <input type="submit" class="btn btn-normal" value="立即绑卡"
                                       data-url="${requestContext.getContextPath()}/bind-card"/>
                            </form>
                        </div>
                    <#else>
                        <div class="fast-recharge-form">
                            <form id="form2" action="/recharge" method="post"
                                  <@global.role hasRole="'INVESTOR', 'LOANER'">target="_blank"</@global.role>>
                                账户可用余额：<i>${balance}</i> 元 <br/>
                                输入充值金额：<input type="text" class="amount" data-d-group="3" data-l-zero="deny"
                                              data-v-min="0.00" placeholder="0.00"> 元
                                <span class="error" style="display: none;"><i class="fa fa-times-circle"></i>温馨提示：充值金额至少为1元</span>

                                <input type="hidden" name="amount" value=""/>
                                <input type="hidden" name="source" value="WEB"/>
                                <input type="hidden" name="PayType" value="FAST_PAY"/>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <#if bankModel??>
                                    <div class="limit-tips"><span>${bankModel.name}
                                        快捷支付限额:单笔${(bankModel.singleAmount/100)}元/单日${(bankModel.singleDayAmount/100)}元</span><i
                                            class="fa fa-question-circle text-b"
                                            title="限额由资金托管方提供，如有疑问或需要换卡，请联系客服400-169-1188"></i></div>
                                </#if>
                                <div class="tc pad-m">
                                    <button type="submit" class="btn" disabled="disabled">确认充值</button>
                                </div>
                            </form>
                        </div>
                    </#if>
                </div>
                <div class="e-bank-recharge">
                    <#if !bankCard??>
                        <div class="bind-card-nav">
                            <form action="${requestContext.getContextPath()}/bank-card/bind/source/WEB" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <span>您尚未绑定银行卡，请先绑定银行卡！</span>
                                <input style="display: block;margin:0 auto" type="submit" class="btn btn-normal" value="立即绑卡"
                                       data-url="${requestContext.getContextPath()}/bind-card"/>
                            </form>
                        </div>

                    <#else>
                        <div class="recharge-form">
                            <form action="/recharge" method="post"
                                  <@global.role hasRole="'INVESTOR', 'LOANER'">target="_blank"</@global.role>>
                                <div class="pad-m">
                                    账户可用余额：<i class="color-note">${balance}</i> 元 <br/>
                                    输入充值金额：<input type="text" class="amount" data-d-group="3" data-l-zero="deny"
                                                  data-v-min="0.00" placeholder="0.00"> 元
                                    <span class="error" style="display: none;"><i class="fa fa-times-circle"></i>温馨提示：充值金额至少为1元</span>
                                    <input type="hidden" name="amount" value=""/>
                                    <input type="hidden" name="PayType" value="GATE_PAY"/>
                                    <input type="hidden" name="source" value="WEB"/>
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <div class="tc clear-blank-m">
                                        <input type="submit" class="btn" disabled="disabled" value="确认充值"/>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </#if>
                </div>
            </div>
            <div class="clear-blank"></div>
            <div class="borderBox">
                <b>温馨提示:</b> <br/>

                1、为了您的账户安全，请在充值前进行实名认证。<br/>
                2、您的账户资金将通过富滇银行存管账号进行充值。<br/>
                3、请注意您的银行卡充值限制，以免造成不便。<br/>
                4、禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。<br/>
                5、如果充值金额没有及时到账，请联系客服。<br/>
            </div>
        </div>
    </div>

    <div id="popRecharge" class="pad-m" style="display: none;">
        <p>请在新打开的富滇银行页面充值完成后选择：</p>
        <p>充值成功：<a href="/account" class="btn-success" data-category="确认成功" data-label="recharge">确认成功</a></p>

        <p>充值失败：<a href="/recharge" class="btn-normal" data-category="重新充值" data-label="recharge">重新充值</a>
            <span class="help">查看<a href="/about/qa" target="_blank" data-category="查看帮助中心"
                                    data-label="recharge">帮助中心</a></span>
        </p>
        <span>遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-20:00）</span>
    </div>

</div>
</@global.main>