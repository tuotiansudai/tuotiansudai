<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.pointsystem_bill}" pageJavascript="${js.pointsystem_bill}" activeNav="积分明细" activeLeftNav="" title="积分明细">

<div class="global-member-integral">
	<div class="wp clearfix">
		<div class="detail-top">
			您所在的位置：<a href="/pointsystem">积分商城</a> > <span>兑换记录&积分明细</span>
		</div>
		<div class="container-detail">
			<div class="type-list">
				<span>
					<a href="/pointsystem/record">兑换记录</a>
				</span>
				<span>|</span>
				<span class="active">
					<a href="/pointsystem/bill">积分明细</a>
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
                <span class="sub-hd">往来类型:</span>
                <span class="select-item current" data-status="">全部</span>
                <span class="select-item" data-status="SIGN_IN,TASK,INVEST">已获取</span>
                <span class="select-item" data-status="EXCHANGE,LOTTERY">已使用</span>
		    </div>

            <div class="clear-blank"></div>
            <div class="bill-list">
            </div>
            <div class="data-list" data-url="/pointsystem/bill-list-data" data-page-size="10">
            </div>




			<div class="data-list" id="dataList"></div>
			<script type="text/html" id="dataListTpl">
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
					{{if data}}
					{{each data}}
						<tr>
							<td>2016-05-26  09:49:30</td>
							<td>兑换商品</td>
							<td>－80</td>
							<td>2016-05-26  签到</td>
						</tr>
					{{/each}}
					{{else}}
					暂无数据
					{{/if}}
					</tbody>
				</table>
			</script>
		</div>
		<div class="pagination" data-url="/announce/list" id="pageList"></div>
	</div>
	<div class="container-ad">
	</div>
</div>



</@global.main>