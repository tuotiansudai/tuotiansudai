<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="InviteHelpActivity" title="返利加油站活动">

<div class="col-md-10">
    <div class="panel panel-default">
        <div class="panel-body">
            <a class="btn btn-default btn-primary"
               href="/activity-console/activity-manage/invite-help/invest-reward-list" role="button">投资奖励</a>
            <a class="btn btn-default"
               href="/activity-console/activity-manage/invite-help/share-reward-list" role="button">分享奖励</a>
        </div>
    </div>

    <form action="/activity-console/activity-manage/invite-help/invest-reward-list" class="form-inline query-build"
          method="get">
        <div class="form-group">
            <input type="text" id="keyWord" name="keyWord" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${keyWord!}" placeholder="姓名/手机号">
        </div>

        <div class="form-group">
            <label for="number">活动期间内投资额</label>
            <input type="text" class="form-control jq-balance-min" name="minInvest" value="${minInvest!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control jq-balance-max" name="maxInvest" value="${maxInvest!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>投资时间</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>投资金额</th>
                <th>年化投资额</th>
                <th>助力人数</th>
                <th>奖励比例</th>
                <th>获得现金奖励</th>
                <th>详情</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.startTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${item.userName!}</td>
                    <td>${item.mobile!}</td>
                    <td>${(item.investAmount/100)?string('0.00')}</td>
                    <td>${(item.annualizedAmount/100)?string('0.00')}</td>
                    <td>${item.helpUserCount}</td>
                    <td>${item.rate}</td>
                    <td>${(item.reward/100)?string('0.00')}</td>
                    <td><a href="/activity-console/activity-manage/invite-help/help/${item.id}/detail" class="btn btn-default btn-primary">查看详情</a></td>
                </tr>
                <#else>
                <tr>
                    <td colspan="9">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <a href="/activity-console/activity-manage/invite-help/invest-reward?keyWord=${keyWord!}&minInvest=${minInvest!}&maxInvest=${maxInvest!}" class="form-control" style="width: 170px">请点击此处导出EXCEl</a><br/>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if data.count &gt; 0>
            <div>
                <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                        <a href="?mobile=${mobile!}&startTime=${startTime!}&endTime=${endTime!}&index=${data.index-1}"
                           aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                        <a href="?mobile=${mobile!}&startTime=${startTime!}&endTime=${endTime!}&index=${data.index+1}"
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