<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.no_work}" pageJavascript="${js.no_work}" activeNav="" activeLeftNav="" title="拓天发薪季_活动中心_拓天速贷" keywords="拓天发薪,投资活动,推荐奖励,拓天速贷" description="拓天速贷发薪季给投资用户发薪,发大奖,让投资用户带薪休假,邀请好友投资还可获得上不封顶旅游基金,拓工发薪季,好礼送不停。">
<div class="activity-container">
	<div class="top-intro-img">
		<img src="${commonStaticServer}/activity/images/nowork/top-img.png" alt="" width="100%" class="top-img">
		<img src="${commonStaticServer}/activity/images/nowork/top-img-phone.png" alt="" width="100%" class="top-img-phone">
	</div>
	<div class="actor-content-group">
		<div class="wp clearfix">
			<div class="reg-tag-current" style="display: none">
				<#include '../module/register.ftl' />
			</div>
			<div class="content-skew-item">
				<div class="content-item">
					<h3 class="title-item text-c">
						<img src="${commonStaticServer}/activity/images/nowork/title-one.png" alt="">
					</h3>
					<div class="actor-group">
						<div class="info-list-item">
							<div class="account-info-item">
								<ul class="user-info">
									<@global.isNotAnonymous>
									<li>
										<p class="title-text">您的累计投资金额：</p>
                                        <p class="number-text">${investAmount!"0"}元</p>
									</li>
									<li>
										<p class="title-text">距下一个奖品还差：</p>
                                        <p class="number-text">${needInvestAmount!"0"}元</p>
									</li>
									<li>
										<a href="/loan-list" class="loan-btn-item">立即投资</a>
									</li>
									</@global.isNotAnonymous>
									<#if !(investAmount??)>
									<li class="login-item">
										<#if !isAppSource>
                                            <span class="show-login">登录查看我的奖品</span>
										<#else>
                                            <a href="/login">登录查看我的奖品</a>
										</#if>
									</li>
									</#if>
								</ul>
							</div>
						</div>
						<ul class="gift-list-group">
							<li class="text-type">
								<div class="text-content-item">
									<h3>
										<img src="${commonStaticServer}/activity/images/nowork/rule-title.png" alt="">
									</h3>
									<div class="content-text">
										<p>月薪5000元，每天工作8小时相当于日薪227元，时薪28.4元。拿着搬砖的工资，操着包工头的心，够了！怎么，想炒掉老板，要搞事情咯？</p>
										<p>拓天速贷助你财务自由</p>
										<p>你罢工，我发薪！</p>
									</div>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 3000 <= investAmount?number>active</#if>"></div>
										<h3>20元红包</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-one.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:3,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">全民带薪假</p>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 8000 <= investAmount?number>active</#if>"></div>
										<h3>30元话费</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-two.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:8,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">1h堵车假，干掉打卡机！</p>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 30000 <= investAmount?number>active</#if>"></div>
										<h3>京东E卡</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-three.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:30,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">半天赖床假，干掉早会！</p>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 50000 <= investAmount?number>active</#if>"></div>
										<h3>300元旅游基金（芒果卡）</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-four.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:50,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">1天神游假，干掉加班！</p>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 100000 <= investAmount?number>active</#if>"></div>
										<h3>索尼数码相机</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-five.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:100,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">3天姨妈假，干掉催稿！</p>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 200000 <= investAmount?number>active</#if>"></div>
										<h3>联想YOGA 平板3代</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-six.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:200,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">5天撒欢假，干掉鼠标手！</p>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 300000 <= investAmount?number>active</#if>"></div>
										<h3>CAN看尚42英寸液晶电视</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-seven.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:300,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">7天旅行假，干掉雾霾天！</p>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 520000 <= investAmount?number>active</#if>"></div>
										<h3>锤子手机M1</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-eight.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:520,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">半月失联假，干掉微信群！</p>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 800000 <= investAmount?number>active</#if>"></div>
										<h3>浪琴手表康卡斯系列</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-nine.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:800,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">30天环球假，干掉夺命call！</p>
								</div>
							</li>
							<li class="gift-type">
								<div class="gift-name">
									<div class="gift-bg">
                                        <div class="gift-trick <#if investAmount?? && 1200000 <= investAmount?number>active</#if>"></div>
										<h3>Apple MacBook Air</h3>
										<p>
											<img src="${commonStaticServer}/activity/images/nowork/gift-ten.png" alt="">
										</p>
									</div>
								</div>
								<div class="gift-intro">
									累计投资金额:1,200,000元
								</div>
								<div class="gift-type">
									<p class="type-name">带薪假类别</p>
									<p class="type-intro">60天太空假，干掉PPT！</p>
								</div>
							</li>
							<li class="text-type">
								<div class="text-content-item">
									<div class="content-text">
										<p>活动期间投资“加薪专享”标，每完成一个投资目标，即可获得对应奖励，奖励可累计领取。</p>
										<p>栗子：如拓小天在活动第一天投资3000元，最后一天投资8000元，则拓小天在活动期间共投资了11000元，可以获得20元红包+30元话费。</p>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="content-skew-item">
				<div class="content-item">
					<h3 class="title-item text-c title-two">
						<img src="${commonStaticServer}/activity/images/nowork/title-two.png" alt="">
					</h3>
					<div class="actor-group">
						<ul class="gift-list-group step-list">
							<li class="text-type">
								<div class="text-content-item">
									<div class="content-text invite-person one">
										<p>活动期间邀请5名以下好友注册，且好友投资任意标的50元及以上（体验标除外），每邀请1名，即可获得20元话费，每人最多限领4次。</p>
									</div>
								</div>
							</li>
							<li class="text-type">
								<div class="text-content-item">
									<div class="content-text invite-person two">
										<p>活动期间用户邀请5名及以上好友注册，且好友投资任意标的50元及以上（体验标除外），每邀请5名即可获得100元旅行基金，如邀请10名好友注册投资，即可获得200元旅行基金，以此类推，上不封顶。</p>
									</div>
								</div>
							</li>
							<li class="text-type">
								<div class="text-content-item">
									<div class="content-text invite-person three">
										<p>活动期间所邀请的所有好友累计投资额每满5000元，即可获得100元旅行基金，上不封顶。如A邀请B注册，B在活动期间投资了10000元，则A可以获得200元旅行基金；如A邀请B和C注册，B和C在活动期间各投资了5000元，则A可获得200元旅行基金。</p>
									</div>
								</div>
							</li>
						</ul>
						<div class="invite-btn-group">
							<a href="/referrer/refer-list" class="invite-item" data-expired="${expired?c}")>邀请好友</a>
							<p>旅行基金以芒果旅游卡的形式发放。</p>
						</div>
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
							<dd>6、市场有风险，出借需谨慎</dd>
						</dl>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<#include "login-tip.ftl" />
</@global.main>