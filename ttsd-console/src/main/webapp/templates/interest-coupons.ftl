<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="interest-coupons.js" headLab="activity-manage" sideLab="statisticsInterestCoupon" title="加息券管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="see-detail">
        <table border="1"></table>
        <span class="close-span"><a href="#" class="close-btn">关闭</a></span>
    </div>
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
            利率
        </th>
        <th>
            总投资金额(元)
        </th>
        <th>
            有效期限
        </th>
        <th>
            发放对象
        </th>
        <th>
            预计发放数量(张)
        </th>
        <th>
            短信提醒
        </th>
        <th>
            实际发放张数(张)
        </th>
        <th>
            已使用(张)
        </th>
        <th>
            可投标的
        </th>
        <th>
            使用条件
        </th>
        <th>
            应发放总收益(元)
        </th>
        <th>
            已发放收益(元)
        </th>
        <th colspan="2">
            操作
        </th>
        <th>
        </th>
    </tr>
    </thead>
<tbody>
    <#list coupons as coupon>
    <tr>
        <td>

            <span class="add-tooltip" data-placement="top" data-toggle="tooltip" data-original-title="${coupon.couponType.getName()}">${coupon.couponType.getName()}</span>
        </td>
        <td>
        ${coupon.rate*100}%
        </td>
        <td>
        ${coupon.totalInvestAmount/100}
        </td>
        <td>
            <#if coupon.active>
                ${coupon.startTime?string('yyyy-MM-dd')}至${coupon.endTime?string('yyyy-MM-dd')}
            <#else>
                -
            </#if>
        </td>
        <td>
            <#if coupon.userGroup == 'IMPORT_USER'>
                <a href="javascript:void(0)" data-url="/activity-manage/coupon/${coupon.id?string('0')}/redis" class="detail-redis <#if coupon.importIsRight??&&coupon.importIsRight>text-blue<#else>text-red</#if>">查看详情</a>
            <#else>
                ${coupon.userGroup.getDescription()}
            </#if>
        </td>
        <td <#if coupon.importIsRight??&&!coupon.importIsRight>class="text-red" </#if>>
            ${coupon.totalCount?string('0')}<#if coupon.importIsRight??&&!coupon.importIsRight>!</#if>
        </td>
        <td>
            <#if coupon.smsAlert>是<#else>否</#if>
        </td>
        <td>
            ${coupon.issuedCount?string('0')}
        </td>
        <td>
            ${coupon.usedCount?string('0')}
        </td>
        <td>
            <#list coupon.productTypes as productType>
            ${productType.getName()}<#sep>, </#sep>
            </#list>
        </td>
        <td>
            投资满${coupon.investLowerLimit}元
        </td>
        <td>
        ${coupon.expectedAmount/100}
        </td>
        <td>
        ${coupon.actualAmount/100}
        </td>
        <td>
        <#if coupon.active>
            -
        <#else>
            <@security.authorize access="hasAuthority('OPERATOR_ADMIN')">
                -
            </@security.authorize>
            <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                <a href="/activity-manage/coupon/${coupon.id?string('0')}/edit" class="btn-link">编辑</a> / <button class="btn-link coupon-delete" data-link="/activity-manage/coupon/${coupon.id?string('0')}" >删除</button>
            </@security.authorize>
        </#if>
        </td>
        <td>
            <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                <#if coupon.active>
                    <label>
                        <i class="check-btn add-check"></i>
                        <button class="loan_repay already-btn btn-link inactive-btn" <#if coupon.couponType != 'NEWBIE_COUPON'>disabled</#if> data-id="${coupon.id?string('0')}" data-type="${coupon.couponType}">已生效</button>
                    </label>
                <#else>
                    <label>
                        <i class="check-btn"></i>
                        <a class="loan_repay confirm-btn" href="javascript:void(0)" data-id="${coupon.id?string('0')}" data-type="${coupon.couponType}">确认生效</a>
                    </label>
                </#if>
            </@security.authorize>
            <@security.authorize access="hasAuthority('OPERATOR')">
                -
            </@security.authorize>
        </td>
        <td>
            <a href="/activity-manage/coupon/${coupon.id?string('0')}/detail" class="btn-link">查看详情</a>
        </td>
    </tr>
</#list>
</tbody>
</table>
</div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${couponsCount}条,每页显示${pageSize}条</span>
        </div>
    <#if coupons?has_content>
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
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>