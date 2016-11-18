<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.no_work}" pageJavascript="${js.no_work}" activeNav="" activeLeftNav="" title="拓天发薪季_活动中心_拓天速贷" keywords="拓天发薪,投资活动,推荐奖励,拓天速贷" description="拓天速贷发薪季给投资用户发薪,发大奖,让投资用户带薪休假,邀请好友投资还可获得上不封顶旅游基金,拓工发薪季,好礼送不停。">
<div class="activity-container">
	<div class="top-intro-img">
		<img src="${staticServer}/activity/images/nowork/top-img.png" alt="" width="100%" class="top-img">
		<img src="${staticServer}/activity/images/nowork/top-img.png" alt="" width="100%" class="top-img-phone">
	</div>
	<div class="actor-content-group">
		<div class="wp clearfix">
			<div class="reg-tag-current" style="display: none">
				<#include '../module/register.ftl' />
			</div>
			<div class="content-skew-item">
				<div class="content-item">
					<h3 class="title-item text-c">
						<img src="${staticServer}/activity/images/nowork/title-one.png" alt="">
					</h3>
					<div class="info-list-item">
						<div class="account-info-item">
							<ul class="user-info">
								<li>
									<p class="title-text">您的累计投资金额：</p>
									<p class="number-text">8,000.00元</p>
								</li>
								<li>
									<p class="title-text">距下一个奖品还差：</p>
									<p class="number-text">12,000.00元</p>
								</li>
								<li>
									<a href="/loan-list" class="loan-btn-item">立即投资</a>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="content-skew-item">
				<div class="content-item">
					<h3 class="title-item text-c">
						<img src="${staticServer}/activity/images/nowork/title-two.png" alt="">
					</h3>
					<div class="actor-group">
						dfsf
					</div>
				</div>
			</div>
			<div class="content-skew-item mt-last">
				<div class="content-item">
					<div class="actor-group">
						<dl class="rule-group">
							<dt>温馨提示：</dt>
							<dd>1、本活动仅适用于带有“加薪专享”标识的债权，其余项目不参与活动；</dd>
							<dd>2、投资红包将于活动结束后三个工作日内发放，用户可在“我的账户-我的宝藏”中查看；</dd>
							<dd>3、实物奖品将于活动结束后10个工作日内由客服联系发放，请保持手机畅通，10个工作日内无法联系的用户，视为自动放弃奖励；</dd>
							<dd>4、为了保证获奖结果的公平性，获奖用户在活动期间所进行的所有投标不允许进行债权转让（活动期外投标不受限制）；</dd>
							<dd>5、拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
							<dd>6、市场有风险，投资需谨慎</dd>
						</dl>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<#include "login-tip.ftl" />
</@global.main>