<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'loan_king' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>

<@global.main pageCss="${css.loan_king}" pageJavascript="${js.loan_king}" activeNav="" activeLeftNav="" title="标王争霸场_拓天周年庆_活动中心_拓天速贷" keywords="拓天速贷,拓天周年庆,京东E卡,红包奖励" description="拓天周年庆-标王争霸场活动,活动期间,每个债权针对用户在的累计投资额进行排名,前三名相应可获得100元京东E卡和红包奖励.">
<div class="loan-king-container" id="loanKingContainer">
	<div class="top-item">
		<img src="" width="100%" class="media-pc">
		<img src="" width="100%" class="media-phone">
	</div>
	<div class="wp clearfix">
		<div class="content-item">
			<h3>活动期间，每个债权根据用户在该债权的累计投资额进行排名，前三名可获丰厚奖励。</h3>
			<div class="loan-list">
				<img src="" width="80%" class="media-pc">
				<img src="" width="90%" class="media-phone">
			</div>
		</div>
		<div class="loan-title"></div>
		<div class="current-loan">
			<h3>房产抵押借款17081</h3>
			<ul class="loan-info">
				<li class="tl">
					<span>预期年化收益</span>
					<span><strong>10</strong>%</span>
				</li>
				<li class="tc">
					<span>项目期限</span>
					<span><strong>3</strong>天</span>
				</li>
				<li class="tr">
					<span>招募金额</span>
					<span><strong>30</strong>万</span>
				</li>
			</ul>
			<div class="progress-line">
				<span></span>
				<span>100.00%</span>
			</div>
			<table class="loan-table">
				<tbody>
					<tr>
						<td>暂居标王</td>
						<td>185****4386</td>
						<td>10.05万</td>
					</tr>
					<tr>
						<td>第二名</td>
						<td>185****4386</td>
						<td>10.05万</td>
					</tr>
					<tr>
						<td>第三名</td>
						<td>185****4386</td>
						<td>10.05万</td>
					</tr>
				</tbody>
			</table>
			<div class="loan-btn">
				<a href="/loan-list">速抢标王</a>
			</div>
		</div>
	</div>
</div>
</@global.main>