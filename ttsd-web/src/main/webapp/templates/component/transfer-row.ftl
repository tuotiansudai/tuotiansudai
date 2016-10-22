<div class="target-category-box" data-url="/transfer/${loan.transferApplicationId}">
    <div class="target-column-con">
        <#--项目名称-->
        <div class="table-row">${loan.name}</div>
            <#--预期年化收益-->
        <div class="table-row">
            <em class="percent-number">
                <i><@percentInteger>${loan.baseRate}</@percentInteger><@percentFraction>${loan.baseRate}</@percentFraction></i>%
            </em>
        </div>
            <#--转让价格-->
        <div class="table-row">
            <#--<em class="key-"-->
                <i class="number"><@percentInteger>${loan.transferAmount}</@percentInteger><@percentFraction>${loan.transferAmount}</@percentFraction></i>元
        </div>
            <#--项目本金-->
        <div class="table-row">
            <i class="number"><@percentInteger>${loan.investAmount}</@percentInteger><@percentFraction>${loan.investAmount}</@percentFraction></i>
            元

        </div>
            <#--剩余期数-->
        <div class="table-row">
            <i class="period">${loan.leftPeriod}</i>
        </div>
        <div class="table-row btn-col">

            <#if loan.transferStatus=='TRANSFERRING'>
                <button class="btn-invest btn-normal">马上投资</button>
            <#else>
                <button class="btn-invest btn-normal" disabled>已转让</button>
            </#if>
        </div>
    </div>
</div>