<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="customer-center" sideLab="platform-loan-list" title="借款申请信息">

<div class="col-md-7" style="text-align: center">
    <h3>借款申请-<#if (data.pledgeType)?? && data.pledgeType='VEHICLE'>车抵<#elseif (data.pledgeType)?? && data.pledgeType='HOUSE'>房抵<#else></#if></h3>
    <br/>
    <div style="line-height: 30px">
        <div style="text-align: left">
            <span style="display: inline-block; width: 50%"><h4>申请人基础信息</h4></span>
            <span style="display: inline-block; width: 49%"><h4>申请时间：<#if (data.createdTime)??>${data.createdTime?datetime!}</#if></h4></span>
        </div>
        <hr>
        <div style="text-align: left">
            <div>
                <span style="display: inline-block; width: 33%">姓名：${(data.userName)!""}</span>
                <span style="display: inline-block; width: 33%">身份证：<#if (data.identityNumber)??>${data.identityNumber?substring(0,6)}********${data.identityNumber?substring(14, 18)}</#if></span>
                <span style="display: inline-block; width: 33%">地址：${(data.address)!""}</span>
            </div>
            <div>
                <span style="display: inline-block; width: 33%">性别：${(data.sex)!""}</span>
                <span style="display: inline-block; width: 33%">年龄：${(data.age)!""}</span>
                <span style="display: inline-block; width: 33%">电话：${(data.mobile)!""}</span>
            </div>
        </div>
    </div>
    <br/>
    <div style="line-height: 30px">
        <div style="text-align: left">
            <span><h4>补充信息</h4></span>
        </div>
        <hr>
        <div style="text-align: left">
            <div>
                <span style="display: inline-block; width: 49%">婚姻状况：<#if data.isMarried?? && data.isMarried>已婚<#elseif data.isMarried?? && !data.isMarried>未婚</#if></span>
                <span style="display: inline-block; width: 50%">提供个人征信报告：<#if data.haveCreditReport?? && data.haveCreditReport>提供<#elseif data.haveCreditReport?? && !data.haveCreditReport>未提供</#if></span>
            </div>
            <div>
                <span style="display: inline-block; width: 49%">职位：${(data.workPosition)!""}</span>
                <span style="display: inline-block; width: 50%">芝麻信用分：${(data.sesameCredit)!""}</span>
            </div>
        </div>
    </div>
    <br/>
    <div style="line-height: 30px">
        <div style="text-align: left">
            <span><h4>借款申请信息</h4></span>
        </div>
        <hr>
        <div style="text-align: left">
            <div>
                <span style="display: inline-block; width: 33%">借款金额：${(data.amount)!""}万元</span>
                <span style="display: inline-block; width: 33%">借款周期：${(data.period)!""}个月</span>
                <span style="display: inline-block; width: 33%">家庭年收入：${(data.homeIncome)!""}万元</span>
            </div>
            <div>
                <span>借款用途：${(data.loanUsage)!""}</span>
            </div>
            <div>
                <span><#if (data.pledgeType)?? && data.pledgeType='VEHICLE'>车辆<#elseif (data.pledgeType)?? && data.pledgeType='HOUSE'>房产<#else></#if>信息：${(data.pledgeInfo)!""}</span>
            </div>
            <div>
                <span>其他资产：${(data.elsePledge)!""}</span>
            </div>
        </div>
    </div>
    <br/>
    <div style="line-height: 30px">
        <hr>
        <div>
            <div style="text-align: left">
                <span>备注：${(data.comment)!"暂无备注"}</span>
            </div>
        </div>
    </div>

</div>

</@global.main>