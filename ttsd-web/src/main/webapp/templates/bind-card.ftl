<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.bind_card}" activeNav="我的账户" activeLeftNav="个人资料" title="绑定银行卡">

<div class="content-container">
    <h4 class="column-title"><em class="tc">绑定银行卡</em></h4>
    <div class="recharge-bind-card pad-s">
    <div class="recharge-wrapper bind-card-frame" id="bindCardBox">
        <#if isBindCard>
        <div class="card-box">
            <form class="open-fast-pay-form" action="/agreement" method="post" target="_blank">
                <div class="pad-s">
                    <img class="logo-card fl" src="${staticServer}/images/bank/logo-${bankCode}.png" />
                    <span class="user fr">${userName}</span>
                </div>

                    <div class="card-num tc">${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <div class="card-link clearfix">
                        <a class="card-edit fl" href="/bind-card/replace">换卡</a>
                        <#if openFastPayAvailable>
                            <input type="hidden" name="fastPay" value="true"/>
                            <a class="open-fast-pay fr" href="javascript:">开通快捷支付</a>
                        </#if>
                    </div>

            </form>
        </div>
        <#else>
        <form action="" method="post" target="_blank">
            真实姓名：${userName}
            <div class="clear-blank"></div>
            <div class="e-bank-recharge">
                <b class="title">选择银行:</b>
                <ol class="select-bank">
                    <#list banks as bank>
                        <li <#if (bank_index + 1) % 4 == 0>class="new-line"</#if>>
                            <input id="bank-${bank}" data-name="${bank}" type="radio" name="select_bank" <#if bank_index == 0>checked="checked"</#if>>
                            <label for="bank-${bank}"><img  src="${staticServer}/images/bank/${bank}.jpg" alt=""></label>
                        </li>
                    </#list>
                </ol>
                <div class="recharge-form pad-m">
                    <form action="/bind-card" method="post" target="_blank">

                        银行卡： <input name="cardNumber" class="input-bankcard" type="text" placeholder="输入卡号" value=""/>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="tc clear-blank-m">
                            <input type="submit" class="btn-normal bind-card-submit" disabled="disabled"  value="确认绑定"/>
                        </div>
                    </form>
                </div>
            </div>
        </form>
        </#if>
    </div>
        <div class="clear-blank"></div>
        <div class="borderBox">
            <b>温馨提示:</b><br/>
            1、不支持提现至信用卡账户。<br/>
            2、由于银行卡保护机制均由联动优势提供，故您的银行卡将通过拓天平台绑定到联动优势平台上进行第三方托管。<br/>
            3、可开通快捷支付银行卡列表：中国工商银行、中国农业银行、中国建设银行、华夏银行、中国银行、中国邮政储蓄银行、浦发银行、交通银行、民生银行、广发银行、中信银行、兴业银行、光大银行、招商银行和平安银行。<br/>
            4、<span style="color: #FF0000">如您已开通快捷支付</span>，更换绑定银行卡时若账户余额或待回款金额不为0，需联动优势人工审核后方能更换成功。<br/>
            5、更换绑定银行卡时间：自动审核一般30分钟以内更换完成，人工审核一般在2个工作日以内完成。<br/>
        </div>
    </div>
</div>

<div id="pop-bind-card" class="pad-m recharge-plat" style="display: none;">
    <p>请在新打开的联动优势页面绑卡完成后选择：</p>
    <div class="ret">
        <p>绑卡成功：<a href="/account" class="btn-success"  data-category="确认成功" data-label="recharge">确认成功</a></p>
        <p>绑卡失败：<a href="/bind-card" class="btn-normal" data-category="重新绑卡" data-label="recharge">重新绑卡</a>
            <span class="help">查看<a href="/about/qa"  target="_blank" data-category="查看帮助中心" data-label="recharge">帮助中心</a></span>
        </p>
        <p>遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-20:00）</p>
    </div>
</div>

<div id="pop-fast-pay" class="pad-m" style="display: none; margin-top:-10px;">
    <div class="pad-m tc clear-blank-s">
        <a href="/recharge" class="btn-success">继续充值</a>
        <p class="clear-blank-m">遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-20:00）</p>
    </div>
</div>
</@global.main>