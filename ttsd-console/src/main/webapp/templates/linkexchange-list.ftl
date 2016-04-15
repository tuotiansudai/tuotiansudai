<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="linkexchange.js" headLab="announce-manage" sideLab="linkexchangeMan" title="友链管理">
<!-- content area begin -->

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">标题</label>
                <input type="text" class="form-control jq-title" value="${title!}">
            </div>
            <button class="btn btn-primary search" type="button">查询</button>
            <button class="btn btn-default pull-right publishAD" type="button"> 添加友链</button>
        </div>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>标题</th>
                <th>链接</th>
                <th>更新时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list linkExchangeList as linkexchange>
                <tr>
                    <td>${linkexchange.title!}</td>
                    <td>${linkexchange.linkUrl!}</td>
                    <td>${(linkexchange.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td><a href="/linkexchange-manage/linkexchange/edit/${(linkexchange.id?string('0'))!}" class="btn btn-link"> 编辑</a>
                        | <a href="#" class="btn btn-link jq-delete"
                             data-id="${(linkexchange.id?string('0'))!}">删除</a></td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${linkExchangeCount}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/linkexchange-manage/linkexchange?title=${title!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="/linkexchange-manage/linkexchange?title=${title!}&index=${index+1}&pageSize=${pageSize}">
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