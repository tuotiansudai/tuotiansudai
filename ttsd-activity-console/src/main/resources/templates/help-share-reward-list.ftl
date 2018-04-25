<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="InviteHelpActivity" title="返利加油站活动">

<div class="col-md-10">
    <div class="panel panel-default">
        <div class="panel-body">
            <a class="btn btn-default"
               href="/activity-console/activity-manage/invite-help/invest-reward-list" role="button">投资奖励</a>
            <a class="btn btn-default btn-primary"
               href="/activity-console/activity-manage/invite-help/share-reward-list" role="button">分享奖励</a>
        </div>
    </div>

    <form action="/activity-console/activity-manage/invite-help/share-reward-list" class="form-inline query-build"
          method="get">
        <div class="form-group">
            <input type="text" id="keyWord" name="keyWord" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${keyWord!}" placeholder="姓名/手机号/昵称">
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>分享时间</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>昵称</th>
                <th>助力人数</th>
                <th>获得现金奖励</th>
                <th>是否发放奖励</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.startTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${item.userName!}</td>
                    <td>${item.mobile!}</td>
                    <td>${item.nickName!}</td>
                    <td>${item.helpUserCount}</td>
                    <td>${(item.reward/100)?string('0.00')}</td>
                    <td>
                        <#if item.cashBack>
                            已发放
                        <#else >
                            未发放
                        </#if>
                    </td>
                </tr>
                <#else>
                <tr>
                    <td colspan="6">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if data.count &gt; 0>
            <div>
                <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                        <a href="?keyWord=${keyWord!}&index=${data.index-1}"
                           aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                        <a href="?keyWord=${keyWord!}&index=${data.index+1}"
                           aria-label="Previous">
                            <span aria-hidden="true">Next &raquo;</span>
                        </a>
                    </#if>
                </li>
            </ul>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>