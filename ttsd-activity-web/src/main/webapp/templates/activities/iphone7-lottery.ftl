<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.iphone7_lottery}" pageJavascript="${js.iphone7_lottery}" activeNav="" activeLeftNav="" title="iphone7活动_活动中心_拓天速贷" keywords="投资活动,APP投资,iphone7,拓天速贷" description="参与拓天速贷'厚惠有7'投资活动的用户即可获得128Giphone7一部，使用APP投资还可获得投资红包大奖">
<div class="activity-container">
	<div class="top-intro-img">
		<img src="${commonStaticServer}/activity/images/iphone7/top-intro.png" alt="英豪榜" width="100%" class="top-img">
		<img src="${commonStaticServer}/activity/images/iphone7/top-intro-phone.png" alt="英豪榜" width="100%" class="top-img-phone">
	</div>
	<div class="actor-content-group">
		<div class="wp clearfix">
            <div class="reg-tag-current" style="display: none">
				<#include '../module/register.ftl' />
            </div>
			<div class="model-group text-c">
				<i class="icon-laba"></i>
				<h3>
				<img src="${commonStaticServer}/activity/images/iphone7/big-event.png" width="90%">
				</h3>
				<div class="content-item">
					<p>
						10月26日-11月4日老板出差,运营汪们私自决定,趁机做一次惊天地泣鬼神的大活动
					</p>
					<p>
						<img src="${commonStaticServer}/activity/images/iphone7/actor-text.png">
					</p>
					<p>
						我们的口号是：宁被老板罚工资，也为用户谋福利！
					</p>
				</div>
			</div>
			<div class="model-group text-c">
				<h3>
				<img src="${commonStaticServer}/activity/images/iphone7/statement-title.png" width="90%">
				</h3>
				<div class="content-item dashed">
					<p>
						<img src="${commonStaticServer}/activity/images/iphone7/title-info.png">
					</p>
					<p>
						活动期间，每次投资都可以获得一个投资码；
					</p>
					<p>
						活动期间，所有用户累计投资每满50万元即抽取一个投资码获得iphone7。
					</p>
				</div>
			</div>
			<div class="actor-gift-group text-c">
				<img src="${commonStaticServer}/activity/images/iphone7/gift-picture.png" width="70%">
			</div>
			<div class="model-group">
				<h3 class="text-c">
				<img src="${commonStaticServer}/activity/images/iphone7/reward-info.png" width="90%">
				</h3>
				<div class="content-item">
					<div class="tip-item" id="moneyTip">
						<p>
							距离下次开奖还差<span>${nextLotteryInvestAmount}</span>元
						</p>
					</div>
					<div class="reward-progress-item progressbar" data-perc="${nextLotteryInvestAmount}">
						<div class="bar color4">
							<span></span>
						</div>
					</div>
					<div class="reward-list-group">
						<dl class="reward-list-item">
							<dt>
							<span>期数</span>
							<span>开奖号码</span>
							<span>获奖用户</span>
							</dt>
							<#if lotteryList??>
								<#list lotteryList as lottery>
                                    <dd>
                                        <span>第${lottery_index + 1}期</span>
                                        <span>${lottery.lotteryNumber}</span>
                                        <span>${lottery.mobile}</span>
                                    </dd>
								</#list>
							</#if>
                            <dd>
								<span>
									<#if (lotteryList?size > 0)> 第${lotteryList?size + 1}期
									<#else>
										第1期
									</#if>
								</span>
                                <span class="enpty-info">等待开奖</span>
                                <span class="enpty-info">等待开奖</span>
                            </dd>
						</dl>
					</div>
				</div>
			</div>
			<div class="model-group">
				<h3 class="text-c">
				<img src="${commonStaticServer}/activity/images/iphone7/my-code.png" width="90%">
				</h3>
				<div class="content-item dashed">
					<@global.isAnonymous>
                        <div class="login-group text-c">
							<a href="javascript:void(0)" class="login-btn show-login">登录查看我的投资码</a>
						</div>
					</@global.isAnonymous>
					<@global.isNotAnonymous>
						<input type="hidden" value="${loginName}" id="loginName">
						<div class="code-list-item" id="codeList">
							
						</div>
                        <script type="text/html" id="codeListTpl">
                        <ul class="my-code-group">
							{{if records.length>0}}
								{{each records}}
                                    <li>
                                        <span>{{$value.lotteryNumber}}</span>
                                        <span>{{$value.status}}</span>
                                    </li>
								{{/each}}
							{{else}}
							<li><span class="no-data">暂无投资码，赶紧去投资吧~</span></li>
							{{/if}}
                        
                        </ul>
                        {{if count>10}}
                        <div class="code-btn-group">
                            <span class="prev-btn code-btn" data-page="{{index-1<=0?1:index-1}}">上一页</span>
                            <span class="next-btn code-btn" data-page="{{index<maxPage?index+1:maxPage}}">下一页</span>
                        </div>
                        {{/if}}
                        </script>
					</@global.isNotAnonymous>

				</div>
			</div>
			<div class="get-loan-group text-c">
				<a href="/loan-list" class="get-loan-btn">抢投资码</a>
			</div>
			<div class="rule-list-group">
				<dl class="rule-item">
					<dt>温馨提示：</dt>
					<dd>1、活动期间用户投资任意直投项目的50元及以上，每次投资都可获得一个投资码参与活动；</dd>
					<dd>2、投资码为系统随机生成的六位数字，用户手持投资码越多，中奖几率越大；</dd>
					<dd>3、活动期间总投资额每满50万元，即诞生1个幸运号码，免费获得iphone7；</dd>
					<dd>4、本活动所有参与用户所获得的50元投资红包，将于活动结束后三个工作日内统一发放，用户可在“我的账户-我的宝藏”中查看；</dd>
					<dd>5、任何通过虚假违规操作，恶意刷量，均视为无效；</dd>
					<dd>6、拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
				</dl>
			</div>
		</div>
	</div>
</div>
<#include "login-tip.ftl" />
</@global.main>