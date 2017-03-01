<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.point_record}" pageJavascript="${js.point_record}" activeNav="兑换记录" activeLeftNav="" title="兑换记录">

<div class="global-member-record">
	<div class="wp clearfix">
		<div class="detail-top">
			您所在的位置：<a href="/point-shop">积分商城</a> > <span>兑换记录&积分明细</span>
		</div>
		<div class="container-detail">
			<div class="type-list">
				<span class="active">
					<a href="/point-shop/record">兑换记录</a>
				</span>
				<span>|</span>
				<span>
					<a href="/point-shop/bill">积分明细</a>
				</span>
			</div>
			<div class="data-list" id="dataList"></div>
			<script type="text/html" id="dataListTpl">
				<table class="table">
					<thead>
						<tr>
							<th>商品</th>
							<th>价格</th>
							<th>数量</th>
							<th>小计</th>
							<th>兑换时间</th>
						</tr>
					</thead>
					<tbody>
                    {{if records}}
                    {{each records}}
                    <tr>
                        <td>{{$value.name}}</td>
                        <td>{{$value.actualPoints}}</td>
                        <td>{{$value.num}}</td>
                        <td>{{$value.actualPoints*$value.num}}</td>
                        <td>{{$value.createdTime}}</td>
                    </tr>
                    {{/each}}
                    {{else}}
                    暂无数据
                    {{/if}}
					</tbody>
				</table>
			</script>
		</div>
		<div class="pagination" data-url="/point-shop/record-list" id="pageList"></div>
	</div>
	<div class="container-ad">
	</div>
</div>
</@global.main>