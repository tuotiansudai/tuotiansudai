<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.point_order}" pageJavascript="${js.point_order}" activeNav="订单确认" activeLeftNav="" title="订单确认" site="membership">

<div class="global-member-order" id="pointOrder">
	<div class="wp clearfix">
		<div class="order-top">
            您所在的位置：<a href="/point-shop">积分商城</a> > <a
                href="/point-shop/${productShowItem.id?string('0')}/${productShowItem.goodsType}/detail">${productShowItem.name!}</a>><span>订单确认</span>
		</div>
	</div>
	<div class="wp clearfix order-item">
		<div class="container-order">
			<#if productShowItem.goodsType.name() == 'PHYSICAL'>
                <div class="order-place">
                	<div class="address-model">
						<h3>添加您的收货地址</h3>
						<div class="address-item">
							<#if addresses?size == 0>
                                <span class="add-place" id="addPlace">添加地址</span>
							<#else>
								<#list addresses as address>
                                    <a href="javascript:void(0)" class="address-set" data-id="${address.id?c!0}"
                                       data-user="${address.contact}" data-phone="${address.mobile}"
                                       data-address="${address.address}" id="updatePlace">修改</a>
									<p class="user-name">${address.contact}</p>
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
									<input class="int-text text-input" type="text" name="Recipient" id="Recipient" value="">
								</div>
							</div>
							<div class="input-group box-item">
								<label><em>*</em> 手机号码：</label>
								<div class="input-box">
									<input class="int-text text-input" type="text" name="Phone" id="Phone" value="">
								</div>
							</div>
							<div class="input-group">
								<label><em>*</em> 收件地址：</label>
								<div class="input-box">
									<textarea class="int-text text-area" type="text" name="AddRess"  id="AddRess" maxlength="100"></textarea>
								</div>
							</div>
							<div class="btn-group">
								<input type="submit" class="btn btn-save" value="保存收货地址" id="btnAddressSubit" disabled>
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
                        <p class="mater-img picture-item">
                            <img src="${commonStaticServer}/${productShowItem.imageUrl}" width="140" height="90"/>
                        </p>
					</div>
					<div class="order-name">
                        <p class="name-text">${productShowItem.name!}</p>

						<#if productShowItem.goodsType.name() == 'PHYSICAL' || productShowItem.goodsType.name() == 'VIRTUAL'>
                            <p>${productShowItem.description}</p>
						<#else>
							<#list productShowItem.description?split("\n") as str>
                                <p>${str}</p>
							</#list>
						</#if>
					</div>
					<div class="order-price">
						<p class="title-text">商品价格</p>

                        <p><i>${((productShowItem.points * discount)?round)?string('0')}</i>积分</p>
					</div>
					<div class="order-number" data-overplus="${productShowItem.leftCount?c!0}" data-mylimit="${productShowItem.monthLimit}" data-buycount="${buyCount}">
						<p class="title-text">商品数量</p>
						<p class="count-list" >
							<span class="count-btn low-btn">-</span>
                            <input type="text" value="${number}" class="num-text" readonly="readonly">
							<span class="count-btn add-btn">+</span>
						</p>
						<p>
							<span class="total-num">
								剩余<i>${productShowItem.leftCount?c!0}</i>件
								<#if productShowItem.monthLimit!=0>
                               		<span class="tip" id="exchangeTip">本月您还可以兑换<i></i>个</span>
								</#if>
							</span>
						</p>
					</div>
					<div class="order-count">
						<p class="title-text">小计</p>

                        <p><i class="count-num"
                              data-num="${((productShowItem.points * discount)?round)?string('0')}">${((productShowItem.points * discount)?round)?string('0')} * ${number}</i>积分
                        </p>
					</div>
				</div>
			</div>

            <div class="order-info my-order-info">
				备注：<textarea type="text" id="comment" placeholder="请您填写备注信息，比如颜色、规格等。如无备注则随机发货" maxlength="50"></textarea>
			</div>

			<div class="order-total">
				<p>
                    <span>共需支付：<i class="count-num"
                                  data-num="${((productShowItem.points * discount)?round)?string('0')}">${((productShowItem.points * discount)?round)?string('0')} * ${number}</i>积分</span>
				</p>
				<p>

					<#if productShowItem.leftCount == 0>
                        <input type="button" value="已售罄" class="order-btn">
					<#else>
                        <input type="button" value="立即兑换" class="order-btn" data-id="${productShowItem.id?c!0}"
                               data-type="${productShowItem.goodsType.name()}" id="orderBtn">
					</#if>

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