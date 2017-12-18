

<#--<@global.main pageCss="${css.buy_transfer}" pageJavascript="${js.buy_transfer}" title="转让项目购买详情">-->

<div class="my-account-content apply-transfer buy-transfer"  id="applyTransfer" style="display: none">

    <div class="benefit-box">
        <div class="target-category-box" data-url="loan-transfer-detail.ftl">
            <div class="newer-title">
                <span>${transferApplication.name!}</span>
            </div>
            <ul class="benefit-list">
                <li>预期收益: <span>${transferApplication.expecedInterest!}</span>元</li>
                <li>账户余额: <span>${transferApplication.balance!}</span>元  </li>
            </ul>
        </div>
        <div class="bg-square-box"></div>
    </div>
    <form id="investForm">
    <div class="input-amount-box">
        <ul class="input-list">
            <li>
                <label>转让价格</label>
                <input type="text" value="${transferApplication.transferAmount}" name="price" class="input-amount" placeholder="请输入金额">
                <em>元</em>
            </li>
        </ul>
    </div>

    <button type="submit" class="btn-wap-normal" disabled>立即投资</button>

    </form>


    <div class="transfer-notice">
        <div class="agreement-box">
            <span class="init-checkbox-style on">
                 <input type="checkbox" id="readOk" class="default-checkbox" checked>
             </span>
            <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-service">《安心签服务协议》</a>、<a href="javascript:void(0)" class="link-agree-privacy">《隐私条款》</a>、<a href="javascript:void(0)" class="link-agree-number">《CFCA数字证书服务协议》</a>、<a href="javascript:void(0)" class="link-agree-number-authorize">《CFCA数字证书授权协议》</a> 和<a href="javascript:void(0)" class="link-agree-number"> 《债权转让协议》</a></lable>
        </div>
    </div>


</div>

<#--</@global.main>-->
