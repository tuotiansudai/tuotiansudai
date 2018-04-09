<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.point_update}" pageJavascript="" activeNav="" activeLeftNav="" title="积分体系_积分商城_拓天速贷" keywords="会员积分,投资积分,签到积分,邀好友积分,拓天速贷" description="拓天速贷积分体系豪华升级,投资得积分,邀好友送积分,签到攒积分,做任务赢积分,会员积分兑好礼,五大秘籍玩转积分商城.">
<div class="point-update-container">
	<div class="top-item compliance-banner">
        <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>
	</div>
	<div class="wp clearfix">
		<div class="content-item">
			<div class="item-icon"></div>
			<h3 class="title-one"></h3>
			<ul class="get-point-item">
				<li>
					<div class="title-name">
						<p class="name-text">投资得积分</p>
						<p>多投多得更靠谱</p>
					</div>
					<table>
						<thead>
							<tr>
								<th>投资类型</th>
								<th>获得积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>30天抵押类债权</td>
								<td>投资金额/365*30</td>
							</tr>
							<tr>
								<td>90天抵押类债权</td>
								<td>投资金额/365*90</td>
							</tr>
							<tr>
								<td>180天抵押类债权</td>
								<td>投资金额/365*180</td>
							</tr>
							<tr>
								<td>360天抵押类债权</td>
								<td>投资金额/365*360</td>
							</tr>
						</tbody>
					</table>
					<div class="tip-info">
						投资指定项目，左手赚收益，右手赚积分。如投资10000元180天债权，可获得4931积分。
					</div>
					<div class="btn-item">
						<a href="/loan-list" class="btn-model">马上投资</a>
					</div>
				</li>
				<li>
					<div class="title-name">
						<p class="name-text">邀好友送积分</p>
						<p>人品值不封顶</p>
					</div>
					<table>
						<thead>
							<tr>
								<th>推荐好友</th>
								<th>获得积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>好友注册</td>
								<td>100积分/人</td>
							</tr>
							<tr>
								<td>好友绑定银行卡</td>
								<td>100积分/人</td>
							</tr>
							<tr>
								<td>好友首次投资</td>
								<td>200积分/人</td>
							</tr>
						</tbody>
					</table>
					<div class="tip-info">
						每邀请一名好友都有积分入袋，邀请越多积分越多，同时又能拿推荐礼包。还等什么？还不速速去让人脉变现！
					</div>
					<div class="btn-item mt-center">
						<a href="/referrer/refer-list" class="btn-model">邀请好友</a>
					</div>
				</li>
				<li>
					<div class="title-name">
						<p class="name-text">每日签到攒积分</p>
						<p>积少成多玩出趣</p>
					</div>
					<table>
						<thead>
							<tr>
								<th>连续签到天数</th>
								<th>每日获得积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>1~3天</td>
								<td>10积分</td>
							</tr>
							<tr>
								<td>4~6天</td>
								<td>15积分</td>
							</tr>
							<tr>
								<td>7~10天</td>
								<td>20积分</td>
							</tr>
							<tr>
								<td>11~15天</td>
								<td>25积分</td>
							</tr>
							<tr>
								<td>15天以上</td>
								<td>30积分</td>
							</tr>
						</tbody>
					</table>
					<div class="tip-info">
						连续签到还能领红包哦！
					</div>
					<div class="btn-item mt-right">
						<#if !isAppSource>
                            <a href="/point-shop" class="btn-model">去签到</a>
						<#else>
                            <a href="app/tuotian/point-home" class="btn-model">去签到</a>
						</#if>
					</div>
				</li>
			</ul>
		</div>
		<div class="content-item media-phone">
			<div class="item-icon"></div>
			<h3 class="title-two"></h3>
			<ul class="get-point-item">
				<li>
					<table>
						<thead>
							<tr>
								<th class="phone-th">任务</th>
								<th class="phone-th">获得积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>实名认证</td>
								<td>100积分</td>
							</tr>
							<tr>
								<td>绑定银行卡</td>
								<td>100积分</td>
							</tr>
							<tr>
								<td>首次充值</td>
								<td>200积分</td>
							</tr>
							<tr>
								<td>首次投资</td>
								<td>200积分</td>
							</tr>
							<tr>
								<td>首次开通免密支付</td>
								<td>100积分</td>
							</tr>
							<tr>
								<td>累计投资满5000元</td>
								<td>100积分</td>
							</tr>
							<tr>
								<td>单笔投资满10000元</td>
								<td>200积分</td>
							</tr>
							<tr>
								<td>首次投资180天标的</td>
								<td>100积分</td>
							</tr>
							<tr>
								<td>首次投资360天标的</td>
								<td>200积分</td>
							</tr>
						</tbody>
					</table>
					<div class="tip-info">
						新手积分初体验，每完成一项任务都有积分入账，如全部通关可得1300积分。送到手的积分，不拿=浪费哦！
					</div>
					<div class="btn-item">
						<#if !isAppSource>
						<a href="/point-shop/task" class="btn-model">去完成</a>
						<#else>
                        <a href="app/tuotian/point-task" class="btn-model">去完成</a>
						</#if>
					</div>
				</li>
			</ul>
		</div>
		<div class="content-item media-pc">
			<div class="item-icon"></div>
			<h3 class="title-two"></h3>
			<ul class="task-point-item">
				<li>
					<table>
						<thead>
							<tr>
								<th>任务</th>
								<th>获得积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>实名认证</td>
								<td>100积分</td>
							</tr>
							<tr>
								<td>绑定银行卡</td>
								<td>100积分</td>
							</tr>
							<tr>
								<td>首次充值</td>
								<td>200积分</td>
							</tr>
							<tr>
								<td>首次投资</td>
								<td>200积分</td>
							</tr>
							<tr>
								<td>首次开通免密支付</td>
								<td>100积分</td>
							</tr>
						</tbody>
					</table>
				</li>
				<li>
					<table>
						<thead>
							<tr>
								<th>任务</th>
								<th>获得积分</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>累计投资满5000元</td>
								<td>100积分</td>
							</tr>
							<tr>
								<td>单笔投资满10000元</td>
								<td>200积分</td>
							</tr>
							<tr>
								<td>首次投资180天标的</td>
								<td>50积分</td>
							</tr>
							<tr>
								<td>首次投资360天标的</td>
								<td>200积分</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
						</tbody>
					</table>
				</li>
			</ul>
			<div class="task-info">
				新手积分初体验，每完成一项任务都有积分入账，如全部通关可得1300积分。送到手的积分，不拿=浪费哦！
			</div>
			<div class="btn-item">
				<#if !isAppSource>
                    <a href="/point-shop/task" class="btn-model">去完成</a>
				<#else>
                    <a href="app/tuotian/point-task" class="btn-model">去完成</a>
				</#if>
			</div>
		</div>
		<div class="content-item left-item">
			<div class="item-icon"></div>
			<h3 class="title-three"></h3>
			<div class="vip-info">
				成为拓天速贷尊贵会员，兑换积分商城商品专享不同程度优惠。
			</div>
			<div class="vip-item">
				<table>
					<thead>
						<tr>
							<th>会员等级</th>
							<th>积分商城折扣</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>V2会员</td>
							<td>9.5折</td>
						</tr>
						<tr>
							<td>V3会员</td>
							<td>9.2折</td>
						</tr>
						<tr>
							<td>V4会员</td>
							<td>8.8折</td>
						</tr>
						<tr>
							<td>V5会员</td>
							<td>8.5折</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="btn-item">
				<#if !isAppSource>
                    <a href="/membership" class="btn-model vip-btn">查看我的会员等级</a>
				<#else>
                    <a href="app/tuotian/vip-center" class="btn-model vip-btn">查看我的会员等级</a>
				</#if>

			</div>
		</div>
		<div class="content-item right-item">
			<div class="item-icon"></div>
			<h3 class="title-four"></h3>
			<ul class="point-item">
				<li class="one">
					<p class="title-name">小积分抽大奖</p>
					<p>每抽奖1次消耗1000积分，以小博大拼手气。</p>
				</li>
				<li class="two">
					<p class="title-name">兑换红包加息券，收益翻不停</p>
					<p>使用积分可兑换虚拟奖品，为收益加码。</p>
				</li>
				<li class="three">
					<p class="title-name">丰厚实物奖，惊喜大派“兑”</p>
					<p>积分换好礼，多种精选实物奖品随心兑。</p>
				</li>
			</ul>
			<div class="btn-item">
				<#if !isAppSource>
                    <a href="/point-shop" class="btn-model">去往积分商城</a>
				<#else>
                    <a href="app/tuotian/point-home" class="btn-model">去往积分商城</a>
				</#if>

			</div>
		</div>
		<dl class="rule-item">
			<dt>温馨提示：</dt>
			<dd>1、用户投资30天、90天、180天、360天抵押类债权，根据累计年化投资额，可获得等值的兑换积分。投资体验项目及债权转让不参与积分的累计，积分只保留整数位，小数点后部分直接抹去；</dd>
			<dd>2、拓天速贷将于每年10月21日24时，对所有用户账户内积分进行清零，清零后积分将重新累计，逾期未使用的积分将自动作废；</dd>
			<dd>3、用户在积分商城中所兑换的红包、加息劵等虚拟奖品实时发放，用户可在电脑端“我的账户-我的宝藏”或App端“我的-优惠券”中查看；</dd>
			<dd>4、话费、爱奇艺会员、优酷会员、电影票、京东E卡、实物奖品将于兑换成功后7个工作日内由客服联系发放，部分地区邮费自付，请保持注册手机畅通，以便客服人员与您联系；</dd>
			<dd>5、用户所获得的积分仅限拓天速贷平台内使用，不可折现，不同账户积分不可合并使用；</dd>
			<dd>6、实物商品兑换订单完成后不可以取消或修改，若无商品质量问题，不予办理商品退换货，如用户在签收时发现商品有质量问题请立即联系客服；</dd>
			<dd>7、在获取和使用积分过程中，如果出现违规行为（如作弊领取等），拓天速贷将取消您获得积分的资格，并有权撤销违规交易，收回活动中所得的积分（含已使用的积分及未使用的积分），必要时将追究法律责任；</dd>
			<dd>8、拓天速贷在法律范围内保留对活动的最终解释权。</dd>
		</dl>
	</div>
</div>
</@global.main>