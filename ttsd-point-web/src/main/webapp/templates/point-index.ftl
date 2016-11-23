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
                        <a href="${webServer}/login?redirect=${pointServer}"
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
                    <a href="/point-shop/record">兑换记录></a> | <a href="/point-shop/bill">积分明细></a> | <a href="javascript:void(0)" class="point-rule-tip">积分规则></a>
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

                        <p class="intro-text">连续签到，积分翻倍送，最多每天可领<span>10</span>积分！</p>
                    </div>
                    <div class="sign-bottom">
                        <ul>
                            <li>
                                <p class="day-name">第1天</p>

                                <p class="day-beans">
                                    <span>2</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第2天</p>

                                <p class="day-beans">
                                    <span>3</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第3天</p>

                                <p class="day-beans">
                                    <span>4</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第4天</p>

                                <p class="day-beans">
                                    <span>5</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第5天</p>

                                <p class="day-beans">
                                    <span>10</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第6天</p>

                                <p class="day-beans">
                                    <span>10</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第7天</p>

                                <p class="day-beans">
                                    <span>10</span>
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
                        <li class="big-gift" data-href="/point-shop/${virtualProduct.id?c}/${virtualProduct.goodsType.name()}/detail">
                            <p class="num-text">剩余${virtualProduct.leftCount?c!"0"}件</p>

                            <p><img src="${staticServer}${virtualProduct.imageUrl}" width="160" height="100"></p>
                            <p class="convert-btn">
                                <span class="name-text">${virtualProduct.name!"0"}</span>
                                <span class="price-text">尊享价：<i>${virtualProduct.points?string('0')}</i>积分</span>
                                <#if virtualProduct?? && virtualProduct.leftCount ==0 >
                                    <span class="fl get-btn">已售罄</span>
                                <#else>
                                    <a class="fl"
                                       href="/point-shop/order/${virtualProduct.id?c}/${virtualProduct.goodsType.name()}/1"><span class="fl get-btn active">立即兑换</span></a>
                                </#if>
                            </p>
                        </li>
                    <#else>
                        <li data-href="/point-shop/${virtualProduct.id?c}/${virtualProduct.goodsType.name()}/detail">
                            <p class="num-text">剩余${virtualProduct.leftCount?c!"0"}件</p>

                            <p><img src="${staticServer}${virtualProduct.imageUrl}" width="160" height="100"></p>
                            <p class="convert-btn">
                                <span class="name-text">${virtualProduct.name}</span>
                                <span class="price-text">尊享价：<i>${virtualProduct.points?string('0')}</i>积分</span>
                                <#if virtualProduct?? && virtualProduct.leftCount ==0 >
                                    <span class="fl get-btn">已售罄</span>
                                <#else>
                                    <a class="fl"
                                       href="/point-shop/order/${virtualProduct.id?c}/${virtualProduct.goodsType.name()}/1"><span
                                            class="fl get-btn active">立即兑换</span></a>
                                </#if>
                            </p>
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
                        <li class="big-gift" data-href="/point-shop/${physicalProduct.id?c}/${physicalProduct.goodsType.name()}/detail">
                            <p class="num-text">剩余${physicalProduct.leftCount?c!"0"}件</p>

                            <p class="mater-img picture-item">
                                <img src="${staticServer}${physicalProduct.imageUrl}" width="160" height="100"/>
                            </p>

                            <p class="convert-btn">
                                <span class="name-text">${physicalProduct.name}</span>
                                <span class="price-text">尊享价：<i>${physicalProduct.points?string('0')}</i>积分</span>
                                <#if physicalProduct?? && physicalProduct.leftCount ==0 >
                                    <span class="fl get-btn">已售罄</span>
                                <#else>
                                    <a href="/point-shop/order/${physicalProduct.id?c}/${physicalProduct.goodsType.name()}/1"><span
                                            class="get-btn active">立即兑换</span></a>
                                </#if>
                            </p>
                        </li>
                    <#else>
                        <li data-href="/point-shop/${physicalProduct.id?c}/${physicalProduct.goodsType.name()}/detail">
                            <p class="num-text">剩余${physicalProduct.leftCount?c!"0"}件</p>

                            <p class="mater-img picture-item">
                                <img src="${staticServer}${physicalProduct.imageUrl}" width="160" height="100"/>
                            </p>

                            <p class="convert-btn">
                                <span class="name-text">${physicalProduct.name}</span>
                                <span class="price-text">尊享价：<i>${physicalProduct.points?string('0')}</i>积分</span>
                                <#if physicalProduct?? && physicalProduct.leftCount ==0 >
                                    <span class="fl get-btn">已售罄</span>
                                <#else>
                                    <a href="/point-shop/order/${physicalProduct.id?c}/${physicalProduct.goodsType.name()}/1"><span
                                            class="get-btn active">立即兑换</span></a>
                                </#if>
                            </p>
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
<div class="rule-info-tip">
    <h3 class="rule-title"><span>积分规则</span></h3>
    <p>
        <span>如何获取积分？</span>
    </p>
    <p>通过完成拓天速贷平台各项任务，即可获得相应积分。详情如下：</p>
    <p>1.连续签到：</p>
    <p>2.其他任务：</p>
    <p>在<span>积分商城</span>-><span>积分任务</span>中查看各项任务，完成后均可获得相应积分。</p>
    <p>3.投资赚积分：</p>
    <p>投资拓天速贷直投项目，投资年化积分=投资金额*投资天数/365。</p>
    <p><span>积分的用途</span></p>
    <p>用户可以用账户相应的积分进行商品兑换实物商品或优惠券或者抽奖。</p>
    <p>拓天速贷积分规则</p>
    <p>1.积分是拓天速贷平台发放的一种奖励，只适用于平台内指定用途，不可转让，不可兑现；</p>
    <p>2.积分有效期：10月22日0时-次年10月21日24时；平台于每年10月21日24时，对所有用     户账户内积分进行清零，清零后积分将重新累计；</p>
    <p>3.若发现存在作弊、刷积分的嫌疑，一经核实，拓天速贷有权回收相应积分；</p>
    <p>4.拓天速贷对平台内的积分规则享有最终解释权。</p>
</div>
</@global.main>