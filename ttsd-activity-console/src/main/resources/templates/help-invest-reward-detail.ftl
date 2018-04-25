<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="InviteHelpActivity" title="返利加油站活动">

<div class="col-md-10">

    <form action="/activity-console/activity-manage/invite-help/help/${helpId}/detail" class="form-inline query-build"
          method="get">
        <div class="form-group">
            <label for="number">昵称</label>
            <input type="text" id="nickName" name="nickName" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${nickName!}" placeholder="">
        </div>

        <div class="form-group">
            <label>发放状态</label>
            <select name="status" class="btn-group">
                <option value="" <#if status == ''>selected</#if>>全部</option>
                <option value="WAITING" <#if status == 'WAITING'>selected</#if>>待发放</option>
                <option value="SUCCESS" <#if status == 'SUCCESS'>selected</#if>>发放成功</option>
                <option value="FAIL" <#if status == 'FAIL'>selected</#if>>发放失败</option>
            </select>
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
    </form>

    <div>
        现金奖励共:${(helpModel.reward/100)?string('0.00')}元, 助力人:${helpModel.helpUserCount}人,
        平均每人获得<#if helpModel.helpUserCount gt 0>${(((helpModel.reward/helpModel.helpUserCount)?floor)/100)?string('0.00')}<#else>0.00</#if>元奖励
    </div>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>助力时间</th>
                <th>助力人姓名</th>
                <th>助力人手机号</th>
                <th>助力人昵称</th>
                <th>发放状态</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.createdTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${item.userName!}</td>
                    <td>${item.mobile!}</td>
                    <td>${item.nickName!}</td>
                    <td>
                        <#if item.status == 'WAITING'>
                            待发放
                        <#elseif item.status == 'SUCCESS'>
                            发放成功
                        <#else>
                            发放失败
                        </#if>
                    </td>
                </tr>
                <#else>
                <tr>
                    <td colspan="5">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <#--<a href="/activity-console/activity-manage/zero-shopping" class="form-control" style="width: 170px">请点击此处导出EXCEl</a><br/>-->

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if data.count &gt; 0>
            <div>
                <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                        <a href="?nickName=${nickName!}&status=${status!}&index=${data.index-1}"
                           aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                        <a href="?nickName=${nickName!}&status=${status!}&index=${data.index+1}"
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