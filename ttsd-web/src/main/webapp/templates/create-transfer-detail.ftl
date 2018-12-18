<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer_apply_detail}" pageJavascript="${js.transfer_apply_detail}" activeNav="我的账户" activeLeftNav="债权转让" title="投资记录">
<div class="content-container">
    <h4 class="column-title">
        <a href="/investor/invest-list"><em class="tc">债权转让</em></a>
    </h4>
	<div class="create-transfer-container">
		<h5>可转让债权>><span>申请转让</span></h5>
		<form action="#" method="post" id="createForm">
			<ul class="info-container">
				<li class="info-list">
					<div class="info-left">
						转让价格：
					</div>
					<div class="info-right">
						<#--<input type="text" class="int-number" id="transferAmount" name="price" value="${formData.investAmount}">-->
						<span class="price_transfer" id="transferAmount">${formData.transferAmount}</span>元
						<input type="hidden" id="transferInvestId" value="${formData.transferInvestId?string.computer}">
					</div>
				</li>
				<li class="info-list">
					<div class="info-left">
						项目本金：
					</div>
					<div class="info-right">
						${formData.investAmount}元
					</div>
				</li>
				<li class="info-list">
					<div class="info-left">
						转让手续费：
					</div>
					<div class="info-right">
						<span>${formData.transferFee}元</span>
						<i class="fa fa-question-circle" aria-hidden="true" title="您持有债权${formData.holdDays}天，需支付本金${(formData.transferFeeRate * 100)?string('0.0')}%的手续费。"></i>
					</div>
				</li>
				<li class="info-list">
					<div class="info-left">
						转让截止时间：
					</div>
					<div class="info-right">
						${formData.expiredDate?string('yyyy-MM-dd')!} 0点
					</div>
				</li>
				<li class="info-list ">
					<em class="agreement checked">
					<i class="fa fa-check-square" aria-hidden="true"></i>
					<input type="hidden" id="skipCheck" value="true">
					<input type="hidden" value="${formData.anxinAuthenticationRequired?c}" id="isAnxinAuthenticationRequired">
					<input type="hidden" value="${formData.anxinUser?c}" id="isAnxinUser">
					<span>我已阅读并同意<strong><a href="${commonStaticServer}/images/pdf/transferAgreement-sample.pdf" target="view">债权转让协议书（范本）</a>

					<@global.role hasRole="'INVESTOR'">
                    <#if formData.anxinUser != true>
					、<a href="javascript:void(0)" class="anxin_layer link-agree-service">《安心签平台服务协议》</a>、<a href="javascript:void(0)" class="anxin_layer link-agree-privacy">《隐私条款》</a>、<a href="javascript:void(0)" class="anxin_layer link-agree-number">《CFCA数字证书服务协议》</a><br />和<a href="javascript:void(0)" class="anxin_layer link-agree-number-authorize">《CFCA数字证书授权协议》</a>
					</#if>
                    </@global.role>
					</strong></span>
                    </em>
					<span class="error agree-tip" style="display: none;">请勾选债权转让协议</span>
				</li>
				<li class="info-list tc">
					<button class="btn btn-normal" type="submit">确定</button>
					<button class="btn btn-default" type="button" id="cancleBtn">取消</button>
				</li>
			</ul>
		</form>
	</div>
</div>
<div class="success-tip" id="successTip">
	<i class="fa fa-check-circle" aria-hidden="true"></i>
	<p>申请转让成功！<br /><span class="count-time">3</span>秒后自动跳转至“转让中债权”</p>
</div>
<#include "component/anxin-qian.ftl" />
<#include "component/anxin-agreement.ftl" />
</@global.main>