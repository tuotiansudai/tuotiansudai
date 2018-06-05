<#import "../macro/global.ftl" as global>

<@global.main pageCss="${css.dragon_boat}" pageJavascript="${js.dragon_boat}" activeNav="" activeLeftNav="" title="端午节活动_活动中心_拓天速贷" keywords="拓天速贷,端午节活动,粽子节活动,体验金奖励,加息券奖励" description="拓天速贷粽子节活动,使用微信打卡,邀请好友可获得5000元体验金奖励，投资端午专享标可获得投资额等额体验金奖励和香槟塔中红包,加息券,实物大奖.">
<div class="dragon-boat-container" id="dragonBoatContainer">
	<div class="top-item compliance-banner">
		<img src="" width="100%" class="media-pc">
		<img src="" width="100%" class="media-phone">
        <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>
	</div>
	<div class="wp clearfix">
		<div class="content-item border-no">
			<h3>
				<span>微信打卡、邀好友，拿双份礼</span>
			</h3>
			<div class="des-item">
				扫描下方二维码，关注“拓天速贷服务号”，每日回复“我要打卡”，即可随机获得奖励，每人每日仅限一次。获得奖励后将奖励链接分享给好友，每邀请一个新用户注册领取，即可获得5000元体验金奖励。每人每日最多可邀请5人领取，超出部分的邀请将不再发放体验金奖励。
			</div>
			<div class="code-item">
				<span></span>
			</div>
			<div class="gift-item">
				
			</div>
		</div>
		<div class="content-item color-one">
			<h3>
				<span>甜粽派VS咸粽派，正室大PK</span>
			</h3>
			<div class="compare-item">
				<div class="progress-item">
					<span class="fl left-pro" data-amout='${sweetAmount}'></span>
					<span class="fr right-pro" data-amout='${saltyAmount}'></span>
				</div>
				<div class="loan-item">
					<span class="fl">${(sweetAmount/100)?string('0.00')}元</span>
					<span class="fr">${(saltyAmount/100)?string('0.00')}元</span>
				</div>
				<div class="type-item">
					<div class="fl type-btn type-left" data-group="SWEET">
						<span class="type-name">支持甜粽子</span>
						<span>(<strong class="person-num">${sweetSupportCount}</strong>人)</span>
						<i class="add-icon">+1</i>
					</div>
					<div class="fr type-btn type-right" data-group="SALTY">
						<span class="type-name">支持咸粽子</span>
						<span>(<strong class="person-num">${saltySupportCount}</strong>人)</span>
						<i class="add-icon">+1</i>
					</div>
				</div>
				<div class="info-item">
					<h3>甜粽子&咸粽子到底哪种是正宗？</h3>
					<p>选择您所支持的阵营，并投资“端午专享”债权，活动结束后累计双方投资额，投资额较大的为赢方，较小的为输方；</p>
					<p>赢方所有参与成员可获得与其专享标投资额等额的体验金奖励，输方所有成员可获得其专享标投资额0.5倍的体验金奖励。</p>
				</div>
			</div>
		</div>
		<div class="content-item color-two">
			<h3>
				<span>仲夏啤酒节，梦幻香槟塔</span>
			</h3>
			<div class="loan-money">
				<span>我的累积投资金额：<strong>${(investAmount/100)?string('0.00')}</strong>元</span>
			</div>
			<ul class="actor-intro">
				<li>活动期间累计投资额达到5000元，即可参与香槟塔挑战活动，并获得该层香槟塔中所含的全部奖励；</li>
				<li>不同层香槟塔中的红包和加息券可累计获得，实物奖品不可累计获得；</li>
				<li>活动期间累计投资100万元，可获5层香槟塔中所有奖励。</li>
			</ul>
			<ul class="floor-item tc">
				<li>
					<p class="floor-img floor-five">
						<span class="floor-loan <#if champagnePrizeLevel==5>active</#if>"><strong>60万</strong></span>
					</p>
				</li>
				<li>
					<p class="floor-img floor-four">
						<span class="floor-loan <#if champagnePrizeLevel==4>active</#if>"><strong>30万</strong></span>
					</p>
				</li>
				<li>
					<p class="floor-img floor-three">
						<span class="floor-loan <#if champagnePrizeLevel==3>active</#if>"><strong>12万</strong></span>
					</p>
				</li>
				<li>
					<p class="floor-img floor-two">
						<span class="floor-loan <#if champagnePrizeLevel==2>active</#if>"><strong>6万</strong></span>
					</p>
				</li>
				<li>
					<p class="floor-img floor-one">
						<span class="floor-loan <#if champagnePrizeLevel==1>active</#if>"><strong>5千</strong></span>
					</p>
				</li>
			</ul>
		</div>
		<dl class="rule-item">
			<dt>温馨提示：</dt>
			<dd>1.本活动仅限直投项目，债权转让及新手专享项目不参与活动；</dd>
			<dd>2.兑换码在“我的账户-我的宝藏”中进行兑换；</dd>
			<dd>3.体验金奖励将于活动结束后3个工作日内统一发放，可在“我的账户”中查看；</dd>
			<dd>4.“香槟塔”活动中的红包及加息券奖励即时发放，用户可在PC端“我的账户”或App端“我的”中进行查看；</dd>
			<dd>5.实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</dd>
			<dd>6.“拓天速贷定制礼盒”将以礼品册的形式发放至您的手中，礼品册中所含20种礼品选项，用户可根据喜好选择一种自行兑换；</dd>
			<dd>7.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
			<dd>8.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</dd>
		</dl>
	</div>
</div>
<#include "../module/login-tip.ftl" />
</@global.main>