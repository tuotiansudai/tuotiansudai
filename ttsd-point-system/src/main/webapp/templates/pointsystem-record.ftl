<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.pointsystem_record}" pageJavascript="${js.pointsystem_record}" activeNav="兑换记录" activeLeftNav="" title="兑换记录">

<div class="global-member-record">
	<div class="wp clearfix">
		<div class="detail-top">
			您所在的位置：积分商城 > <span>兑换记录&积分明细</span>
		</div>
		<div class="container-detail">
			<div class="type-list">
				<span class="active">
					<a href="/membership/record">兑换记录</a>
				</span>
				<span>|</span>
				<span>
					<a href="/membership/integral">积分明细</a>
				</span>
			</div>
			<div class="data-list" id="dataList">
				<table class="table">
					<thead>
						<tr>
							<th>商品</th>
							<th>价格</th>
							<th>数量</th>
							<th>小计</th>
							<th>兑换时间</th>
							<th>商品编号</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>3%加息券</td>
							<td>20000</td>
							<td>2</td>
							<td>40000</td>
							<td>2016-05-05</td>
							<td>20160606002</td>
							<td>11</td>
						</tr>
						<tr>
							<td>3%加息券</td>
							<td>20000</td>
							<td>2</td>
							<td>40000</td>
							<td>2016-05-05</td>
							<td>20160606002</td>
							<td>11</td>
						</tr>
						<tr>
							<td>3%加息券</td>
							<td>20000</td>
							<td>2</td>
							<td>40000</td>
							<td>2016-05-05</td>
							<td>20160606002</td>
							<td>11</td>
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