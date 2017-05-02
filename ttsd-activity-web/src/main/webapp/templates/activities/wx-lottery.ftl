<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.wx_lottery}" pageJavascript="${js.wx_lottery}" activeNav="" activeLeftNav="" title="微信活动_活动中心_拓天速贷" keywords="拓天速贷,现金奖励,实物大奖,注册用户,微信活动" description="活动期间新注册用户,及平台所有注册后未投资过的用户,投资直投项目,可获得不同等额的现金奖励和抽奖机会,100%抽取实物大奖.">
<div class="activity-container" id="lanternFrame">
    <div class="top-item"></div>
    <div class="actor-content-group">
        <div class="wp clearfix">
            <div class="wechat-model">
                <div class="info-item">
                    在活动期间，凡新手用户单笔投资每满5000元(不含新手体验和转让项目）即可获得一次抽奖机会，1万可获得两次，以此累加，所有100%中奖！点击转盘开始抽奖哦！
                </div>
                <div class="gift-item text-c">
                    <div class="gift-circle-frame clearfix">
					    <div class="gift-circle-out">
					            <div class="pointer-img"></div>
					            <div class="rotate-btn"></div>
					    </div>
					    <div class="lottery-times">
					    	当前剩余<span id="leftDrawCount">${leftDrawCount}</span>次抽奖机会
					    </div>
					    <a href="/loan-list" class="loan-btn">立即投资赢取更多机会</a>
					    <div class="gift-circle-detail">
					        <div class="gift-info-box">
					            <ul class="gift-record">
					                <li class="active"><span>中奖记录</span></li>
					                <li><span>我的奖品</span></li>
					            </ul>
					            <div class="trim-strip">
						            <i class="icon-square"></i>
						            <i class="icon-square"></i>
						            <i class="icon-square"></i>
						            <i class="icon-square"></i>
						            <i class="icon-vertical-line"></i>
						            <i class="icon-vertical-line"></i>
						        </div>
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
        <dl class="rule-item">
        	<dt>温馨提示：</dt>
        	<dd>1.本活动仅限直投项目，债权转让及新手专享项目不参与累计；</dd>
        	<dd>2.在活动中，抽奖获得的实物奖品，将在活动结束后7个工作内为您寄出；</dd>
        	<dd>3.在活动中，抽奖获得的红包，会实时发放到您的账户中，可登陆后在“我的-优惠券”中进行查看；</dd>
        	<dd>4.如有疑问，欢迎致电拓天速贷客服中心400-169-1188，或微信关注“拓天速贷服务号”进行在线咨询。（工作时间：9:00—20:00）；</dd>
        	<dd>5.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
        	<dd>6.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</dd>
        </dl>
    </div>

    <#include "../module/login-tip.ftl" />
    <div class="tip-list-frame">
    <#--奖品的提示-->
        <div class="tip-list" data-return="concrete">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text">恭喜您抽中了 <span class="prizeValue"></span></p>
                <p class="reward-text"></p>
                <p class="des-text"></p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a></div>
        </div>
    <#--没有抽奖机会-->
        <div class="tip-list" data-return="nochance">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="nochance-img"></p>
                <p class="nochance-text">凡新手用户单笔投资每满<span>5000元</span>(不含新手体验和转让项目）即可获得一次抽奖机会。</p>
            </div>
            <div class="btn-list"><a href="/loan-list">立即投资</a></div>
        </div>

    <#--不在活动时间范围内-->
        <div class="tip-list" data-return="expired">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">不在活动时间内~</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>

    <#--实名认证-->
        <div class="tip-list" data-return="authentication">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您还未实名认证~</p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>
    </div>
</div>
</@global.main>