<#import "../macro/global.ftl" as global>

<@global.main pageCss="${css.single_rank}" pageJavascript="${js.single_rank}" activeNav="" activeLeftNav="" title="'单笔'狂欢场_拓天周年庆_活动中心_拓天速贷" keywords="拓天速贷,拓天周年庆,抽奖,体验金,红包奖励" description="拓天周年庆-'单笔'狂欢场活动,活动期间每单笔投资满1万元以上,即可获得体验金奖励及一次抽奖机会,最高可获68888元体验金奖励及投资红包,100%中奖,实物大奖送不停.">
<div class="single-rank-container" id="singleRank">
	<div class="top-item" id="topImg">
		<img src="" width="100%" class="media-pc">
		<img src="" width="100%" class="media-phone">
	</div>
	<div class="wp clearfix">
		<div class="reg-tag-current" style="display: none">
			<#include '../module/fast-register.ftl' />
        </div>
		<div class="content-item">
			<i class="icon-hot"></i>
			<h3>
				<span><i class="icon-title"></i>单笔聚划算，奖励更加码</span>
			</h3>
			<div class="tip-item media-pc">
				活动期间单笔投资满1万元以上，即可获得体验金奖励，最高可获<span>68888</span>元体验金奖励！
			</div>
			<div class="tip-item media-phone">
				<p>活动期间单笔投资满1万元以上，</p>
				<p>即可获得体验金奖励，</p>
				<p>最高可获<span>68888</span>元体验金奖励！</p>
			</div>
			<div class="intro-item tc" id="introImg">
				<img src="" width="90%" class="media-pc">
				<img src="" width="90%" class="media-phone">
			</div>
		</div>
		<div class="content-item">
			<h3>
				<span><i class="icon-title"></i>幸运大抽奖，100%中奖</span>
			</h3>
			<div class="tip-item">
				活动期间，单笔投资满1万元可以获得一次抽奖机会，如单笔投资5万元，则可获得5次抽奖机会，以此类推。
			</div>
			<div class="gift-circle-frame clearfix">
					    <div class="gift-circle-out">
					            <div class="pointer-img"></div>
					            <div class="rotate-btn"></div>
					    </div>
					    <div class="gift-circle-detail">
						    <div class="lottery-times">
						    	我的抽奖机会：<span id="leftDrawCount">${drawCount}</span>次
						    </div>
					        <div class="gift-info-box">
					            <ul class="gift-record">
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
		<dl class="rule-item">
			<dt>温馨提示：</dt>
			<dd>1.本活动仅限直投项目，债权转让及新手专享项目不参与累计；</dd>
			<dd>2.“单笔聚划算，奖励更加码”活动中所获体验金将于用户投资成功后即时发放；</dd>
			<dd>3.抽奖活动中所获的红包、加息券、体验金奖励将即时发放，用户可在PC端“我的账户”或App端“我的”中进行查看；</dd>
			<dd>4.实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</dd>
			<dd>5.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
			<dd>6.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有；</dd>
            <dd>7.投资有风险，投资需谨慎。</dd>
		</dl>
	</div>
	<#include "../module/login-tip.ftl" />
    <div class="tip-list-frame">
    	<!-- 真实奖品的提示 -->
        <div class="tip-list" data-return="concrete">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text"></p>
                <p class="reward-text"><em class="prizeValue"></em>！</p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">确定</a></div>
        </div>

    	<!--虚拟奖品的提示-->
        <div class="tip-list" data-return="virtual">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text"></p>
                <p class="reward-text"><em class="prizeValue"></em></p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">确定</a></div>
        </div>

    	<!--没有抽奖机会-->
        <div class="tip-list" data-return="nochance">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您暂无抽奖机会啦～</p>
                <p class="des-text">快去投资赢取抽奖机会吧</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close canble-btn">取消</a><a href="/loan-list" class="go-close">去投资</a></div>
        </div>

    	<!--不在活动时间范围内-->
        <div class="tip-list" data-return="expired">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">不在活动时间内~</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">确定</a></div>
        </div>

    	<!--实名认证-->
        <div class="tip-list" data-return="authentication">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您还未实名认证~</p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">确定</a></div>
        </div>
    </div>
</div>
</@global.main>