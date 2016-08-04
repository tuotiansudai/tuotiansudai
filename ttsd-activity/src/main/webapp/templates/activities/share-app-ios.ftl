<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.share_app}" pageJavascript="${js.share_app}" activeNav="" activeLeftNav="" title="推荐奖励_拓天速贷" keywords="拓天速贷,推荐奖励,P2P理财,短期理财,短期投资,拓天速贷2级推荐机制" description="拓天速贷针对老用户推出2级推荐机制的推荐奖励,可以让您的财富快速升值.">
<div class="share-app-container clearfix">
	<div class="share-container">
		<div class="share-item">
			<div class="item-tel">
                <span>${referrerInfo}</span>
			</div>
			<div class="item-intro">
				<img src="${staticServer}/activity/images/sign/actor/shareapp/intro-text.png" width="100%">
			</div>
			<div class="item-intro">
				体验再得588现金红包＋3%加息券
			</div>
			<div class="item-form">
				<form action="#" method="post" id="iosForm">
					<div class="item-int">
                        <input type="text" class="item-text " name="mobile" placeholder="请输入您的手机号码" id="mobile">
					</div>
					<div class="item-int">
						<input type="text" class="item-text" name="captcha" placeholder="请输入验证码" id="captcha">
						<input type="button" class="ignore get-code" value="获取验证码" id="iosBtn">
					</div>
					<div class="item-int">
						<input type="submit" class="item-submit" value="领取5888元体验金">
					</div>
					<div class="item-int">
	      				<input type="checkbox" class="checkbox" id="agree" name="agree">
						<label for="agree" class="agree">同意拓天速贷<span>《服务协议》</span></label>
					</div>
					<div class="item-int">
						<p class="tc">好友<span>133****2082</span>邀请你来拓天速贷理财</p>
						<p class="tc">新手活动收益高，奖不停，拿红包到手软！</p>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="share-bg">
		<img src="${staticServer}/activity/images/sign/actor/shareapp/share-bg.png" width="100%">
	</div>
	<div class="title-container">
		<span>为什么选择拓天速贷？</span>
	</div>
	<ul class="list-container">
		<li>
			<div class="img-item">
				<img src="${staticServer}/activity/images/sign/actor/shareapp/safe_icon.png" width="100%">
			</div>
			<div class="info-item">
				<p class="title-text">安全</p>
				<p class="intro-text">房、车抵押，公认的安全抵押债权</p>
			</div>
		</li>
		<li>
			<div class="img-item">
				<img src="${staticServer}/activity/images/sign/actor/shareapp/trans_icon.png" width="100%">
			</div>
			<div class="info-item">
				<p class="title-text">透明</p>
				<p class="intro-text">第三方资金托管，资金流向公开透明</p>
			</div>
		</li>
		<li>
			<div class="img-item">
				<img src="${staticServer}/activity/images/sign/actor/shareapp/money_icon.png" width="100%">
			</div>
			<div class="info-item">
				<p class="title-text">收益</p>
				<p class="intro-text">稳健收益，预期年化收益率可达13%</p>
			</div>
		</li>
		<li>
			<div class="img-item">
				<img src="${staticServer}/activity/images/sign/actor/shareapp/use_icon.png" width="100%">
			</div>
			<div class="info-item">
				<p class="title-text">便捷</p>
				<p class="intro-text">手机APP随时随地放心理财</p>
			</div>
		</li>
	</ul>
</div>
</@global.main>