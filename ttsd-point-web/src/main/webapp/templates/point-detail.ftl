<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.point_detail}" pageJavascript="${js.point_detail}" activeNav="商品详情" activeLeftNav="" title="商品详情">

<div class="global-member-detail" id="pointDetail">
    <div class="wp clearfix detail-model">
        <div class="container-detail">
            <div class="detail-top">
                您所在的位置：<a href="/point-shop">积分商城</a> > <span>${productShowItem.name!}</span>
            </div>
            <div class="detail-info">
                <div class="detail-left">
                    <p class="mater-img picture-item">
                        <img src="${commonStaticServer}/${productShowItem.imageUrl}" width="300" height="200"/>
                    </p>
                </div>
                <div class="detail-right">
                    <h3>${productShowItem.name!}</h3>
                    <div class="info-text">
                        <div class="info-name">
                            商品介绍：
                        </div>
                        <div class="info-content">
                            <#if productShowItem.goodsType.name() == 'PHYSICAL' || productShowItem.goodsType.name() == 'VIRTUAL'>
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
                                <span>${((productShowItem.points * discount)?round)?string('0')}</span>积分
                                <#if discount?? && discount?floor != 1>
                                    <span class="old-price">${productShowItem.points?string('0')}积分</span>
                                </#if>
                            </div>
                        </div>
                    </div>
                    <div class="info-text">
                        <div class="info-name">
                            数量：
                        </div>
                        <div class="info-content">
                            <div class="count-list" data-overplus="${productShowItem.leftCount?c!0}"
                                 data-mylimit="${productShowItem.monthLimit}" data-buycount="${buyCount}">
                                <span class="count-btn low-btn">-</span>
                                <input type="text" value="1" class="num-text" readonly="readonly">
                                <span class="count-btn add-btn">+</span>
                                <span class="total-num" id="productNumber">
									剩余<i>${productShowItem.leftCount?c!0}</i>件
								</span>
                            </div>
                        </div>
                    </div>
                    <div class="info-text mt-20">
                        <#if productShowItem?? && productShowItem.leftCount == 0>
                            <a class="btn get-btn">已售罄</a>
                        <#else>
                            <a href="javascript:void(0)" class="btn get-btn" data-id="${productShowItem.id?c!0}"
                               data-type="${productShowItem.goodsType.name()}" id="getBtn">立即兑换</a>
                            <@global.isNotAnonymous>
                                <#if productShowItem.monthLimit!=0>
                                    <span class="tip" id="exchangeTip">本月您还可以兑换<i></i>个</span>
                                </#if>
                            </@global.isNotAnonymous>
                        </#if>

                    </div>
                    <div class="info-text mt-20">
                        <#if productShowItem.goodsType.name() == 'PHYSICAL' || productShowItem.goodsType.name() == 'VIRTUAL'>
                            <p class="tip-text">兑换成功后，拓天客服将会在7个工作日联系您发放商品</p>
                        <#else>
                            <p class="tip-text">兑换成功后，请前去“我的账户-我的宝藏”中进行查看</p>
                        </#if>
                    </div>
                </div>
            </div>
            <div>
                <p class="mater-img picture-item">
                    <img src="${commonStaticServer}/${productShowItem.webPictureUrl}"/>
                </p>
            </div>
        </div>
    </div>
    <div class="container-ad">
    </div>
</div>
</@global.main>