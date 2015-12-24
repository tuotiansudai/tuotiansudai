<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="feedback-list.js" headLab="announce-manage" sideLab="feedbackMan" title="公告管理">
<!-- content area begin -->

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">用户名</label>
                <input type="text" class="form-control jq-title jq-loginName" name="loginName" value="${loginName!}">
            </div>
            <button class="btn btn-primary search" type="submit">查询</button>
            <a href="/announce-manage/feedback" class="btn btn-sm btn-default">重置</a>
        </div>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>编号</th>
                <th>用户名</th>
                <th>内容</th>
                <th>时间</th>
            </tr>
            </thead>
            <tbody>
                <#list feedbackList as feedback>
                <tr>
                    <td width="50">${(feedback.id?string('0'))!}</td>
                    <td width="150">${feedback.loginName}</td>
                    <td style="text-align:left;">${feedback.content?replace('\n','<br>')}</td>
                    <td width="150">${(feedback.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${feedbackCount}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="?loginName=${loginName!}&currentPageNo=${currentPageNo-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span>
                </a>
                </li>
                <li><a>${currentPageNo}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?loginName=${loginName!}&currentPageNo=${currentPageNo+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span>
                </a>
                </li>
            </ul>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>