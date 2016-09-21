<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="membership-give-list.js" headLab="membership-manage" sideLab="membershipGiveList" title="会员发放管理">

<!-- content area begin -->
<div class="col-md-10">

    <form action="/membership-manage/give/${selectGiveId?c}/details" class="form-inline query-build">
        <div class="form-group">
            <label>手机</label>
            <input type='text' class="form-control" id="mobile" name="mobile" value="${selectMobile!}"/>
        </div>

        <button class="btn btn-sm btn-primary query">查询</button>
    </form>

    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    用户名
                </th>
                <th>
                    手机
                </th>
                <th>
                    获得时间
                </th>
            </tr>
            </thead>
            <tbody>
                <#list membershipGiveReceiveDtos as membershipGiveReceiveDto>
                <tr>
                    <td>${membershipGiveReceiveDto.loginName}</td>
                    <td>${membershipGiveReceiveDto.mobile}</td>
                    <td>${membershipGiveReceiveDto.receiveTime?date}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${totalCount}条,每页显示${pageSize}条</span>
        </div>
        <#if membershipGiveReceiveDtos?has_content>
            <ul class="pagination">
                <li>
                    <#if hasPreviousPage>
                    <a href="?index=${index - 1}&pageSize=${pageSize}<#if selectMobile??>&mobile=${selectMobile}</#if>" aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?index=${index + 1}&pageSize=${pageSize}<#if selectMobile??>&mobile=${selectMobile}</#if>" aria-label="Next">
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