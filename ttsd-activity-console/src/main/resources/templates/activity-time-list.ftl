<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="activity-lottery-list.js" headLab="activity-manage" sideLab="lottery" title="抽奖数据统计">

<div class="col-md-10">
    <div class="panel panel-default">
        <div class="panel-body">
            <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/user-time-list" role="button">抽奖机会统计</a>
            <a class="btn btn-default " href="/activity-console/activity-manage/user-prize-list" role="button">抽奖记录</a>
        </div>
    </div>

    <div class="table-responsive">
        <form action="/activity-console/activity-manage/user-time-list" method="get" class="form-inline query-build" id="lotteryTimeForm">

                <div class="form-group">
                    <label>用户手机号</label>
                    <input id="login-name" name="mobile" id="mobile" class="form-control" value="${mobile!}"/>
                </div>

                <div class="form-group">
                    <label>活动类型</label>
                    <select class="selectpicker" name="prizeType">
                        <#list prizeTypes as prizeType>
                            <option value="${prizeType}" <#if prizeTypes?? && prizeType==selectPrize>selected</#if>>
                            ${prizeType.description}
                            </option>
                        </#list>
                    </select>
                </div>
            <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
        </form>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>用户手机号</th>
                    <th>姓名</th>
                    <th>可用抽奖机会</th>
                    <th>已用抽奖机会</th>
                </tr>
                </thead>
                <tbody>
                    <#if lotteryList?? >
                        <#list lotteryList as lottery>
                            <tr>
                                <td>${lottery.mobile!}</td>
                                <td>${lottery.userName!}</td>
                                <td>${lottery.unUseCount!}</td>
                                <td>${lottery.useCount!}</td>
                            </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>

            <!-- pagination  -->
            <nav class="pagination-control">
                <div><span class="bordern">总共${lotteryCount}条,每页显示${pageSize}条</span></div>
                <ul class="pagination pull-left">
                    <li>
                        <#if hasPreviousPage >
                        <a href="/activity-console/activity-manage/user-time-list?mobile=${mobile!}&index=${index-1}&pageSize=${pageSize}">
                        <#else>
                        <a href="#">
                        </#if>
                        <span>« Prev</span>
                    </a>
                    </li>
                    <li><a>${index}</a></li>
                    <li>
                        <#if hasNextPage>
                        <a href="/activity-console/activity-manage/user-time-list?mobile=${mobile!}&index=${index+1}&pageSize=${pageSize}">
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