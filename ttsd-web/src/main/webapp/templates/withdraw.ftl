<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="${js.withdraw}" activeNav="我的账户" activeLeftNav="资金管理" title="提现">
<div class="content-container auto-height">
    <h4 class="column-title"><em class="tc">我要提现</em></h4>
    <div class="withdraw">
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
            <input name="amount" type="hidden" value=""/>
            <input name="source" type="hidden" value="WEB"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>

    <div class="tips">
        <b>温馨提示:</b><br/>
        1、每笔提现，手续费3.00元。<br/>
        2、提现操作，T+1日内完成，均受节假日影响。<br/>
        3、提现银行卡姓名应与实名认证身份一致，才可提现。<br/>
    </div>
</div>

<div id="popWithdraw" class="pad-m recharge-plat" style="display: none;">
    <p>请在新打开的联动优势页面提现完成后选择：</p>

    <div class="ret">
        <p>充值成功：<a href="/account" class="btn-success"  data-category="确认成功" data-label="withdraw">确认成功</a></p>
        <p>充值失败：<a href="" class="btn-normal" data-category="重新提现" data-label="withdraw">重新提现</a>
            <span class="help">查看<a href="#"  target="_blank" data-category="查看帮助中心" data-label="recharge">帮助中心</a></span>
        </p>
        <p>遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-22:00）</p>
    </div>
</div>
</@global.main>