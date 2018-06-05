<div class="target-category-box" data-url="/loan/${loan.id?c}">
    <div class="target-column-con">
        <div class="table-row">${loan.name}
        <#if loan.activity?string("true","false") == "true">
            <span class="loan-tag-vip">
                ${loan.activityDesc!}
            </span>
        </#if>
        </div>
        <div class="table-row">
            <em class="percent-number">
                <s class="app">约定年化利率</s>
                <#--<h1>${loan.baseRate}</h1>-->
                <#--<h1>${loan.activityRate}</h1>-->
            <#if loan.extraRate != 0>
            <i>${loan.baseRate + loan.activityRate}</i>% ~ <i>${loan.baseRate + loan.activityRate + loan.extraRate * 100}</i>%
            <#else>
                <i><@percentInteger>${loan.baseRate}</@percentInteger></i>%
                <#if loan.activityRate != 0>
                    <i class="sign-plus">+</i><i>${loan.activityRate!}</i>%
                </#if>
            </#if>
            </em>
        </div>
        <div class="table-row">最长<i class="duration-day">${loan.duration}</i>天</div>
        <div class="table-row progress-column">

            <div class="progress-bar">
                <span class="p-title">剩余金额：<i>${loan.availableInvestAmount}元</i></span>

                <div class="process-percent">
                    <div class="percent" style="width:${loan.progress}%">
                    </div>
                </div>
                <span class="point fr">${loan.progress?string("0.00")}%</span>
            </div>

        </div>
        <div class="table-row btn-col">

        <#if loan.status== 'RAISING'>
        <#--筹款-->
            <a href="javascript:void(0)" class="btn-invest btn-normal">立即购买</a>

        <#elseif loan.status == 'PREHEAT'>
            <a href="javascript:void(0)" class="btn-invest btn-normal preheat-btn">
                    <#if loan.preheatSeconds lte 1800>
                        <span class="preheat" data-time="${loan.preheatSeconds?string.computer}">
                            <i class="minute_show"></i>分
                            <i class="second_show"></i>秒后开标
                        </span>
                    <#else>
                    ${(loan.fundraisingStartTime?string("MM-dd HH时mm分"))!}开标
                    </#if>
            </a>
        <#else>
            <button class="btn-normal" disabled="">已售罄</button>
        </#if>
        </div>
    </div>
</div>