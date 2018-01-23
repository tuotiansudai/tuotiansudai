<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'exchange_coupon' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.exchange_coupon}" pageJavascript="${js.exchange_coupon}" title="兑换优惠券">


<div class="my-account-content amount-overview" id="couponList">
	<div class="tip-item">
		<p>
			恭喜您：
		</p>
		<p>兑换码<span>WERTYUIOON94TI</span>兑换成功</p>
	</div>
    <ul class="coupon-list-container">
        <li class="coupon-item">
            <div class="left-item">
                <dl>
                    <dt>现金红包</dt>
                    <dd>单笔满500.00元可用</dd>
                    <dd>可用于60天以上标的</dd>
                    <dd>请在2016-12-22日之前使用</dd>
                    <dd class="coupon-line">拓天速贷平台赠送</dd>
                </dl>
                <ul class="circle-line">
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                </ul>
            </div>
            <div class="right-item">
                <p>
                    <span class="price-text">28</span><span>元</span>
                </p>
                <p>
                    <a href="">立即使用</a>
                </p>
            </div>
        </li>
    </ul>
    <div class="button-note">
	    <a href="#" class="btn-wap-normal next-step" >确定</a>
	</div>
</div>
</@global.main>
