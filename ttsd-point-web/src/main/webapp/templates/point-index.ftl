<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.point_index}" pageJavascript="${js.point_index}" activeNav="积分商城" activeLeftNav="" title="积分商城">

<div class="global-member-store">
    <div class="store-top">
        <div class="store-login">
            <#if isLogin>
                <div class="user-info">
                    <h3>我的积分</h3>

                    <p>${userPoint}</p>
                    <#if isSignIn>
                        <p><span class="already">已签到</span></p>
                    <#else>
                        <p><span data-url="/point-shop/signIn" id="signBtn">签到</span></p>
                    </#if>
                </div>
            <#else>
                <div class="login-model">
                    <p>
                        <a href="${webServer}/login?redirect=${pointServer}/point-shop"
                           class="login-btn">立即登录</a>
                    </p>

                    <p>
                        <a href="${webServer}/register/user" class="regist-btn">新用户注册></a>
                    </p>
                </div>
            </#if>

            <ul class="other-list">
                <li class="right-line">
                    <p><span>做任务赚积分</span></p>

                    <p><a href="/point-shop/task">去做任务</a></p>
                </li>
                <li>
                    <p><span>投资赚积分</span></p>

                    <p><a href="${webServer}/loan-list">去投资</a></p>
                </li>
            </ul>
            <#if isLogin>
                <div class="user-trade">
                    <a href="/point-shop/record">兑换记录></a> | <a href="/point-shop/bill">积分明细></a>
                </div>
            </#if>
            <div class="sign-layer" id="signLayer">
                <div class="sign-layer-list">
                    <div class="sign-top">
                        <div class="close-btn" id="closeSign"></div>
                        <p class="sign-text">签到成功，领取5积分！</p>

                        <p class="tomorrow-text">明日可领10积分</p>

                        <p class="img-beans">
                            <img src="${staticServer}/point/images/sign-beans.png"/>
                            <span class="add-dou">
                                +5
                            </span>
                        </p>

                        <p class="intro-text">连续签到，积分翻倍送，最多每天可领<span>80</span>积分！</p>
                    </div>
                    <div class="sign-bottom">
                        <ul>
                            <li>
                                <p class="day-name">第1天</p>

                                <p class="day-beans">
                                    <span>5</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第2天</p>

                                <p class="day-beans">
                                    <span>10</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第3天</p>

                                <p class="day-beans">
                                    <span>20</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第4天</p>

                                <p class="day-beans">
                                    <span>40</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第5天</p>

                                <p class="day-beans">
                                    <span>80</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第6天</p>

                                <p class="day-beans">
                                    <span>80</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第7天</p>

                                <p class="day-beans">
                                    <span>80</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li class="last-day">
                                <p class="day-name">第N天</p>

                                <p class="day-beans">
                                    <span>...</span>
                                </p>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="store-title">
        <span>
            <i class="title-left"></i>
            虚拟商品
            <i class="title-right"></i>
        </span>
    </div>
    <div class="store-material">
        <div class="wp clearfix">
            <#if (virtualProducts?size > 0)>
            <ul class="material-list">
                <#list virtualProducts as virtualProduct>
                    <#if virtualProduct_index < 2>
                        <li class="big-gift">
                            <a href="/point-shop/${virtualProduct.id?c}/${virtualProduct.itemType.name()}/detail">
                                <p class="num-text">剩余${virtualProduct.leftCount?c!"0"}件</p>
                                <#if virtualProduct.itemType.name() == 'RED_ENVELOPE'>
                                    <p class="mater-img bag-bg">
                                        <span><i><@amount>${virtualProduct.pictureDescription!"0"}</@amount></i>元</span>
                                    </p>
                                <#elseif virtualProduct.itemType.name() == 'INVEST_COUPON'>
                                    <p class="mater-img coupon-bg">
                                        <span><i><@amount>${virtualProduct.pictureDescription!"0"}</@amount></i>元</span>
                                        <span>投资体验券</span>
                                    </p>
                                <#elseif virtualProduct.itemType.name() == 'INTEREST_COUPON'>
                                    <p class="mater-img jia-bg">
                                        <span><i><@amount>${virtualProduct.pictureDescription!"0"}</@amount></i>%</span>
                                        <span>加息券</span>
                                    </p>
                                <#elseif virtualProduct.itemType.name() == 'VIRTUAL'>
                                    <p class="mater-img picture-item">
                                        <img src="/${virtualProduct.imageUrl}" width="160" height="100"/>
                                    </p>
                                </#if>
                                <p class="convert-btn">
                                    <span class="name-text">${virtualProduct.pictureDescription!"0"}</span>
                                    <span class="price-text">尊享价：<i>${virtualProduct.points?string('0')}</i>积分</span>
                                    <a class="fl"
                                       href="/point-shop/order/${virtualProduct.id?c}/${virtualProduct.itemType.name()}/1"><span
                                            class="fl get-btn">立即兑换</span></a>
                                </p>
                            </a>
                        </li>
                    <#else>
                        <li>
                            <a href="/point-shop/${virtualProduct.id?c}/${virtualProduct.itemType.name()}/detail">
                                <p class="num-text">剩余${virtualProduct.leftCount?c!"0"}件</p>
                                <#if virtualProduct.itemType == 'RED_ENVELOPE'>
                                    <p class="mater-img bag-bg">
                                        <span><i><@amount>${virtualProduct.pictureDescription!"0"}</@amount></i>元</span>
                                    </p>
                                <#elseif virtualProduct.itemType == 'INVEST_COUPON'>
                                    <p class="mater-img coupon-bg">
                                        <span><i><@amount>${virtualProduct.pictureDescription!"0"}</@amount></i>元</span>
                                        <span>投资体验券</span>
                                    </p>
                                <#elseif virtualProduct.itemType == 'INTEREST_COUPON'>
                                    <p class="mater-img jia-bg">
                                        <span><i>${virtualProduct.pictureDescription!"0"}</i>%</span>
                                        <span>加息券</span>
                                    </p>
                                <#elseif virtualProduct.itemType == 'VIRTUAL'>
                                    <p class="mater-img picture-item">
                                        <img src="/${virtualProduct.imageUrl}" width="160" height="100"/>
                                    </p>
                                </#if>
                                <p class="convert-btn">
                                    <span class="name-text">${virtualProduct.name}</span>
                                    <span class="price-text">尊享价：<i>${virtualProduct.points?string('0')}</i>积分</span>
                                    <a class="fl"
                                       href="/point-shop/order/${virtualProduct.id?c}/${virtualProduct.itemType.name()}/1"><span
                                            class="fl get-btn">立即兑换</span></a>
                                </p>
                            </a>
                        </li>
                    </#if>
                </#list>
            </ul>
            <#else>
                <p class="no-product">商品即将上线，敬请期待！</p>
            </#if>
        </div>
    </div>
    <div class="store-title">
        <span>
            <i class="title-left"></i>
            实物商品
            <i class="title-right"></i>
        </span>
    </div>
    <div class="store-material">
        <div class="wp clearfix">
            <#if (physicalProducts?size > 0)>
            <ul class="material-list">
                <#list physicalProducts as physicalProduct>
                    <#if physicalProduct_index < 2>
                        <li class="big-gift">
                            <a href="/point-shop/${physicalProduct.id?c}/${physicalProduct.itemType.name()}/detail">
                                <p class="num-text">剩余${physicalProduct.leftCount?c!"0"}件</p>

                                <p class="mater-img picture-item">
                                    <img src="/${physicalProduct.imageUrl}" width="160" height="100"/>
                                </p>

                                <p class="convert-btn">
                                    <span class="name-text">${physicalProduct.name}</span>
                                    <span class="price-text">尊享价：<i>${physicalProduct.points?string('0')}</i>积分</span>
                                    <a href="/point-shop/order/${physicalProduct.id?c}/${physicalProduct.itemType.name()}/1"><span
                                            class="get-btn">立即兑换</span></a>
                                </p>
                            </a>
                        </li>
                    <#else>
                        <li>
                            <a href="/point-shop/${physicalProduct.id?c}/${physicalProduct.itemType.name()}/detail">
                                <p class="num-text">剩余${physicalProduct.leftCount?c!"0"}件</p>

                                <p class="mater-img picture-item">
                                    <img src="/${physicalProduct.imageUrl}" width="160" height="100"/>
                                </p>

                                <p class="convert-btn">
                                    <span class="name-text">${physicalProduct.name}</span>
                                    <span class="price-text">尊享价：<i>${physicalProduct.points?string('0')}</i>积分</span>
                                    <a href="/point-shop/order/${physicalProduct.id?c}/${physicalProduct.itemType.name()}/1"><span
                                            class="get-btn">立即兑换</span></a>
                                </p>
                            </a>
                        </li>
                    </#if>
                </#list>
            </ul>
            <#else>
                <p class="no-product">商品即将上线，敬请期待！</p>
            </#if>
        </div>
    </div>
</div>
<div class="error-tip" id="errorTip"></div>
<script type="text/html" id="errorTipTpl">
    <h3>温馨提示</h3>
    <p>{{message}}</p>
    <a href="${webServer}/register/account?redirect=/point-shop" class="go-to">去认证</a>
</script>
</@global.main>