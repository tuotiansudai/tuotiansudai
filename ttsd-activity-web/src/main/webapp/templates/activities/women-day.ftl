<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.women_day}" pageJavascript="${js.women_day}" activeNav="" activeLeftNav="" title="女神节活动_活动中心_拓天速贷" keywords="拓天速贷,妇女节活动,女神节,少女节" description="拓天速贷3月8日推出女神节活动,少女总动员,开启全民抓娃娃,参与幸运女神时光机,集花瓣赢礼盒等投资活动获得相应蜜汁礼盒.">
<div class="women-day-container" id="womenDayContainer">
	<div class="top-item">
		<img src="${staticServer}/activity/images/women-day/top-img.png" width="100%" class="media-pc">
		<img src="${staticServer}/activity/images/women-day/top-img-phone.png" width="100%" class="media-phone">
	</div>
	<div class="wp clearfix">
        <div class="reg-tag-current" style="display: none">
			<#include '../module/register.ftl' />
        </div>
		<div class="content-item">
			<div class="title-item">
				<img src="${staticServer}/activity/images/women-day/title-one.png">
			</div>
			<div class="tip-item tc">
				<div class="tip-text-item top-tip">
					活动期间，每日在本活动页面<span>签到</span>即可获取一次“抓娃娃”的机会。
				</div>
			</div>
			<div class="machine-item tc">
				<div class="machine-lottery">
					<div class="sign-btn">
						<#if signedIn?? && signedIn>
                            <span class="sign-already"></span>
						<#else>
                            <span></span>
						</#if>
					</div>
					<div class="lottery-group" id="lotteryList">
	                    <h3 class="menu-switch">
	                        <span class="active">中奖纪录</span>
	                        <span>我的奖品</span>
	                    </h3>
	                    <div class="record-group record-list">
				            <ul class="record-item user-record" id="recordList"></ul>
				            <script type="text/html" id="recordListTpl">
				            {{if recordlist.length>0}}
				            {{each recordlist}}
				            	<li><span>恭喜<strong>{{$value.mobile}}</strong>获得<strong>{{$value.prizeValue}}</strong></span></li>
				            {{/each}}
				            {{else}}
				            	<li class="record-img">
				            		<p>还没有奖品呢，快去签到吧！</p>
				            	</li>
				            {{/if}}
				            </script>
				            <ul class="record-item own-record" id="myRecord" style="display: none"></ul>
				            <script type="text/html" id="myRecordTpl">
				            {{if myrecord.length>0}}
				            	{{each myrecord}}
					            	<li><span class="time-text">{{$value.lotteryTime}}</span><span class="text-name">获<strong>{{$value.prizeValue}}</strong>元红包</span></li>
					            {{/each}}
					        {{else}}
					        	<li class="record-img">
				            		<p>还没有奖品呢，快去签到吧！</p>
				            	</li>
					        {{/if}}
				            </script>
				        </div>
	                </div>
				</div>
			</div>
		</div>
		<div class="content-item">
			<div class="title-item">
				<img src="${staticServer}/activity/images/women-day/title-two.png">
			</div>
			<div class="tip-item">
				<div class="tip-text-item">
					活动期间每个标的将送出<span>6</span>个“撒娇礼”，分别由参与该标的的第<span>1</span>、<span>3</span>、<span>8</span>、<span>38</span>、<span>138</span>及<span>最后一笔</span>投资对应的投资人获得（每个投资人可获得多个奖励；1人投满标的的投资人将获得全部6个奖励）。
				</div>
			</div>
			<ul class="loan-gift">
				<li>
					<p><img src="${staticServer}/activity/images/women-day/loan-one.png"></p>
					<p>8元红包<br/>第1笔投资者奖励</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/loan-two.png"></p>
					<p>38元红包<br/>第3笔投资者奖励</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/loan-three.png"></p>
					<p>0.8%加息券<br/>第8笔投资者奖励</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/loan-four.png"></p>
					<p>金属书签礼盒<br/>第38笔投资者奖励</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/loan-five.png"></p>
					<p>萌兔子保温杯<br/>第138笔投资者奖励</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/loan-six.png"></p>
					<p>一年之茶礼盒<br/>最后一笔投资者奖励</p>
				</li>
			</ul>
			<ul class="table-item">
				<li>
					<span class="text-item">第1笔投资者奖励</span>
					<span class="info-item">8元红包</span>
				</li>
				<li>
					<span class="text-item">第3笔投资者奖励</span>
					<span class="info-item">38元红包</span>
				</li>
				<li>
					<span class="text-item">第8笔投资者奖励</span>
					<span class="info-item">0.8%加息券</span>
				</li>
				<li>
					<span class="text-item">第38笔投资者奖励</span>
					<span class="info-item">金属书签礼盒</span>
				</li>
				<li>
					<span class="text-item">第138笔投资者奖励</span>
					<span class="info-item">萌兔子保温杯</span>
				</li>
				<li>
					<span class="text-item">最后一笔投资者奖励</span>
					<span class="info-item">一年之茶礼盒</span>
				</li>
			</ul>
		</div>
		<div class="content-item">
			<div class="title-item">
				<img src="${staticServer}/activity/images/women-day/title-three.png">
			</div>
			<div class="tip-item">
				<div class="tip-text-item">
					活动期间用户可以通过<span>签到</span>、<span>投资</span>、<span>邀请好友</span>行为获得花瓣，活动结束时根据累计获得的花瓣数量可获得相应的蜜汁礼盒。
				</div>
			</div>
			<dl class="flower-rule">
				<dt class="tc">获取花瓣规则</dt>
				<dd>1.活动期间每日签到可以得到1片花瓣；</dd>
				<dd>2.活动期间累计投资每满1000元可得到1片花瓣；</dd>
				<dd>3.活动期间每邀请1个好友注册并投资50元以上，可获5片花瓣（用户活动期间靠推荐好友最多可获50片花瓣，如推荐10名以上好友注册投资，则不再累计花瓣奖励）；</dd>
				<dd>4.活动结束后计算累计花瓣数量，可获得相应的蜜汁礼盒（礼盒不可累计获得）。</dd>
			</dl>
			<div class="flower-info">
				<p class="title-text tc">我的花瓣</p>
				<@global.isAnonymous>
				<p class="tc">请登录查看所获得花瓣</p>
				<p class="tc">
					<a href="javascript:void(0)" class="show-login">登录查看</a>
				</p>
				</@global.isAnonymous>
				<@global.isNotAnonymous>
				<p class="tc">当前累计获得花瓣<span>${totalLeaves}</span>个</p>
				<p class="tc">可获得<span>${prize}</span></p>
				<p class="tc">
					<a href="/referrer/refer-list">邀请好友</a>
					<a href="/loan-list">立即投资</a>
				</p>
				</@global.isNotAnonymous>
			</div>
			<ul class="flower-gift">
				<li>
					<p><img src="${staticServer}/activity/images/women-day/flower-one.png"></p>
					<p>
						<span>0.5%加息券+30元红包</span>
						<span class="gift-info">9≤累计花瓣数量＜38</span>
					</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/flower-two.png"></p>
					<p>
						<span>20元话费+0.5%加息券+60元红包</span>
						<span class="gift-info">38≤累计花瓣数量＜78</span>
					</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/flower-three.png"></p>
					<p>
						<span>100元京东E卡+0.5%加息券+160元红包</span>
						<span class="gift-info">78≤累计花瓣数量＜138</span>
					</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/flower-four.png"></p>
					<p>
						<span>SNP动物面膜+100元京东E卡+0.5%加息券+300元红包</span>
						<span class="gift-info">138≤累计花瓣数量＜238</span>
					</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/flower-five.png"></p>
					<p>
						<span>阿玛尼小胖丁染唇液+100元京东E卡+0.5%加息券+500元红包</span>
						<span class="gift-info">238≤累计花瓣数量＜338</span>
					</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/flower-six.png"></p>
					<p>
						<span>SK-II神仙水+100元京东E卡+0.5%加息券+600元红包</span>
						<span class="gift-info">338≤累计花瓣数量＜438</span>
					</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/flower-seven.png"></p>
					<p>
						<span>迪奥真我香水+0.8%加息券+600元红包</span>
						<span class="gift-info">438≤累计花瓣数量＜638</span>
					</p>
				</li>
				<li>
					<p><img src="${staticServer}/activity/images/women-day/flower-eight.png"></p>
					<p>
						<span>雅诗兰黛套装礼盒+0.8%加息券+600元红包</span>
						<span class="gift-info">累计花瓣数量≥638</span>
					</p>
				</li>
			</ul>
			<ul class="table-item flower-table">
				<li>
					<span class="text-item">礼盒一 9≤累计花瓣数量＜38</span>
					<span class="info-item">0.5%加息券+30元红包</span>
				</li>
				<li>
					<span class="text-item">礼盒二 38≤累计花瓣数量＜78</span>
					<span class="info-item">20元话费+0.5%加息券+60元红包</span>
				</li>
				<li>
					<span class="text-item">礼盒三 78≤累计花瓣数量＜138</span>
					<span class="info-item">100元京东E卡+0.5%加息券+160元红包</span>
				</li>
				<li>
					<span class="text-item">礼盒四 138≤累计花瓣数量＜238</span>
					<span class="info-item">SNP动物面膜+100元京东E卡+0.5%加息券+300元红包</span>
				</li>
				<li>
					<span class="text-item">礼盒五 238≤累计花瓣数量＜338</span>
					<span class="info-item">阿玛尼小胖丁染唇液+100元京东E卡+0.5%加息券+500元红包</span>
				</li>
				<li>
					<span class="text-item">礼盒六 338≤累计花瓣数量＜438</span>
					<span class="info-item">SK-II神仙水+100元京东E卡+0.5%加息券+600元红包</span>
				</li>
				<li>
					<span class="text-item">礼盒七 438≤累计花瓣数量＜638</span>
					<span class="info-item">迪奥真我香水+0.8%加息券+600元红包</span>
				</li>
				<li>
					<span class="text-item">礼盒八 累计花瓣数量≥638</span>
					<span class="info-item">雅诗兰黛套装礼盒+0.8%加息券+600元红包</span>
				</li>
			</ul>
		</div>
		<dl class="rule-item">
			<dt>温馨提示：</dt>
			<dd>1. 本活动仅限90天、180天、360天抵押类产品，债权转让及新手专享项目不参与活动；</dd>
			<dd>2. 活动一中优惠券将即时发放，活动二及活动三优惠券将于活动结束后3个工作日内统一发放，用户可在“我的账户-我的宝藏”中查看；</dd>
			<dd>3. 实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</dd>
			<dd>4. 活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
			<dd>5. 活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</dd>
		</dl>
	</div>
    <a href="javascript:void(0)" class="show-login no-login-text"></a>
    <div class="layer-tip-item" id="tipItem"></div>
    <script type="text/html" id="tipItemTpl">
    	{{if returnCode==0}}
    		<p class="gift-text">恭喜您抓取到
    		{{if prizeValue=='5.21元红包'}}
				小鲜肉
    		{{else if prizeValue=='5.80元红包'}}
    			男闺蜜
    		{{else if prizeValue=='8.80元红包'}}
    			老腊肉
    		{{else if prizeValue=='10元红包'}}
    			萌大叔
    		{{else if prizeValue=='0.2%加息券'}}
    			怪蜀黍
    		{{else if prizeValue=='38元红包'}}
    			肌肉猛男
    		{{else if prizeValue=='138元红包'}}
    			霸道总裁
    		{{else if prizeValue=='0.5%加息券'}}
    			长腿偶吧
    		{{/if}}
    		</p>
    		<p class="gift-info">送您<span>{{prizeValue}}</span>已发放至您的账户</p>
    	{{else if returnCode==1}}
    	<p class="gift-info tip-text">您暂无抽奖机会啦～</p>
    	{{else if returnCode==3}}
    	<p class="gift-info tip-text">不在活动时间范围内！</p>
    	{{else if returnCode==4}}
    	<p class="gift-info tip-text">请实名认证后再来抽奖吧！</p>
    	{{/if}}
    	<p class="gift-btn">
    		<a href="javascript:void(0)" class="close-btn">确定</a>
    	</p>
    </script>
    <div class="loading-item" id="loadingItem">
    	娃娃抓取中，请稍后... ...
    </div>
</div>
<#include "login-tip.ftl" />
</@global.main>