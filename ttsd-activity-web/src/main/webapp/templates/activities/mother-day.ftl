<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.mother_day}" pageJavascript="${js.mother_day}" activeNav="" activeLeftNav="" title="母亲节活动_活动中心_拓天速贷" keywords="拓天速贷,母亲节活动,转盘抽奖,体验金,实物大奖" description="拓天速贷感恩母亲节,活动期间可转动转盘参加抽奖,投资可享188888元体验金福利和128G中国红iphone7等实物大奖.">
<div class="mother-day-container" id="motherDayContainer">
	<div class="top-item compliance-banner">
		<img src="" class="media-pc" id="topImg" width="100%">
		<img src="" class="media-phone" id="topImgPhone" width="100%">
        <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>
	</div>
	<div class="wp clearfix">
        <div class="reg-tag-current" style="display: none">
			<#include '../module/register.ftl' />
        </div>
		<div class="content-item">
			<div class="title-item title-one">
			</div>
			<div class="tip-item tc">
				活动期间可转动转盘参加抽奖，每人每日仅限1次，如当日未参加抽奖，则机会不予累计。
			</div>
			<div class="detail-bg">
				<div class="detail-item gift-item">
					<div class="gift-circle-frame clearfix">
					    <div class="gift-circle-out">
				            <div class="pointer-img"></div>
				            <div class="rotate-btn"></div>
					    </div>
					    <div class="gift-circle-detail">
					        <div class="gift-info-box">
					            <ul class="gift-record clearfix">
					                <li class="active"><span>中奖记录</span></li>
					                <li><span>我的奖品</span></li>
					            </ul>
					            <div class="record-list">
					                <ul class="record-model user-record" ></ul>
					                <ul class="record-model own-record" style="display: none"></ul>
					            </div>
					        </div>
					    </div>
					</div>
				</div>
			</div>
		</div>
		<div class="content-item">
			<div class="title-item title-two">
			</div>
			<div class="tip-item tc">
				活动期间单笔投资满1万元以上，即可获得体验金奖励，最高可享188888元体验金福利。
			</div>
			<div class="detail-bg">
				<div class="detail-item">
					<table class="list-item">
						<thead>
							<tr>
								<th>单笔投资金额（元）</th>
								<th>赠送体验金（元）</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>1万≤单笔投资额<5万</td>
								<td>6888</td>
							</tr>
							<tr>
								<td>5万≤单笔投资额<10万</td>
								<td>38888</td>
							</tr>
							<tr>
								<td>10万≤单笔投资额<20万</td>
								<td>88888</td>
							</tr>
							<tr>
								<td>20万≤单笔投资额</td>
								<td>188888</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="content-item">
			<div class="title-item title-three">
			</div>
			<div class="tip-item tc">
				活动期间，用户累计投资满5万元以上即可获得实物奖，奖品不可累计获得。
			</div>
			<div class="detail-bg">
				<div class="detail-item">
					<div class="gift-title">
						<span>我的累计投资金额: 
						<#if !isAppSource>
						<@global.isAnonymous>
						<strong id="isLogin">登录后查看</strong>
						</@global.isAnonymous>
						<@global.isNotAnonymous>
						<strong>${investAmount}</strong>元
						</@global.isNotAnonymous>
						<#else>
						<strong>${investAmount}</strong>元
						</#if>
						</span>
					</div>
					<div class="gift-img">
					</div>
					<div class="gift-box">
						<span>拓天速贷定制礼盒（20选1）</span>
					</div>
					<ul class="gift-list">
						<li>
							<p class="img-item"></p>
							<p>累计投资金额110000元</p>
						</li>
						<li>
							<p class="img-item"></p>
							<p>累计投资金额70000元</p>
						</li>
						<li>
							<p class="img-item"></p>
							<p>累计投资金额50000元</p>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<dl class="rule-item">
			<dt>温馨提示：</dt>
			<dd>1.本活动仅限直投项目，债权转让及新手专享项目不参与累计；</dd>
			<dd>2.活动中所有红包、加息券、体验金奖励将即时发放，用户可在PC端“我的账户”或App端“我的”中进行查看；</dd>
			<dd>3.实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</dd>
			<dd>4.“拓天速贷定制礼盒”将以礼品册的形式发放至您的手中，礼品册中所含20种礼品选项，用户可根据喜好选择一种自行兑换；</dd>
			<dd>5.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
			<dd>6.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</dd>
		</dl>
	</div>

	<div class="tip-list-frame">
    	<!-- 真实奖品的提示 -->
        <div class="tip-list" data-return="concrete">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text"></p>
                <p class="reward-text">恭喜您获得<em class="prizeValue"></em>！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">知道了</a></div>
        </div>

    	<!--虚拟奖品的提示-->
        <div class="tip-list" data-return="virtual">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text"></p>
                <p class="reward-text">恭喜您获得<em class="prizeValue"></em></p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">知道了</a></div>
        </div>

    	<!--没有抽奖机会-->
        <div class="tip-list" data-return="nochance">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您暂无抽奖机会啦～</p>
                <p class="des-text">明天再来吧~</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>

    	<!--不在活动时间范围内-->
        <div class="tip-list" data-return="expired">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">不在活动时间内~</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>

    	<!--实名认证-->
        <div class="tip-list" data-return="authentication">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您还未实名认证~</p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>
    </div>


	<div class="gift-list-tip" id="giftList">
		<i class="close-tip"></i>
		<h3>拓天速贷定制礼盒（20选1）</h3>
		<ul class="gift-content">
			<li class="gift-one"></li>
			<li class="gift-two"></li>
			<li class="gift-three"></li>
		</ul>
	</div>
</div>
<#include "../module/login-tip.ftl" />
</@global.main>