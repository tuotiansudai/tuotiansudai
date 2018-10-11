<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.sign_check}" pageJavascript="${js.sign_check}" activeNav="" activeLeftNav="" title="会员签到_积分商城_拓天速贷" keywords="签到赢积分,签到领红包,红包大奖,拓天速贷" description="拓天速贷会员每日签到赢积分,连续签到积分阶梯递增,累计签到领红包,连续签到365天,可额外获得365元出借红包大奖.">
<div class="sign-check-container">
	<div class="top-item compliance-banner">
        <div class="invest-tip tip-width">市场有风险，出借需谨慎！</div>
	</div>
	<div class="wp clearfix">
		<div class="content-item">
			<div class="title-item title-one">
			</div>
			<div class="intro-text">
				<p>每日签到可获取积分奖励，连续签到积分阶梯递增，最高每日30积分。如签到间断则重新从每日10积分计起。</p>
			</div>
			<div class="left-item">
				<div class="title-name">
					<span>连续签到天数</span><span>每日获得积分</span>
					<div class="icon-item left-icon">
						<span class="circle-icon top-icon"></span>
						<span class="line-icon"></span>
						<span class="circle-icon bottom-icon"></span>
					</div>
					<div class="icon-item right-icon">
						<span class="circle-icon top-icon"></span>
						<span class="line-icon"></span>
						<span class="circle-icon bottom-icon"></span>
					</div>
				</div>
				<div class="title-content">
					<table>
						<tbody>
							<tr>
								<td><span>1~3天</span></td>
								<td><span>10积分</span></td>
							</tr>
							<tr>
								<td><span>4~6天</span></td>
								<td><span>15积分</span></td>
							</tr>
							<tr>
								<td><span>7~10天</span></td>
								<td><span>20积分</span></td>
							</tr>
							<tr>
								<td><span>11~15天</span></td>
								<td><span>25积分</span></td>
							</tr>
							<tr>
								<td><span class="last-child">15天以上</span></td>
								<td><span class="last-child">30积分</span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="right-item">
				<dl class="intro-item clearfix">
					<dt>举个栗子</dt>
					<dd><span class="name-text">连续签到30天</span><span class="point-text">730积分</span></dd>
					<dd><span class="name-text">连续签到60天</span><span class="point-text">1630积分</span></dd>
					<dd><span class="name-text">连续签到一年</span><span class="point-text">10780积分</span></dd>
				</dl>
				<div class="app-item">
					<i class="app-code"></i>
					<p>下载APP，坚持打卡更简单</p>
				</div>
			</div>
			<div class="tip-item">
				连续签到还能领红包哦！
			</div>
			<div class="btn-item">
				<#if !isAppSource>
					<a href="/point-shop">去签到</a>
				<#else>
                    <a href="app/tuotian/point-home">去签到</a>
				</#if>
			</div>
		</div>
		<div class="content-item">
			<div class="title-item title-two">
			</div>
			<div class="intro-text">
				用户累计签到一定天数，即可领取惊喜出借红包。
			</div>
			<div class="left-item center-item clearfix">
				<div class="title-name">
					<span>累计签到天数</span><span>红包金额</span>
					<div class="icon-item left-icon">
						<span class="circle-icon top-icon"></span>
						<span class="line-icon"></span>
						<span class="circle-icon bottom-icon"></span>
					</div>
					<div class="icon-item right-icon">
						<span class="circle-icon top-icon"></span>
						<span class="line-icon"></span>
						<span class="circle-icon bottom-icon"></span>
					</div>
				</div>
				<div class="title-content">
					<table>
						<tbody>
							<tr>
								<td><span>8天</span></td>
								<td><span>3.8元</span></td>
							</tr>
							<tr>
								<td><span>18天</span></td>
								<td><span>10元</span></td>
							</tr>
							<tr>
								<td><span>28天</span></td>
								<td><span>38元</span></td>
							</tr>
							<tr>
								<td><span>38天</span></td>
								<td><span>68元</span></td>
							</tr>
							<tr>
								<td><span class="last-child">58天</span></td>
								<td><span class="last-child">128元</span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="content-item">
			<div class="title-item title-three">
			</div>
			<div class="intro-text">
				连续签到365天，可额外获得365元出借红包。
			</div>
			<div class="gift-item">
			</div>
		</div>
		<dl class="rule-item">
			<dt>温馨提示：</dt>
			<dd>1.用户每日进入活动页面通过点击“签到”按钮完成签到，即可获得积分奖励；</dd>
			<dd>2.签到红包将于连续签到成功后实时发放至用户账户，可在电脑端“我的账户-我的宝藏”或App端“我的-优惠券”中查看，红包的有效期为自获得之日起15天内，请尽快使用；</dd>
			<dd>3.拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
		</dl>
	</div>
</div>
</@global.main>