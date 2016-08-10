<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.pointsystem_order}" pageJavascript="${js.pointsystem_order}" activeNav="订单确认" activeLeftNav="" title="订单确认" site="membership">

<div class="global-member-order">
	<div class="wp clearfix">
		<div class="order-top">
			您所在的位置：积分商城 > 拓天速贷U盘><span>订单确认</span>
		</div>
	</div>
	<div class="wp clearfix order-item">
		<div class="container-order">
			<div class="order-place">
				<h3>添加您的收货地址</h3>
				<div class="address-item">
					<a href="javascript:void(0)" class="address-set">修改</a>
					<p class="user-name">刘海宁</p>
					<p>18610074450</p>
					<p title="北京市丰台区18号胡同对面街道办事处">北京市丰台区18号胡同对面街道办事处</p>
				</div>
			</div>
			<div class="order-info">
				<h3>订单确认</h3>
				<div class="order-table">
					<div class="order-picture">
						<img src="http://dummyimage.com/140x114/d1e9fa/686a82.gif&text=商品" width="140" height="114">
					</div>
					<div class="order-name">
						<p class="name-text">拓天速贷U盘</p>
						<p>限时抢兑</p>
						<p>拓天速贷系列U盘</p>
						<p>印花图案，8G内存</p>
					</div>
					<div class="order-price">
						<p class="title-text">商品价格</p>
						<p><i>20000</i>积分</p>
					</div>
					<div class="order-number">
						<p class="title-text">商品数量</p>
						<p class="count-list">
							<span class="count-btn low-btn">-</span>
							<input type="text" value="1" class="num-text">
							<span class="count-btn add-btn">+</span>
						</p>
						<p>
							<span class="total-num">
								剩余<i>8938</i>件
							</span>
						</p>
					</div>
					<div class="order-count">
						<p class="title-text">小计</p>
						<p>20000积分</p>
					</div>
				</div>
			</div>
			<div class="order-total">
				<p>
					<span>共需支付：<i>20000</i>积分</span>
				</p>
				<p>
					<input type="button" value="立即兑换" class="order-btn" id="orderBtn">
				</p>
			</div>
		</div>
	</div>
	<div class="container-ad">
	</div>
</div>




</@global.main>