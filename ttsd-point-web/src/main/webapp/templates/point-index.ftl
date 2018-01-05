<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.point_index}" pageJavascript="${js.point_index}" activeNav="积分商城" activeLeftNav="" title="积分商城">

<div class="global-member-store" id="pointContainer">
    <div class="store-top">
        <div class="store-login">
            <#if isLogin>
                <div class="user-info">
                    <h3>可用积分</h3>
                    <p>${userPoint}</p>
                    <#if isSignIn>
                        <p><span class="already" data-url="/point-shop/sign-in" id="signBtn">已签到</span></p>
                    <#else>
                        <p><span data-url="/point-shop/sign-in" id="signBtn">签到</span></p>
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
            <#if isLogin>
                <div class="sign-rule">
                    <span>已连续签到${signCount}天</span>
                    <a href="/activity/sign-check">签到规则></a>
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
                        <div class="sign-text"></div>
                        <div class="sign-content">
                            <p class="sign-point"><span></span>积分</p>
                            <p class="tomorrow-text"></p>
                            <p class="intro-text"></p>
                            <p class="next-text"></p>
                            <p class="sign-reward"><a href="/activity/sign-check">查看连续签到奖励</a></p>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="store-title">
        <span>
            <i class="title-left"></i>
            积分抽奖
            <i class="title-right"></i>
        </span>
    </div>
    <div class="store-material even">
        <div class="wp clearfix">
            <#include 'module/nine-lottery.ftl'/>
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
                    <li data-href="/point-shop/${virtualProduct.id?c}/${virtualProduct.goodsType.name()}/detail">
                        <p><img src="${commonStaticServer}/${virtualProduct.imageUrl}" width="160" height="100"></p>
                        <p class="convert-btn">
                            <span class="name-text">${virtualProduct.name}</span>
                            <span class="price-text">
                                尊享价：<i>${(((virtualProduct.points * discount * 100)/100)?round)?string('0')}积分</i>
                                <#if discount?? && discount?floor != 1>
                                    <i class="old-price">${virtualProduct.points?string('0')}积分</i>
                                </#if>
                            </span>
                            <#if virtualProduct?? && virtualProduct.leftCount ==0 >
                                <span class="get-btn">已售罄</span>
                            <#else>
                                <a href="/point-shop/order/${virtualProduct.id?c}/${virtualProduct.goodsType.name()}/1"><span
                                        class="get-btn active">立即兑换</span></a>
                            </#if>
                        </p>
                    </li>
                </#list>
            </ul>
            <#else>
                <p class="no-material"></p>
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
    <div class="store-material even">
        <div class="wp clearfix">
            <#if (physicalProducts?size > 0)>
            <ul class="material-list">
                <#list physicalProducts as physicalProduct>
                    <li data-href="/point-shop/${physicalProduct.id?c}/${physicalProduct.goodsType.name()}/detail">
                        <#if isLogin && isShowDiscount>
                            <i class="hot-icon">
                                <span>${discountShow!}</span>
                            </i>
                        </#if>

                        <p class="num-text">剩余${physicalProduct.leftCount?c!"0"}件
                            <#if physicalProduct.monthLimit!=0>
                                /每人每月最多可兑换${physicalProduct.monthLimit}个
                            </#if>
                        </p>

                        <p class="mater-img picture-item">
                            <img src="${commonStaticServer}/${physicalProduct.imageUrl}" width="160" height="100"/>
                        </p>

                        <p class="convert-btn">
                            <span class="name-text">${physicalProduct.name}</span>
                            <span class="price-text">尊享价：<i>${(((physicalProduct.points * discount * 100)/100)?round)?string('0')}积分</i>
                                <#if discount?? && discount?floor != 1>
                                    <i class="old-price">${physicalProduct.points?string('0')}积分</i>
                                </#if>
                            </span>
                            <#if physicalProduct?? && physicalProduct.leftCount ==0 >
                                <span class="get-btn">已售罄</span>
                            <#else>
                                <a href="/point-shop/order/${physicalProduct.id?c}/${physicalProduct.goodsType.name()}/1"><span
                                        class="get-btn active">立即兑换</span></a>
                            </#if>
                        </p>
                    </li>
                </#list>
            </ul>
            <#else>
                <p class="no-material"></p>
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
<div class="rule-info-tip" id="ruleInfoTip">
    <i class="close-btn"></i>
    <h3 class="rule-title"><span>积分规则</span></h3>
    <p>
        <span>如何获取积分？</span>
    </p>
    <p class="mb-15">通过完成拓天速贷平台各项任务，即可获得相应积分。详情如下：</p>
    <p>1.连续签到：</p>
    <table class="table table-border">
        <thead>
            <tr>
                <th>1~3天</th>
                <th>4~6天</th>
                <th>7~10天</th>
                <th>11~15天</th>
                <th>15天以上</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>10积分</td>
                <td>15积分</td>
                <td>20积分</td>
                <td>25积分</td>
                <td>30积分</td>
            </tr>
        </tbody>
    </table>
    <p>2.其他任务：</p>
    <p class="mb-15">在<span>积分商城</span>-><span>积分任务</span>中查看各项任务，完成后均可获得相应积分。</p>
    <p>3.投资赚积分：</p>
    <p class="mb-15">投资拓天速贷直投项目，投资年化积分=投资金额*投资天数/365。</p>
    <p><span>积分的用途</span></p>
    <p class="mb-15">用户可以用账户相应的积分进行商品兑换实物商品或优惠券或者抽奖。</p>
    <p><span>拓天速贷积分规则</span></p>
    <p>1.用户投资30天、90天、180天、360天抵押类债权，根据累计年化投资额，可获得等值的兑换积分。投资体验项目及债权转让不参与积分的累计，积分只 保留整数位，小数点后部分直接抹去；</p>
    <p>2.拓天速贷将于每年10月21日24时，对所有用户账户内积分进行清零，清零后积分将重新累计，逾期未使用的积分将自动作废；</p>
    <p>3.用户在积分商城中所兑换的红包、加息劵等虚拟奖品实时发放，用户可在电脑端“我的账户-我的宝藏”或App端“我的-优惠券”中查看；</p>
    <p>4.话费、爱奇艺会员、优酷会员、电影票、京东E卡、实物奖品将于活动结束后7个工作日内由客服联系发放，部分地区邮费自付，请保持注册手机畅通，以便客服人员与您联系；</p>
    <p>5.用户所获得的积分仅限拓天速贷平台内使用，不可折现，不同账户积分不可合并使用；</p>
    <p>6.实物商品兑换订单完成后不可以取消或修改，若无商品质量问题，不予办理商品退换货，如用户在签收时发现商品有质量问题请立即联系客服；</p>
    <p>7.在获取和使用积分过程中，如果出现违规行为（如作弊领取等），拓天速贷将取消您获得积分的资格，并有权撤销违规交易，收回活动中所得的积分（含已使用的积分及未使用的积分），必要时将追究法律责任；</p>
    <p class="mb-15">8.拓天速贷在法律范围内保留对活动的最终解释权。</p>
</div>
</@global.main>