<div class="target-category-box" data-url="/loan/${loan.id?c}">
    <div class="target-column-con">
        <div class="table-row">${loan.name}
        <#if loan.activity?string("true","false") == "true">
            <span class="arrow-tag-normal">
                  <i class="ic-left"></i>
                  <em> ${loan.activityDesc!}</em>
                 <i class="ic-right"></i>
            </span>
        </#if>
        </div>
        <div class="table-row">
            <em class="percent-number">
                <s class="app">预期年化收益</s>
            <#if loan.extraRate != 0>
            ${loan.baseRate + loan.activityRate}% ~ ${loan.baseRate + loan.activityRate + loan.extraRate * 100}%
            <#else>
                <i><@percentInteger>${loan.baseRate}</@percentInteger></i>%
                <#if loan.activityRate != 0>
                    <i class="sign-plus">+</i>${loan.activityRate!}%
                </#if>
            </#if>
            </em>
        </div>
        <div class="table-row"><i class="duration-day">${loan.duration}</i>天</div>
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
            <a href="javascript:void(0)" class="btn-invest btn-normal">
                <#if loan.preheatSeconds lte 1800>
                    <i class="time-clock"></i><strong
                        class="minute_show">00</strong><em>:</em><strong
                        class="second_show">00</strong>放标
                <#else>
                ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                </#if>
            </a>
        <#else>
            <button class="btn-normal" disabled="">已售罄</button>
        </#if>
        </div>
    </div>
</div>