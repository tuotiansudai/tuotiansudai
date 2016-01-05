<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="coupons.js" headLab="activity-manage" sideLab="statisticsCoupon" title="体验券数据统计">

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
                金额(元)
            </th>
            <th>
                开始日期
            </th>
            <th>
                结束日期
            </th>
            <th>
                可发放(张)
            </th>
            <th>
                已发放(张)
            </th>
            <th>
                已使用(张)
            </th>
            <th>
                发放对象
            </th>
            <th>
                使用条件
            </th>
            <th>
                预估总收益(元)
            </th>
            <th>
                已回款总收益(元)
            </th>
            <th>
                操作
            </th>
        </tr>
        </thead>
        <tbody>
        <#list coupons as coupon>
        <tr>
            <td>
                <span class="add-tooltip" data-placement="top" data-toggle="tooltip" data-original-title=""></span>
            </td>
            <td>
                ${coupon.amount/100}
            </td>
            <td>
                ${(coupon.startTime?string('yyyy-MM-dd HH:mm'))!}
            </td>
            <td>
                ${(coupon.endTime?string('yyyy-MM-dd HH:mm'))!}
            </td>
            <td>
                ${coupon.totalCount?string('0')}
            </td>
            <td>
                ${coupon.issuedCount?string('0')}
            </td>
            <td>
                ${coupon.usedCount?string('0')}
            </td>
            <td>
                新注册用户
            </td>
            <td>
                所有标的
            </td>
            <td>
                ${coupon.expectedAmount/100}
            </td>
            <td>
                ${coupon.actualAmount/100}
            </td>
            <td>
                <a class="loan_repay " href="/activity-manage/coupon/${coupon.id?string('0')}/edit" data-id="${coupon.id?string('0')}">编辑</a>
                <#if coupon.active>
                    <label>
                        <i class="check-btn add-check"></i>
                        <a class="loan_repay confirm-btn already-btn" href="javascript:void(0)" data-id="${coupon.id?string('0')}">已生效</a>
                    </label>
                <#else>
                <label>
                    <i class="check-btn"></i>
                    <a class="loan_repay confirm-btn" href="javascript:void(0)" data-id="${coupon.id?string('0')}">确认生效</a>
                </label>
                </#if>
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