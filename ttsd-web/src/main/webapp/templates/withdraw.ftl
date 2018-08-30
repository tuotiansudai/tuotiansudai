<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.withdraw}" pageJavascript="${js.withdraw}" activeNav="我的账户" activeLeftNav="资金管理" title="提现">
<div class="content-container auto-height">
    <h4 class="column-title"><em class="tc">我要提现</em></h4>

    <div class="pad-s">
        <div class="borderBox withdraw">
            <span style="padding-left: 15px;">可提现额度：<i class="color-note">${balance}</i>元</span><br/>
            <span class="pad-l-15">提现金额：</span>
            <input type="text" class="amount-display" data-l-zero="deny" data-v-min="0.00" data-v-max="${balance}" placeholder="0.00" style="margin-left: 10px;"> 元
            <span class="error" style="display: none;"><i class="fa fa-times-circle"></i> 金额必须大于<em id="cash">1.50</em>元</span>

            <div class="calculate">
                提现费用：<em class="withdraw-fee" data-fudianbank="${isFudianBank?c}" ></em> 元（每笔） <br/>
                实际到账：<em class="actual-amount">0.00</em> 元
            </div>
            <button class="withdraw-submit btn-normal" type="button" disabled="disabled">确认提现</button>
            <div class="clear-blank"></div>
            <form action="/withdraw" method="post" id="withdraw">
                <input name="amount" type="hidden" value=""/>
                <input name="source" type="hidden" value="WEB"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
        <div class="clear-blank"></div>
        <div class="borderBox">
            <b>温馨提示:</b><br/>
            1、5W及以下，任何时间都可以申请提现，实时到账；<br/>
            2、5W以上：工作日8:30-17:00，实时到账；其他时间不允许提现；<br/>
            3、当日充值的金额只能下一个工作日12:00之后才能提现；<br/>
            4、提现手续费收取标准：5万元（含5万）以下1.5元／笔，5万以上5元／笔。
        </div>
    </div>

</div>
</@global.main>