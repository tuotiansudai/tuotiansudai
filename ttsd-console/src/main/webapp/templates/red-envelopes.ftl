<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="coupons.js" headLab="activity-manage" sideLab="statisticsCoupon" title="现金红包管理">

<!-- content area begin -->
<div class="col-md-10">

    <div class="col-md-12" style="margin-bottom: 40px">
        <a href="/activity-manage/coupons" class="btn btn-default" style="margin-right: 60px">体验券</a>
        <a href="/activity-manage/interest-coupons" class="btn btn-default" style="margin-right: 60px">加息券</a>
        <a href="/activity-manage/red-envelopes" class="btn btn-default btn-warning" style="margin-right: 60px">现金红包</a>
        <a href="/activity-manage/birthday-coupons" class="btn btn-default">生日月</a>
    </div>

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
    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
<div class="table-responsive" style="width: 100%">
<table class="table table-bordered table-hover ">
    <thead>
    <tr>
        <th>
            名称
        </th>
        <th>
            来源描述
        </th>
        <th>
            金额(元)
        </th>
        <th>
            活动期限
        </th>
        <th>
            有效天数
        </th>
        <th>
            发放对象
        </th>
        <th>
            预计发放数量(张)
        </th>
        <th>
            可投标的
        </th>
        <th>
            使用条件
        </th>
        <th>
            是否共用
        </th>
        <th>
            已发放(张)
        </th>
        <th>
            已使用(张)
        </th>
        <th>
            总投资金额(元)
        </th>
        <th>
            应发放总收益(元)
        </th>
        <th>
            已发放收益(元)
        </th>
        <th>
            备注
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
            <span class="add-tooltip" data-placement="top" data-toggle="tooltip"
                  data-original-title="${coupon.couponType.getName()}">${coupon.couponType.getName()}</span>
        </td>
        <td>
        ${coupon.couponSource}
        </td>
        <td>
        ${coupon.amount}
        </td>
        <td>
        ${coupon.startTime?string('yyyy-MM-dd')}至${coupon.endTime?string('yyyy-MM-dd')}
        </td>
        <td>
        ${coupon.deadline?string('0')}天
        </td>
        <td>
            <#if coupon.userGroup == 'IMPORT_USER'>
                <a href="javascript:void(0)" data-url="/activity-manage/coupon/${coupon.id?string('0')}/redis"
                   class="detail-redis <#if coupon.importIsRight??&&coupon.importIsRight>text-blue<#else>text-red</#if>">查看详情</a>
            <#elseif coupon.userGroup == "EXCHANGER_CODE">
                <a href="/activity-manage/coupon/${coupon.id?c}/exchange-code"
                   class="btn-link">${coupon.userGroup.getDescription()}</a>
            <#else>
            ${coupon.userGroup.getDescription()}
            </#if>
        </td>
        <td>
        ${coupon.totalCount?c}
        </td>
    <td>
        <#list coupon.productTypes as productType>
        ${productType.getName()}<#sep>, </#sep>
</#list>
</td>
    <td>
    ${coupon.investLowerLimit}
    </td>
    <td>
    <#if coupon.shared>是<#else>否</#if>
    </td>
    <td>
    ${coupon.issuedCount?string('0')}
    </td>
    <td>
    ${coupon.usedCount?string('0')}
    </td>
    <td>
    ${coupon.totalInvestAmount/100}
    </td>
    <td>
    ${coupon.expectedAmount/100}
    </td>
    <td>
    ${coupon.actualAmount/100}
    </td>
    <td>
    ${coupon.comment!}
    </td>
    <td>
    <#if coupon.deleted>
        已删除
    <#else>
    <#if coupon.active>
        -
    <#else>
    <@security.authorize access="hasAuthority('OPERATOR_ADMIN')">
        -
    </@security.authorize>
    <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
        <#if !(coupon.activatedTime??)>
            <a href="/activity-manage/coupon/${coupon.id?string('0')}/edit" class="btn-link">编辑</a> /
            <button class="btn-link coupon-delete" data-link="/activity-manage/coupon/${coupon.id?string('0')}">删除</button>
        <#else >
            -
        </#if>
    </@security.authorize>
    </#if>
    </#if>
    </td>
    <td>
    <#if coupon.deleted>
        -
    <#else>
    <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
    <#if coupon.active>
        <label>
            <i class="check-btn add-check"></i>
            <button class="loan_repay already-btn btn-link inactive-btn" data-id="${coupon.id?string('0')}"
                    data-type="${coupon.couponType}">已生效
            </button>
        </label>
    <#else>
        <label>
            <i class="check-btn"></i>
            <a class="loan_repay confirm-btn" href="javascript:void(0)" data-id="${coupon.id?string('0')}"
               data-type="${coupon.couponType}">确认生效</a>
        </label>
    </#if>
    </@security.authorize>
    <@security.authorize access="hasAuthority('OPERATOR')">
        -
    </@security.authorize>
    </#if>
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
        <@security.authorize access="hasAnyAuthority('DATA')">
            <button class="btn btn-default pull-left export-red-envelopes" type="button">导出Excel</button>
        </@security.authorize>
        </ul>
    </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>