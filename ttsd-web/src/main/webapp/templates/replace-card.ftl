<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.bind_card}" activeNav="我的账户" activeLeftNav="个人资料" title="更换银行卡">
<div class="content-container">
    <h4 class="column-title"><em class="tc">更换银行卡</em></h4>
    <div class="recharge-bind-card pad-s">
        <div class="recharge-wrapper bind-card-frame" id="bindCardBox">
        <form action="" method="post" target="_blank">
            真实姓名：${userName}
            <div class="clear-blank"></div>
            <div class="e-bank-recharge">
                <b class="title">选择银行:</b>
                <ol class="select-bank">
                    <#list banks as bank>
                        <li <#if (bank_index + 1) % 4 == 0>class="new-line"</#if>>
                            <input data-name="${bank}" type="radio" name="select_bank" <#if bank_index == 0>checked="checked"</#if>>
                            <label for="bank-${bank}"><img src="${staticServer}/images/bank/${bank}.jpg" alt=""></label>
                        </li>
                    </#list>
                </ol>
                <div class="recharge-form">
                    <form action="/bind-card/replace" method="post" target="_blank">
                        银行卡： <input name="cardNumber" class="input-bankcard" type="text" placeholder="输入卡号" value=""/>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="tc pad-m">
                            <input type="submit" class="btn-normal replace-card-submit" disabled="disabled"  value="确认更换"/>
                        </div>
                    </form>
                </div>
            </div>
        </form>
    </div>
    <div class="clear-blank"></div>
    <div class="borderBox">
        <b>温馨提示:</b><br/>
        1、不支持提现至信用卡账户。<br/>
        2、由于银行卡保护机制均由联动优势提供，故您的银行卡将通过拓天平台绑定到联动优势平台上进行第三方托管。<br/>
        3、如果您的借记卡是中国工商银行、中国农业银行、中国建设银行、华夏银行、中国银行、中国邮政储蓄银行、浦发银行、交通银行、民生银行、广发银行、中信银行、兴业银行、光大银行、招商银行和平安银行，方可开通快捷支付。<br/>
        4、如果您已经开通快捷支付，系统不再支持您更换银行卡。<br/>
    </div>
</div>
</div>

<div id="pop-replace-card" class="pad-m recharge-plat" style="display: none;">
    <p>请在新打开的联动优势页面换卡完成后选择：</p>
    <div class="ret">
        <p>换卡成功：<a href="/account" class="btn-success"  data-category="确认成功" data-label="recharge">确认成功</a></p>
        <p>换卡失败：<a href="/bind-card/replace" class="btn-normal" data-category="重新换卡" data-label="recharge">重新换卡</a>
            <span class="help">查看<a href="#"  target="_blank" data-category="查看帮助中心" data-label="recharge">帮助中心</a></span>
        </p>
        <p>遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-20:00）</p>
    </div>
</div>
</@global.main>