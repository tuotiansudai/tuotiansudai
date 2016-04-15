<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="coupon-exchanges.js" headLab="point-manage" sideLab="couponExchangeManage" title="优惠券兑换管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
<div class="table-responsive">
<table class="table table-bordered table-hover ">
    <thead>
    <tr>
        <th>
            名称
        </th>
        <th>
            券额(元)
        </th>
        <th>
            利率
        </th>
        <th>
            总数量
        </th>
        <th>
            已兑换数量
        </th>
        <th>
            所需财豆
        </th>
        <th>
            有效期限
        </th>
        <th>
            可投标的
        </th>
        <th>
            使用条件
        </th>
        <th>
            操作
        </th>
        <th>
        </th>
        <th>
        </th>
    </tr>
    </thead>
<tbody>
    <#list exchangeCoupons as exchangeCoupon>
    <tr>
        <td>
            <span class="add-tooltip" data-placement="top" data-toggle="tooltip" data-original-title="${exchangeCoupon.couponType.getName()}">${exchangeCoupon.couponType.getName()}</span>
        </td>
        <td>
            <#if exchangeCoupon.couponType == 'INVEST_COUPON'>
                ${exchangeCoupon.amount}
            <#else>
                -
            </#if>
        </td>
        <td>
            <#if exchangeCoupon.couponType == 'INTEREST_COUPON'>
                ${exchangeCoupon.rate*100}%
            <#else>
                -
            </#if>
        </td>
        <td>
            ${exchangeCoupon.totalCount?string('0')}
        </td>
        <td>
            ${exchangeCoupon.issuedCount?string('0')}
        </td>
        <td>
            ${exchangeCoupon.exchangePoint?string('0')}
        </td>
        <td>
            ${exchangeCoupon.startTime?string('yyyy-MM-dd')}至${exchangeCoupon.endTime?string('yyyy-MM-dd')}
        </td>
        <td>
            <#list exchangeCoupon.productTypes as productType>
                ${productType.getName()}<#sep>, </#sep>
            </#list>
        </td>
        <td>
            投资满${exchangeCoupon.investLowerLimit}元
        </td>
        <td>
            <#if exchangeCoupon.active>
                -
            <#else>
                <@security.authorize access="hasAuthority('OPERATOR_ADMIN')">
                    -
                </@security.authorize>
                <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                    <a href="/activity-manage/coupon-exchange/${exchangeCoupon.id?string('0')}/edit" class="btn-link">编辑</a>
                </@security.authorize>
            </#if>
        </td>
        <td>
            <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                <#if exchangeCoupon.active>
                    <label>
                        <i class="check-btn add-check"></i>
                        <button class="loan_repay already-btn btn-link inactive-btn" disabled data-id="${exchangeCoupon.id?string('0')}" >已生效</button>
                    </label>
                <#else>
                    <label>
                        <i class="check-btn"></i>
                        <a class="loan_repay confirm-btn" href="javascript:void(0)" data-id="${exchangeCoupon.id?string('0')}" >确认生效</a>
                    </label>
                </#if>
            </@security.authorize>
            <@security.authorize access="hasAuthority('OPERATOR')">
                -
            </@security.authorize>
        </td>
        <td>
            <a href="/activity-manage/coupon/${exchangeCoupon.id?string('0')}/detail" class="btn-link">查看详情</a>
        </td>
    </tr>
    </#list>
</tbody>
</table>
</div>

    <nav>
        <div>
            <span class="bordern">总共${exchangeCouponCount}条,每页显示${pageSize}条</span>
        </div>
    <#if exchangeCoupons?has_content>
        <ul class="pagination">
            <li>
            <#if hasPreviousPage>
                <a href="?index=${index-1}&pageSize=${pageSize}" aria-label="Previous">
                <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                        <span aria-hidden="true">&laquo; Prev</span>
                    </a>
            </li>
            <li><a>${index}</a></li>
            <li>
            <#if hasNextPage>
                <a href="?index=${index+1}&pageSize=${pageSize}" aria-label="Next">
                <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                        <span aria-hidden="true">Next &raquo;</span>
                    </a>
            </li>
        </ul>
    </#if>
    </nav>

</div>
<!-- content area end -->
</@global.main>