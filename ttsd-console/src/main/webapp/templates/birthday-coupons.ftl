<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="coupons.js" headLab="activity-manage" sideLab="statisticsCoupon" title="生日红包管理">

<!-- content area begin -->
<div class="col-md-10">

    <form action="/activity-manage/coupons-list" class="form-inline query-build" id="couponList">

        <div class="form-group">
            <label>优惠券类型</label>
            <select  name="couponType" id="operationType">
                <option value="INTEREST_COUPON" <#if couponType=="INTEREST_COUPON">selected</#if> >加息券</option>
                <option value="RED_ENVELOPE" <#if couponType=="RED_ENVELOPE">selected</#if> >投资红包</option>
                <option value="BIRTHDAY_COUPON" <#if couponType=="BIRTHDAY_COUPON">selected</#if> >生日月</option>
                <option value="EXPERIENCE" <#if couponType=="EXPERIENCE">selected</#if> >体验券</option>
            </select>
        </div>

        <div class="form-group">
            <label for="control-label">来源描述</label>
            <input type="text" id="couponSource" name="couponSource" class="form-control jq-loginName" value="${couponSource!}">
        </div>

        <div class="form-group">
            <label for="control-label">金额</label>
            <input type="text" id="amount" name="amount" class="form-control money" value="${amount!}">
        </div>

        <button class="btn btn-primary search" type="submit">查询</button>

    </form>

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
            翻倍倍数
        </th>
        <th>
            总投资金额(元)
        </th>
        <th>
            活动期限
        </th>
        <th>
            发放对象
        </th>
        <th>
            可投标的
        </th>
        <th>
            已使用(张)
        </th>
        <th>
            应发放总收益(元)
        </th>
        <th>
            已发放收益(元)
        </th>
        <th>
            推送提醒
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
    <#list coupons as coupon>
    <tr>
        <td>
                        <span class="add-tooltip" data-placement="top" data-toggle="tooltip"
                              data-original-title="${coupon.couponType.getName()}">${coupon.couponType.getName()}</span>
        </td>
        <td>
        ${(coupon.birthdayBenefit+1)!}
        </td>
        <td>
        ${coupon.totalInvestAmount/100}
        </td>
        <td>
        ${coupon.startTime?string('yyyy-MM-dd')}至${coupon.endTime?string('yyyy-MM-dd')}
        </td>
        <td>
        ${coupon.userGroup.getDescription()}
        </td>
    <td>
        <#list coupon.productTypes as productType>
        ${productType.getName()}<#sep>, </#sep>
</#list>
</td>
    <td>
    ${coupon.usedCount?string('0')}
    </td>
    <td>
    ${coupon.expectedAmount/100}
    </td>
    <td>
    ${coupon.actualAmount/100}
    </td>
    <td>是</td>
    <td>
    <#if coupon.deleted>
        已删除
    <#else>
    <#if coupon.active>
        -
    <#else>
        <#if !(coupon.activatedTime??)>
            <a href="/activity-manage/coupon/${coupon.id?string('0')}/edit" class="btn-link">编辑</a>
            /
            <button class="btn-link coupon-delete"
                    data-link="/activity-manage/coupon/${coupon.id?string('0')}">删除
            </button>
        <#else >
            -
        </#if>
    </#if>
    </#if>
    </td>
    <td>
    <#if coupon.deleted>
        -
    <#else>
    <#if coupon.active>
        <label>
            <i class="check-btn add-check"></i>
            <button class="loan_repay already-btn btn-link inactive-btn"
                    data-id="${coupon.id?string('0')}" data-type="${coupon.couponType}">已生效
            </button>
        </label>
    <#else>
        <label>
            <i class="check-btn"></i>
            <a class="loan_repay confirm-btn" href="javascript:void(0)"
               data-id="${coupon.id?string('0')}" data-type="${coupon.couponType}">确认生效</a>
        </label>
    </#if>
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
                    <a href="?couponType=${couponType!}&couponSource=${couponSource!}&amount=${amount!}&index=${index-1}&pageSize=${pageSize}" aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?couponType=${couponType!}&couponSource=${couponSource!}&amount=${amount!}&index=${index+1}&pageSize=${pageSize}" aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
        <@security.authorize access="hasAnyAuthority('DATA')">
            <button class="btn btn-default pull-left export-birthday-coupons" type="button">导出Excel</button>
        </@security.authorize>
        </ul>
    </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>