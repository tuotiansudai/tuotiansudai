<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.membership_integral}" pageJavascript="${js.membership_integral}" activeNav="积分明细" activeLeftNav="" title="积分明细" site="membership">

<div class="global-member-integral">
	<div class="wp clearfix">
		<div class="detail-top">
			您所在的位置：积分商城 > <span>兑换记录&积分明细</span>
		</div>
		<div class="container-detail">
			<div class="type-list">
				<span>
					<a href="/membership/record">兑换记录</a>
				</span>
				<span>|</span>
				<span class="active">
					<a href="/membership/integral">积分明细</a>
				</span>
			</div>
			<div class="item-block date-filter">
		        <span class="sub-hd">起止时间:</span>
		        <input type="text" id="date-picker" class="input-control" size="35"/>
		        <span class="select-item" data-day="1">今天</span>
		        <span class="select-item" data-day="7">最近一周</span>
		        <span class="select-item current" data-day="30">一个月</span>
		        <span class="select-item" data-day="180">六个月</span>
		        <span class="select-item" data-day="">全部</span>
		    </div>

		    <div class="item-block status-filter">
		        <span class="sub-hd">交易状态:</span>
		        <span class="select-item current" data-status="">全部</span>
		        <span class="select-item" data-status="RAISING">正在招募</span>
		        <span class="select-item" data-status="RECHECK">招募成功</span>
		        <span class="select-item" data-status="REPAYING">正在回款</span>
		        <span class="select-item" data-status="COMPLETE">回款完毕</span>
		    </div>
			<div class="data-list" id="dataList">
				<table class="table">
					<thead>
						<tr>
							<th>时间</th>
							<th>行为</th>
							<th>财豆数(个)</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>2016-05-26  09:49:30</td>
							<td>兑换商品</td>
							<td>－80</td>
							<td>2016-05-26  签到</td>
						</tr>
						<tr>
							<td>2016-05-26  09:49:30</td>
							<td>兑换商品</td>
							<td>+80</td>
							<td>2016-05-26  签到</td>
						</tr>
						<tr>
							<td>2016-05-26  09:49:30</td>
							<td>兑换商品</td>
							<td>－80</td>
							<td>2016-05-26  签到</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="pagination" data-url="/announce/list" id="pageList"></div>
	</div>
	<div class="container-ad">
	</div>
</div>



</@global.main>