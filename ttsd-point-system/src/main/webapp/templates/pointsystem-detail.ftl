<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.pointsystem_detail}" pageJavascript="${js.pointsystem_detail}" activeNav="商品详情" activeLeftNav="" title="商品详情">

<div class="global-member-detail">
	<div class="wp clearfix detail-model">
		<div class="container-detail">
			<div class="detail-top">
                您所在的位置：积分商城 > <span>${productShowItem.productName}</span>
			</div>
			<div class="detail-info">
				<div class="detail-left">
					<#if productShowItem.itemType.name() == 'RED_ENVELOPE'>
                        <p class="mater-img bag-bg">
                            <span><i><@amount>${productShowItem.pictureDescription!"0"}</@amount></i>元</span>
                        </p>
					<#elseif productShowItem.itemType.name() == 'INVEST_COUPON'>
                        <p class="mater-img coupon-bg">
                            <span><i><@amount>${productShowItem.pictureDescription!"0"}</@amount></i>元</span>
                            <span>投资体验券</span>
                        </p>
					<#elseif productShowItem.itemType.name() == 'INTEREST_COUPON'>
                        <p class="mater-img jia-bg">
                            <span><i>${productShowItem.pictureDescription!"0"}</i>%</span>
                            <span>加息券</span>
                        </p>
					<#else>
                        <p class="mater-img picture-item">
                            <img src="${productShowItem.imageUrl}" width="300" height="244"/>
                        </p>
					</#if>
				</div>
				<div class="detail-right">
                    <h3>${productShowItem.productName}</h3>
					<div class="info-text">
						<div class="info-name">
							商品介绍：
						</div>
                        <div class="info-content">
							<#if productShowItem.itemType.name() == 'PHYSICAL' || productShowItem.itemType.name() == 'VIRTUAL'>
                                <p>${productShowItem.description}</p>
							<#else>
								<#list productShowItem.description?split("\n") as str>
                                    <p>${str}</p>
								</#list>
							</#if>
						</div>
					</div>
					<div class="info-text mt-price">
						<div class="info-name">
							尊享价：
						</div>
						<div class="info-content">
							<div class="price-text">
                                <span>${productShowItem.productPrice}</span>积分
							</div>
						</div>
					</div>
					<div class="info-text">
						<div class="info-name">
							数量：
						</div>
						<div class="info-content">
							<div class="count-list">
								<span class="count-btn low-btn">-</span>
								<input type="text" value="1" class="num-text" readonly="readonly">
								<span class="count-btn add-btn">+</span>
								<span class="total-num">
									剩余<i>${productShowItem.leftCount?c!0}</i>件
								</span>
							</div>
						</div>
					</div>
					<div class="info-text mt-20">
                        <a href="/pointsystem/order/${productShowItem.id?c!"0"}?itemType=${productShowItem.itemType.name()}"
                           class="btn get-btn">立即兑换</a>
					</div>
					<div class="info-text mt-20">
						<#if productShowItem.itemType.name() == 'PHYSICAL' || productShowItem.itemType.name() == 'VIRTUAL'>
                            <p class="tip-text">需要修改文案</p>
						<#else>
                            <p class="tip-text">兑换成功后，请前去“我的账户”－》“我的宝藏”中进行查看</p>
						</#if>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="container-ad">
	</div>
</div>




</@global.main>