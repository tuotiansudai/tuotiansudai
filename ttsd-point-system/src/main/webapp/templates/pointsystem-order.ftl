<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.pointsystem_order}" pageJavascript="${js.pointsystem_order}" activeNav="订单确认" activeLeftNav="" title="订单确认" site="membership">

<div class="global-member-order">
	<div class="wp clearfix">
		<div class="order-top">
            您所在的位置：<a href="/pointsystem">积分商城</a> > <a
                href="/pointsystem/${productShowItem.id?string('0')}/${productShowItem.itemType}/detail">${productShowItem.productName}</a>><span>订单确认</span>
		</div>
	</div>
	<div class="wp clearfix order-item">
		<div class="container-order">
			<#if productShowItem.itemType.name() == 'PHYSICAL'>
                <div class="order-place">
                	<div class="address-model">
						<h3>添加您的收货地址</h3>
						<div class="address-item">
							<#if addresses?size == 0>
                                <span class="add-place" id="addPlace">添加地址</span>
							<#else>
								<#list addresses as address>
                                    <a href="javascript:void(0)" class="address-set" data-id="${address.id?c!0}"
                                       data-user="${address.realName}" data-phone="${address.mobile}"
                                       data-address="${address.address}" id="updatePlace">修改</a>
									<p class="user-name">${address.realName}</p>
									<p>${address.mobile}</p>
									<p title="${address.address}">${address.address}</p>
								</#list>
							</#if>
						</div>
					</div>
					<div class="fix-address" id="fixAdress">
						<div class="addr-form">
						<form action="#" method="post" id="addressForm">
							<div class="input-group box-item">
								<label><em>*</em> 收件人：</label>
								<div class="input-box">
									<input class="text-input" type="text" name="Recipient" id="Recipient" value="">
								</div>
							</div>
							<div class="input-group box-item">
								<label><em>*</em> 手机号码：</label>
								<div class="input-box">
									<input class="text-input" type="text" name="Phone" id="Phone" value="">
								</div>
							</div>
							<div class="input-group">
								<label><em>*</em> 收件地址：</label>
								<div class="input-box">
									<textarea class="text-area" type="text" name="AddRess"  id="AddRess" maxlength="100"></textarea>
								</div>
							</div>
							<div class="btn-group">
								<input type="submit" class="btn btn-save" value="保存收货地址" id="btnAddressSubit">
							</div>
						</form>
						</div>
					</div>
                </div>
			</#if>
			<div class="order-info">
				<h3>订单确认</h3>
				<div class="order-table">
					<div class="order-picture">
						<#if productShowItem.itemType.name() == 'RED_ENVELOPE'>
                            <p class="mater-img bag-bg">
                                <span><i><@amount>${productShowItem.pictureDescription!0}</@amount></i>元</span>
                            </p>
						<#elseif productShowItem.itemType.name() == 'INVEST_COUPON'>
                            <p class="mater-img coupon-bg">
                                <span><i><@amount>${productShowItem.pictureDescription!0}</@amount></i>元</span>
                                <span>投资体验券</span>
                            </p>
						<#elseif productShowItem.itemType.name() == 'INTEREST_COUPON'>
                            <p class="mater-img jia-bg">
                                <span><i>${productShowItem.pictureDescription!"0"}</i>%</span>
                                <span>加息券</span>
                            </p>
						<#else>
                            <p class="mater-img picture-item">
                                <img src="${productShowItem.imageUrl}" width="140" height="114"/>
                            </p>
						</#if>
					</div>
					<div class="order-name">
                        <p class="name-text">${productShowItem.productName}</p>

                        <p>${productShowItem.description}</p>
					</div>
					<div class="order-price">
						<p class="title-text">商品价格</p>

                        <p><i>${productShowItem.productPrice?string('0')}</i>积分</p>
					</div>
					<div class="order-number">
						<p class="title-text">商品数量</p>
						<p class="count-list">
							<span class="count-btn low-btn">-</span>
                            <input type="text" value="${number}" class="num-text" readonly="readonly">
							<span class="count-btn add-btn">+</span>
						</p>
						<p>
							<span class="total-num">
								剩余<i>${productShowItem.leftCount?c!0}</i>件
							</span>
						</p>
					</div>
					<div class="order-count">
						<p class="title-text">小计</p>

                        <p><i class="count-num"
                              data-num="${productShowItem.productPrice?string('0')}">${productShowItem.productPrice * number}</i>积分
                        </p>
					</div>
				</div>
			</div>
			<div class="order-total">
				<p>
                    <span>共需支付：<i class="count-num"
                                  data-num="${productShowItem.productPrice?string('0')}">${productShowItem.productPrice * number}</i>积分</span>
				</p>
				<p>
                    <input type="button" value="立即兑换" class="order-btn" data-id="${productShowItem.id?c!0}"
                           data-type="${productShowItem.itemType.name()}" id="orderBtn">
				</p>
			</div>
		</div>
	</div>
	<div class="container-ad">
	</div>
	<div class="error-tip" id="errorTip"></div>
	<script type="text/html" id="errorTipTpl">
		<h3>温馨提示</h3>
		<p>{{message}}</p>
		<a href="javascript:void(0)" class="close-layer">确认</a>
	</script>
</div>
</@global.main>