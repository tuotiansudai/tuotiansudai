<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.membership}" pageJavascript="${js.membership}" activeNav="成长体系" activeLeftNav="" title="成长体系" site="membership">

<div class="global-member-ship">
	<#-- 登录 -->
	<div class="user-info-block page-width structure">
		<div class="title">会员等级</div>
		<div class="info clearfix">
			<div class="avatar fl">
				<img src="${staticServer}/images/sign/head.png" />
				<#-- 更改下面这个i标签的class值来改变显示的等级 0级对应vip-0 1级对应vip-1 以此类推-->
				<i class="vip-no-bg vip-0"></i>
			</div>
			<div class="text">
				我当前等级<i class="vip-no-bg vip-0"></i>特权了哦！
				成长值：70000
			</div>
		</div>
		<div class="progress">
			<div class="progress-bar" style="margin-top: 45px;">
				<div class="vip-bg vip-0"></div>
				<div class="vip-bg vip-1"></div>
				<div class="vip-bg vip-2"></div>
				<div class="vip-bg vip-3"></div>
				<div class="vip-bg vip-4"></div>
				<div class="vip-bg vip-5"></div>
				<div class="popup-number vip-0">0</div>
				<div class="popup-number vip-1">5000</div>
				<div class="popup-number vip-2">50,000</div>
				<div class="popup-number vip-3">300,000</div>
				<div class="popup-number vip-4">1,500,000</div>
				<div class="popup-number vip-5">5,000,000</div>
			</div>
		</div>
	</div>
	<#-- 未登录 -->
	<div class="user-info-block page-width no-login">
		<div class="info clearfix">
			<div class="avatar fl">
				<img src="${staticServer}/images/sign/head.png" />
			</div>
			<div class="text">
				亲，成为会员可享受多种特权哦~ <br />
				了解更多请 <a href="/login" class="btn-normal">登录</a>
			</div>
		</div>
		<div class="progress">
			<div class="progress-bar" style="margin-top: 45px;">
				<div class="vip-bg vip-0"></div>
				<div class="vip-bg vip-1"></div>
				<div class="vip-bg vip-2"></div>
				<div class="vip-bg vip-3"></div>
				<div class="vip-bg vip-4"></div>
				<div class="vip-bg vip-5"></div>
				<div class="popup-number vip-0">0</div>
				<div class="popup-number vip-1">5000</div>
				<div class="popup-number vip-2">50,000</div>
				<div class="popup-number vip-3">300,000</div>
				<div class="popup-number vip-4">1,500,000</div>
				<div class="popup-number vip-5">5,000,000</div>
			</div>
		</div>
		<div class="register">
			新用户请 <a href="/register">注册</a>
		</div>
	</div>


	<div class="instructions" id="instructions">
		<div class="main-title">
			<div class="inner">
				<h2>会员说明</h2>
			</div>
		</div>
		<div class="inner-block">
			<div class="item active">
				<div class="question">
					什么是会员?
				</div>
				<div class="answer">
					答：是快乐的附件是劳动法谁离开的房间里撒开房间卡死啦的附件流口水的减肥了卡萨减肥了开始放假了快速的积分卡拉斯加对方离开，是浪费大家撒开了的房间时打发速度快了附件是老款的附件
				</div>
			</div>
			<div class="item">
				<div class="question">
					什么是会员?
				</div>
				<div class="answer">
					答：是快乐的附件是劳动法谁离开的房间里撒开房间卡死啦的附件流口水的减肥了卡萨减肥了开始放假了快速的积分卡拉斯加对方离开，是浪费大家撒开了的房间时打发速度快了附件是老款的附件
				</div>
			</div>
			<div class="item">
				<div class="question">
					什么是会员?
				</div>
				<div class="answer">
					答：是快乐的附件是劳动法谁离开的房间里撒开房间卡死啦的附件流口水的减肥了卡萨减肥了开始放假了快速的积分卡拉斯加对方离开，是浪费大家撒开了的房间时打发速度快了附件是老款的附件
				</div>
			</div>
			<div class="item">
				<div class="question">
					什么是会员?
				</div>
				<div class="answer">
					答：是快乐的附件是劳动法谁离开的房间里撒开房间卡死啦的附件流口水的减肥了卡萨减肥了开始放假了快速的积分卡拉斯加对方离开，是浪费大家撒开了的房间时打发速度快了附件是老款的附件
				</div>
			</div>
		</div>
	</div>


	<div class="structure-detail">
		<div class="main-title">
			<div class="inner">
				<h2>成长值明细</h2>
			</div>
		</div>
		<div class="inner-block">
			<div class="filter-bar">
				<div class="clearfix">
					<div class="fl buttons">
						<span class="active">全部</span>
						<span>六个月</span>
						<span>一个月</span>
						<span>本周</span>
						<span>今天</span>
					</div>
					<div class="fr">
						<label>日期</label>
						<input type="text" />
					</div>
				</div>
			</div>
			<div class="table">
				<table>
					<thead>
						<tr>
							<th>日期</th>
							<th>详情</th>
							<th>成长值</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>2016-06-23</td>
							<td>投资车辆抵押借款项目7000元</td>
							<td>+7000</td>
						</tr>
						<tr>
							<td>2016-06-23</td>
							<td>投资车辆抵押借款项目7000元</td>
							<td>+7000</td>
						</tr>
						<tr>
							<td>2016-06-23</td>
							<td>投资车辆抵押借款项目7000元</td>
							<td>+7000</td>
						</tr>
						<tr>
							<td>2016-06-23</td>
							<td>投资车辆抵押借款项目7000元</td>
							<td>+7000</td>
						</tr>
						<tr>
							<td>2016-06-23</td>
							<td>投资车辆抵押借款项目7000元</td>
							<td>+7000</td>
						</tr>
						<tr>
							<td>2016-06-23</td>
							<td>投资车辆抵押借款项目7000元</td>
							<td>+7000</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>


</@global.main>