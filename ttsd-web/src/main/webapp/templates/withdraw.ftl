<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.withdraw}" activeNav="我的账户" activeLeftNav="资金管理" title="提现">
<div class="content-container auto-height">
    <h4 class="column-title"><em class="tc">我要提现</em></h4>
    <div class="pad-s">
        <div class="borderBox withdraw">
            可提现额度：<i class="color-note">${balance}</i>元<br/>
            <span class="pad-l-15">提现金额：</span><input type="text" class="amount-display" data-l-zero="deny" data-v-min="0.00" data-v-max="${balance}" placeholder="0.00"> 元
                <span class="error" style="display: none;"><i class="fa fa-times-circle"></i> 金额必须大于2.00元</span>

            <div class="calculate">
                提现费用：<em class="withdraw-fee">${withdrawFee}</em> 元（每笔） <br/>
                实际到账：<em class="actual-amount">0.00</em> 元
            </div>
            <button class="withdraw-submit btn-normal" type="button" disabled="disabled">确认提现</button>
            <div class="clear-blank"></div>
            <form action="/withdraw" method="post" target="_blank">
                <input name="amount" type="hidden" value=""/>
                <input name="source" type="hidden" value="WEB"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
        <div class="clear-blank"></div>
        <div class="borderBox">
            <b>温馨提示:</b><br/>
            1、每笔提现，手续费${withdrawFee}元。<br/>
            2、当日16:00点前提现预计当日到款，16:00后提现预计次日到款（如遇双休日或法定节假日顺延）。<br/>
            3、提现银行卡姓名应与实名认证身份一致，才可提现。<br/>
        </div>
    </div>

</div>

<div id="popWithdraw" class="pad-m recharge-plat" style="display: none;">
    <p>请在新打开的联动优势页面提现完成后选择：</p>
    <div class="ret">
        <p>充值成功：<a href="/account" class="btn-success"  data-category="确认成功" data-label="withdraw">确认成功</a></p>
        <p>充值失败：<a href="/withdraw" class="btn-normal" data-category="重新提现" data-label="withdraw">重新提现</a>
            <span class="help">查看<a href="/about/qa"  target="_blank" data-category="查看帮助中心" data-label="recharge">帮助中心</a></span>
        </p>
        <span>遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-20:00）</span>
    </div>
</div>
</@global.main>