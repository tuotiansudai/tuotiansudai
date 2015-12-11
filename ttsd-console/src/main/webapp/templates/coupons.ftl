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
        <tr>
            <td>
                <span class="add-tooltip" data-placement="top" data-toggle="tooltip" data-original-title="201512新手活动体验券">201512新手活动体验券</span>
            </td>
            <td>
                2000
            </td>
            <td>
                2015-11-12
            </td>
            <td>
                2016-11-12
            </td>
            <td>
                3000
            </td>
            <td>
                2000
            </td>
            <td>
                1000
            </td>
            <td>
                新注册用户
            </td>
            <td>
                所有标的
            </td>
            <td>
                0
            </td>
            <td>
                0
            </td>
            <td>
                <a class="loan_repay confirm-btn" href="javascript:void(0)" data-id="111">确认生效</a>
            </td>
        </tr>
        </tbody>
        </table>
    </div>
    <!-- pagination  -->
    <nav>
    <div>
        <span class="bordern">总共20条,每页显示10条</span>
    </div>
    <#if loanlistdtos?has_content>
    <ul class="pagination">
        <li>
        <#if haspreviouspage>
        <a href="?status=${status!}&currentPageNo=${currentPageNo-1}&pageSize=${pageSize}&loanId=${loanId!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&loanName=${loanName!}" aria-label="Previous">
        <#else>
        <a href="#" aria-label="Previous">
        </#if>
        <span aria-hidden="true">&laquo; Prev</span>
        </a>
        </li>
        <li><a>${currentPageNo}</a></li>
        <li>
        <#if hasnextpage>
        <a href="?status=${status!}&currentPageNo=${currentPageNo+1}&pageSize=${pageSize}&loanId=${loanId!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&loanName=${loanName!}" aria-label="Next">
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