<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.loan_list}" pageJavascript="${m_js.loan_list}" title="投资列表_投资产品_拓天速贷">

<div class="my-loan-content" id="loanList">
    <div class="menu-category">
        <span class="current"><a>直投项目</a></span>
        <span><a href="/m/transfer-list">转让项目</a></span>
    </div>

    <div id="wrapperOut" class="loan-list-frame">
        <div class="loan-list-content">
            <div class="category-box-main">
                <#list loanItemList as loanItem>

                <div class="target-category-box <#if loanItem.productType == 'EXPERIENCE'>newer-experience</#if>"
                     data-url="/m/loan/${loanItem.id?c}">

                    <b class="newer-title">${loanItem.name}
                        <#if loanItem.productType == 'EXPERIENCE'>
                            <i class="icon-sign">体验金投资</i>
                        </#if>
                        <#if loanItem.productType == 'NEWBIE'>
                            <i class="icon-sign">新手专享</i>
                        </#if>
                        <#if loanItem.activity?string("true","false") == "true">
                            <i class="icon-sign">${loanItem.activityDesc!}</i>
                        </#if>
                    </b>
                    <ul class="loan-info clearfix">
                        <li>
                        <span class="percent-number">
                            <i>
                                <#if loanItem.activityType == 'NEWBIE' && loanItem.interestCouponRate gt 0>

                                    <@percentInteger>${loanItem.baseRate+loanItem.activityRate}</@percentInteger>
                                    <@percentFraction>${loanItem.baseRate+loanItem.activityRate}</@percentFraction>
                                    +<@percentInteger>${loanItem.interestCouponRate}</@percentInteger><@percentFraction>${loanItem.interestCouponRate}</@percentFraction>
                                <#else>
                                    <@percentInteger>${loanItem.baseRate}</@percentInteger>
                                    <@percentFraction>${loanItem.baseRate}</@percentFraction>
                                    <#if (loanItem.extraRate > 0)>
                                        ~ <@percentInteger>${loanItem.baseRate + loanItem.extraRate}</@percentInteger><@percentFraction>${loanItem.extraRate}</@percentFraction>
                                    </#if>
                                    <#if (loanItem.activityRate > 0)>
                                        +<@percentInteger>${loanItem.activityRate}</@percentInteger><@percentFraction>${loanItem.activityRate}</@percentFraction>
                                    </#if>
                                </#if>
                            </i>%
                        </span>
                            <em class="note">预期年化收益</em>
                        </li>
                        <li>
                            <#if loanItem.productType != 'EXPERIENCE'>最长 </#if><em
                                class="duration-day">${loanItem.duration}</em> 天
                            <em class="note">项目期限</em>
                        </li>
                        <li>
                            <#if ['PREHEAT', 'RAISING']?seq_contains(loanItem.status)>
                                <a href="/m/loan/1" class="btn-invest btn-normal">立即投资</a>
                            <#else>
                                <i class="loan-status icon-sellout"></i>
                            </#if>
                        </li>
                    </ul>
                    <#if loanItem.productType != 'EXPERIENCE'>
                        <div class="table-row progress-column">
                            <div class="progress-bar">
                                <div class="process-percent">
                                    <div class="percent" style="width:${loanItem.progress}%">
                                    </div>
                                </div>
                            </div>
                            <#if ['PREHEAT', 'RAISING']?seq_contains(loanItem.status)>
                                <#if loanItem.status == 'PREHEAT'>
                                    <#if loanItem.preheatSeconds lte 1800>
                                        <span class="time">${loanItem.preheatSeconds?string.computer}以后可投资</span>
                                    <#else>
                                        <span class="time">${(loanItem.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}
                                            放标</span>
                                    </#if>
                                </#if>
                                <span class="p-title">可投金额：<i>${loanItem.alert}</i></span>
                            <#else>
                                <span class="p-title"><i>${loanItem.alert}</i></span>
                            </#if>
                        </div>
                    </#if>
                </div>
            </#list>
            </div>

            <div id="pullUp">
                <span class="pullUpLabel">上拉加载更多</span>
            </div>


        </div>


    </div>

</div>
</@global.main>
