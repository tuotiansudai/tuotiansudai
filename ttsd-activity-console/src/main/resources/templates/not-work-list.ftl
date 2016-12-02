<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="no-work-list.js" headLab="activity-manage" sideLab="notWork" title="活动投资奖励管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/activity-console/activity-manage/not-work-list" method="get" class="form-inline query-build" id="rewardForm">
        <div class="form-group">
            <label>用户手机号</label>
            <input id="login-name" name="mobile" id="mobile" class="form-control" value="${mobile!}"/>
        </div>

        <div class="form-group">
            <label>活动类型</label>
            <select class="selectpicker" name="activityCategory">
                <#list activityCategoryList as category>
                    <option value="${category}" <#if activityCategoryList?? && category==activityCategory>selected</#if>>
                    ${category.description}
                    </option>
                </#list>
            </select>
        </div>
        <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
    </form>

    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    编号
                </th>
                <th>
                    姓名
                </th>
                <th>
                    用户名
                </th>
                <th>
                    手机号
                </th>
                <th>
                    活动期限内投资金额
                </th>
                <th>
                    获得奖品
                </th>
                <th>
                    推荐用户注册数
                </th>
                <th>
                    推荐用户实名认证数
                </th>
                <th>
                    推荐用户累计投资金额
                </th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>
                    ${item.id!}
                    </td>
                    <td>
                    ${item.loginName!}
                    </td>
                    <td>
                    ${item.userName!}
                    </td>
                    <td>
                    ${item.mobile!}
                    </td>
                    <td>
                    ${item.investAmount!}
                    </td>
                    <td>
                    ${item.rewards!}
                    </td>
                    <td>
                    ${item.recommendedRegisterAmount!}
                    </td>
                    <td>
                    ${item.recommendedIdentifyAmount!}
                    </td>
                    <td>
                    ${item.recommendedInvestAmount!}
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
        </div>
        <#if data?has_content>
            <ul class="pagination  pull-left">
                <li>
                    <#if data.hasPreviousPage>
                    <a href="?index=${data.index - 1}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage>
                    <a href="?index=${data.index + 1}"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
            </ul>
            <@security.authorize access="hasAnyAuthority('DATA')">
                <button class="btn btn-default pull-left export-activity-prize-prize" id="activityPrizeExport" type="button">导出Excel</button>
            </@security.authorize>
        </#if>
    </nav>

    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>