<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.share_app}" pageJavascript="${js.share_app}" activeNav="" activeLeftNav="" title="新手福利_拓天新手投资_拓天速贷" keywords="拓天速贷,新手投资,新手加息券,新手红包" description="拓天速贷是中国P2P互联网金融信息服务平台,为广大投资、贷款的用户提供多元化的投资选择和优质的综合理财服务,新手注册可领取5888体验金，体验再得红包大奖和3%的新手加息券.">
<div class="share-app-container clearfix">
	<div class="share-container">
		<div class="share-item">
			<div class="item-tel">
                <span data-referrer="" id="referrer">${referrerInfo!}</span>

			</div>
			<div class="item-intro">
				<img src="${staticServer}/activity/images/sign/actor/shareapp/intro-text.png" width="100%">
			</div>
			<div class="item-intro">
				体验再得588现金红包＋3%加息券
			</div>
			<div class="item-form">
				<form action="#" method="post" id="androidForm">
					<div class="item-int">
						<input type="text" class="item-text" name="mobile" placeholder="请输入您的手机号码" id="mobile">
					</div>
					<div class="item-int">
						<input type="password" class="item-text" name="password" placeholder="请输入您的密码" id="password">
					</div>
					<div class="item-int">
						<input type="text" class="item-text" name="captcha" placeholder="请输入验证码" id="captcha">
						<input type="button" class="ignore get-code" value="获取验证码" id="androidBtn">
					</div>
					<div class="item-int">
						<input type="submit" class="item-submit" value="领取5888元体验金">
					</div>
					<div class="item-int">
	      				<input type="checkbox" class="checkbox" id="agreement" name="agreement" checked>
						<label for="agreement" class="agree">同意拓天速贷<span>《服务协议》</span></label>
					</div>
					<div class="item-int">
						<p class="tc">好友<span>${referrerInfo!}</span>邀请你来拓天速贷投资</p>
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
				<p class="intro-text">手机APP随时随地放心投资</p>
			</div>
		</li>
	</ul>
</div>
</@global.main>