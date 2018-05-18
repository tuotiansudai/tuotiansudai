<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="InviteHelpActivity" title="返利加油站活动">

<div class="col-md-10">

    <form action="/activity-console/activity-manage/super-scholar-list" class="form-inline query-build"
          method="get">
        <div class="form-group">
            <input type="text" id="keyWord" name="keyWord" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${keyWord!}" placeholder="姓名/手机号">
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>姓名</th>
                <th>手机号</th>
                <th>投资金额</th>
                <th>年化投资额</th>
                <th>返现比率</th>
                <th>获得现金奖励</th>
                <th>投资时间</th>
                <th>是否返现</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.userName}</td>
                    <td>${item.mobile!}</td>
                    <td>${item.amount!}</td>
                    <td>${item.annualizedAmount!}</td>
                    <td>${item.rewardRate}</td>
                    <td>${item.reward}</td>
                    <td>${item.investTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${item.cashBack?string('是','否')}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="7">暂无数据</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <a href="/activity-console/activity-manage/super-scholar?keyWord=${keyWord!}" class="form-control" style="width: 170px">请点击此处导出EXCEl</a><br/>

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