<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.point_bill}" pageJavascript="${js.point_bill}" activeNav="积分明细" activeLeftNav="" title="积分明细">

<div class="global-member-integral">
	<div class="wp clearfix">
		<div class="detail-top">
			您所在的位置：<a href="/point-shop">积分商城</a> > <span>兑换记录&积分明细</span>
		</div>
		<div class="container-detail">
			<div class="type-list">
				<span>
					<a href="/point-shop/record">兑换记录</a>
				</span>
				<span>|</span>
				<span class="active">
					<a href="/point-shop/bill">积分明细</a>
				</span>
			</div>
			<div class="item-block date-filter">
		        <span class="sub-hd">起止时间:</span>
		        <input type="text" id="date-picker" class="input-control" size="35" readonly/>
		        <span class="select-item" data-day="1">今天</span>
		        <span class="select-item" data-day="7">最近一周</span>
		        <span class="select-item current" data-day="30">一个月</span>
		        <span class="select-item" data-day="180">六个月</span>
		        <span class="select-item" data-day="">全部</span>
		    </div>

		    <div class="item-block status-filter">
                <span class="sub-hd">往来类型:</span>
                <span class="select-item current" data-status="">全部</span>
                <span class="select-item" data-status="INCOME">已获取</span>
                <span class="select-item" data-status="EXPENSE">已使用</span>
		    </div>
			<div class="data-list" id="dataList"></div>
			<script type="text/html" id="dataListTpl">
				<table class="table">
					<thead>
						<tr>
							<th>时间</th>
							<th>行为</th>
							<th>积分</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody>
					{{if records}}
					{{each records}}
						<tr>
							<td>{{$value.createdTime}}</td>
							<td>{{$value.businessType}}</td>
							<td>{{$value.point}}</td>
							<td>{{$value.note}}</td>
						</tr>
					{{/each}}
					{{else}}
					暂无数据
					{{/if}}
					</tbody>
				</table>
			</script>
		</div>
		<div class="pagination" data-url="/point-shop/bill-list" data-page-size="10" id="pageList"></div>
	</div>
	<div class="container-ad">
	</div>
</div>
</@global.main>