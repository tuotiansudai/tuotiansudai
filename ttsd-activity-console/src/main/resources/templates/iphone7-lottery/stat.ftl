<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "../macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="iphone7-lottery.js" headLab="activity-manage" sideLab="iphone7-lottery" title="iphone7活动参与情况">

<div class="col-md-10">
    <div class="table-responsive">
        <form method="get" class="form-inline query-build">
            <div class="form-group">
                <label>用户手机号</label>
                <input id="login-name" name="mobile" id="mobile" class="form-control" value="${mobile!}"/>
            </div>
            <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
        </form>
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户手机号</th>
                <th>姓名</th>
                <th>活动期限投资金额(元)</th>
                <th>投资码个数</th>
            </tr>
            </thead>
            <tbody>
                <#if data.records?? >
                    <#list data.records as userItem>
                    <tr>
                        <td>${userItem.mobile}</td>
                        <td>${userItem.userName!}</td>
                        <td>${userItem.investAmountTotal/100!}</td>
                        <td><a class="user-lotteries-count" href="#" data-username="${userItem.userName}"
                               data-lotteries="${userItem.lotteryNumberArray!}">${userItem.investCount!}</a></td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>

        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${data.count}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                    <a href="/activity-console/activity-manage/iphone7-lottery/stat?mobile=${mobile!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if data.hasNextPage>
                    <a href="/activity-console/activity-manage/iphone7-lottery/stat?mobile=${mobile!}&index=${index+1}&pageSize=${pageSize}">
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