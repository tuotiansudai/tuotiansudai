<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.my_treasure}" activeNav="我的账户" activeLeftNav="我的宝藏" title="我的宝藏">

<div class="content-container my-treasure-content">
    <div class="rule-list" id="ruleList">
        <div class="rule-com">
            <div class="close-icon close-btn"></div>
            <h3><span>使用规则</span></h3>
            <dl>
                <dt>体验券使用规则：</dt>
                <dd>1. 体验券仅适用于标的投资；</dd>
                <dd>2. 体验结束后系统自动收回本金，收益转回用户账户内，详见“我的账户”-->“资金管理”；</dd>
                <dd>3. 如体验券中有限制条件， 用户必须按照限制条件使用。</dd>
            </dl>
            <dl>
                <dt>加息券使用规则：</dt>
                <dd>1. 加息券仅适用于标的投资；</dd>
                <dd>2. 标的回款后，加息券所得收益转回用户账户内，详见“我的账户”-->“资金管理”；</dd>
                <dd>3. 如加息券中有限制条件， 用户必须按照限制条件使用；</dd>
                <dd>4. 使用方式：在“我要投资”-->“标的详情”页面选择使用加息券。</dd>
            </dl>
            <dl>
                <dt>红包使用规则：</dt>
                <dd>1. 在投资过程中使用红包，投资成功放款后即可返现；</dd>
                <dd>2. 现金红包不可与平台其他优惠券同时使用（3元现金红包除外）；</dd>
                <dd>3. 投资成功放款后用户获得的现金可在“我的账户”中查询，提现；</dd>
                <dd>4. 如红包有使用条件，用户需要按照条件使用。</dd>
            </dl>
            <div class="close-text">
                <span class="close-btn">我已了解</span>
            </div>
        </div>
    </div>
    <h4 class="column-title">
        <em class="tc title-navli active">我的宝藏</em>
        <span class="rule-show">使用规则<i class="fa fa-question-circle text-b"></i></span>
    </h4>
    <ul class="filters-list">
        <li class="active">未使用</li>
        <li>已使用</li>
        <li>已过期</li>
        <div class="get-coupon-code">
            <label for="couponByCode">
                <span>输入兑换码：</span>
                <input type="text" class="coupon-by-code" id="couponByCode" maxlength="14">
                <button type="button" class="btn btn-primary submit-code" id="submitCode">兑换</button>
            </label>
        </div>
    </ul>
    <div class="model-list">

    <#-- 未使用的优惠券 -->
        <div class="coupon-com active">
            <ul class="coupon-list">
                <#list unusedCoupons as coupon>
                    <#assign unusedCouponClass = ''>
                    <#if coupon.couponType == 'RED_ENVELOPE'>
                        <#if coupon.userGroup == 'EXCHANGER_CODE'>
                            <#assign unusedCouponClass = 'yellow-type-get'>
                        <#else>
                            <#assign unusedCouponClass = 'yellow-type'>
                        </#if>
                    <#elseif coupon.couponType == 'NEWBIE_COUPON' || coupon.couponType == 'INVEST_COUPON'>
                        <#if coupon.userGroup == 'EXCHANGER_CODE'>
                            <#assign unusedCouponClass = 'new-type-get'>
                        <#else>
                            <#assign unusedCouponClass = 'new-type'>
                        </#if>
                    <#elseif coupon.couponType == 'INTEREST_COUPON'>
                        <#if coupon.userGroup == 'EXCHANGER_CODE'>
                            <#assign unusedCouponClass = 'bite-type-get'>
                        <#else>
                            <#assign unusedCouponClass = 'bite-type'>
                        </#if>
                    </#if>
                    <li class="${unusedCouponClass}">
                        <div class="top-com">
                            <div class="left-name">
                                <span>${coupon.couponType.getName()}</span>
                                <em></em>
                                <i class="circle-top"></i>
                                <i class="circle-bottom"></i>
                            </div>
                            <div class="right-coupon">
                                <p class="mt-10">
                                    <#if coupon.couponType == 'RED_ENVELOPE' || coupon.couponType == 'NEWBIE_COUPON' ||coupon.couponType == 'INVEST_COUPON'>
                                        <span class="num-text">${(coupon.couponAmount/100)?string("0")}</span>
                                        <span class="unit-text">元</span>
                                    <#elseif coupon.couponType == 'INTEREST_COUPON'>
                                        <span class="num-text">+${coupon.rate*100?float}%</span>
                                        <span class="unit-text">年化收益</span>
                                    </#if>
                                </p>

                                <p>
                                    <#if coupon.couponType == 'RED_ENVELOPE'>
                                        <#if coupon.investLowerLimit == 0>
                                            ［投资成功放款返现］
                                        <#else>
                                            ［单笔投资满 <@amount>${coupon.investLowerLimit?string(0)}</@amount> 元可用］
                                        </#if>
                                    <#elseif coupon.couponType == 'NEWBIE_COUPON'>
                                        ［在拓天平台投资可用］
                                    <#elseif coupon.couponType == 'INVEST_COUPON'>
                                        ［单笔投资满 <@amount>${coupon.investLowerLimit?string(0)}</@amount> 元可用］
                                    <#elseif coupon.couponType == 'INTEREST_COUPON'>
                                        <#if coupon.investUpperLimit == 0>
                                            ［投资即可使用］
                                        <#else>
                                            ［限投资 <@amount>${coupon.investUpperLimit?string(0)}</@amount> 元以内可用］
                                        </#if>
                                    </#if>
                                </p>

                                <p>产品限制：
                                    <#list coupon.productTypeList as productType>
                                        <#if productType == 'SYL'><i class="pro-icon">速<em class="bg-com"></em><em
                                                class="circle-com"></em></i>
                                        <#elseif productType == 'WYX'><i class="pro-icon">稳<em class="bg-com"></em><em
                                                class="circle-com"></em></i>
                                        <#elseif productType == 'JYF'><i class="pro-icon">久<em class="bg-com"></em><em
                                                class="circle-com"></em></i>
                                        </#if>
                                    </#list>
                                    产品线可用
                                </p>
                            </div>
                        </div>
                        <div class="bottom-time">
                            <span>请在 ${coupon.endTime?date} 前使用</span>
                            <a href="/loan-list">立即使用</a>
                        </div>
                    </li>
                <#else>
                    <p class="no-treasure-tip tc pad-m">您现在没有可用的优惠券。</p>
                </#list>
            </ul>
        </div>

        <#-- 已使用的优惠券 -->
        <div class="coupon-com">
            <ul class="coupon-list used-card">
                <#list useRecords as record>
                    <#assign usedCouponClass = ''>
                    <#if record.couponType == 'RED_ENVELOPE'>
                        <#if record.userGroup == 'EXCHANGER_CODE'>
                            <#assign usedCouponClass = 'yellow-type-get'>
                        <#else>
                            <#assign usedCouponClass = 'yellow-type'>
                        </#if>
                    <#elseif record.couponType == 'NEWBIE_COUPON' || record.couponType == 'INVEST_COUPON'>
                        <#if record.userGroup == 'EXCHANGER_CODE'>
                            <#assign usedCouponClass = 'new-type-get'>
                        <#else>
                            <#assign usedCouponClass = 'new-type'>
                        </#if>
                    <#elseif record.couponType == 'INTEREST_COUPON'>
                        <#if record.userGroup == 'EXCHANGER_CODE'>
                            <#assign usedCouponClass = 'bite-type-get'>
                        <#else>
                            <#assign usedCouponClass = 'bite-type'>
                        </#if>
                    </#if>
                    <li class="${usedCouponClass}">
                        <div class="top-com">
                            <div class="left-name">
                                <span>${record.couponType.getName()}</span>
                                <em></em>
                                <i class="circle-top"></i>
                                <i class="circle-bottom"></i>
                            </div>
                            <div class="right-coupon">
                                <p class="mt-10">
                                    <#if record.couponType == 'RED_ENVELOPE' || record.couponType == 'NEWBIE_COUPON' ||record.couponType == 'INVEST_COUPON'>
                                        <span class="num-text">${(record.couponAmount/100)?string("0")}</span>
                                        <span class="unit-text">元</span>
                                    <#elseif record.couponType == 'INTEREST_COUPON'>
                                        <span class="num-text">+${record.rate*100?float}%</span>
                                        <span class="unit-text">年化收益</span>
                                    </#if>
                                </p>

                                <p>
                                    <#if record.couponType == 'RED_ENVELOPE'>
                                        ［投资成功放款返现］
                                    <#elseif record.couponType == 'NEWBIE_COUPON'>
                                        ［在拓天平台投资可用］
                                    <#elseif record.couponType == 'INVEST_COUPON'>
                                        ［单笔投资满 <@amount>${record.investLowerLimit?string(0)}</@amount> 元可用］
                                    <#elseif record.couponType == 'INTEREST_COUPON'>
                                        ［限投资 <@amount>${record.investUpperLimit?string(0)}</@amount> 元以内可用］
                                    <#elseif record.couponType == 'BIRTHDAY_COUPON'>
                                        ［仅限发券当月使用］
                                    </#if>
                                </p>

                                <p>产品限制：
                                    <#list record.productTypeList as productType>
                                        <#if productType == 'SYL'><i class="pro-icon">速<em class="bg-com"></em><em
                                                class="circle-com"></em></i>
                                        <#elseif productType == 'WYX'><i class="pro-icon">稳<em class="bg-com"></em><em
                                                class="circle-com"></em></i>
                                        <#elseif productType == 'JYF'><i class="pro-icon">久<em class="bg-com"></em><em
                                                class="circle-com"></em></i>
                                        </#if>
                                    </#list>
                                    产品线可用
                                </p>
                            </div>
                        </div>
                        <div class="detail-com">
                            <p class="bottom-line">
                                <span class="status-text">已使用</span>
                                <a href="/loan/${record.loanId?c}" class="see-detail">查看详情</a>
                            </p>

                            <p>
                                <span class="left-text">所投标的： ${record.loanName!}</span>
                                <span class="right-text">投资金额： ${(record.investAmount/100)?string("0")}元</span>
                            </p>

                            <p>
                                <span class="left-text">使用时间： ${record.usedTime?date}</span>
                                <span class="right-text">预计收益： ${(record.expectedIncome/100)?string("0")}元</span>
                            </p>
                        </div>
                    </li>
                <#else>
                    <p class="no-treasure-tip tc pad-m">您还没有使用过优惠券。</p>
                </#list>
            </ul>
        </div>

    <#-- 已过期的优惠券 -->
        <div class="coupon-com">
            <ul class="coupon-list">
                <#list expiredCoupons as coupon>
                    <li <#if coupon.userGroup == 'EXCHANGER_CODE'>class="default-type-get"</#if> >
                        <div class="top-com">
                            <div class="left-name">
                                <span>${coupon.couponType.getName()}</span>
                                <em></em>
                                <i class="circle-top"></i>
                                <i class="circle-bottom"></i>
                            </div>
                            <div class="right-coupon">
                                <p class="mt-10">
                                    <#if coupon.couponType == 'RED_ENVELOPE' || coupon.couponType == 'NEWBIE_COUPON' ||coupon.couponType == 'INVEST_COUPON'>
                                        <span class="num-text">${(coupon.couponAmount/100)?string("0")}</span>
                                        <span class="unit-text">元</span>
                                    <#elseif coupon.couponType == 'INTEREST_COUPON'>
                                        <span class="num-text">+${coupon.rate*100?float}%</span>
                                        <span class="unit-text">年化收益</span>
                                    </#if>
                                </p>

                                <p>
                                    <#if coupon.couponType == 'RED_ENVELOPE'>
                                        ［投资成功放款返现］
                                    <#elseif coupon.couponType == 'NEWBIE_COUPON'>
                                        ［在拓天平台投资可用］
                                    <#elseif coupon.couponType == 'INVEST_COUPON'>
                                        ［单笔投资满 <@amount>${coupon.investLowerLimit?string(0)}</@amount> 元可用］
                                    <#elseif coupon.couponType == 'INTEREST_COUPON'>
                                        ［限投资 <@amount>${coupon.investUpperLimit?string(0)}</@amount> 元以内可用］
                                    </#if>
                                </p>

                                <p>产品限制：
                                    <#list coupon.productTypeList as productType>
                                        <#if productType == 'SYL'><i class="pro-icon">速<em class="bg-com"></em><em
                                                class="circle-com"></em></i>
                                        <#elseif productType == 'WYX'><i class="pro-icon">稳<em class="bg-com"></em><em
                                                class="circle-com"></em></i>
                                        <#elseif productType == 'JYF'><i class="pro-icon">久<em class="bg-com"></em><em
                                                class="circle-com"></em></i>
                                        </#if>
                                    </#list>
                                    产品线可用
                                </p>
                            </div>
                        </div>
                        <div class="bottom-time">
                            <span>过期时间：${coupon.endTime?date}</span>
                            <a href="javascript:void(0)">立即使用</a>
                        </div>
                    </li>
                <#else>
                    <p class="no-treasure-tip tc pad-m">您现在没有已过期的优惠券。</p>
                </#list>
            </ul>
        </div>

        <div class="record-tab">
            <div class="invest-list-birthday"></div>
            <div id="use-record-birthday" class="pagination" data-url="/coupon/use-record" data-page-size="10">
            </div>
        </div>
    </div>
    <#include "coupon-alert.ftl" />
</div>
</@global.main>