<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="finance-manage" sideLab="userBalance" title="用户余额">

<!-- content area begin -->
<div class="col-md-10">

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>地区</th>
                <th>账户余额(共${sumBalance/100})</th>
            </tr>
            </thead>
            <tbody>
                <#list userAccountList as userAccount>
                <tr>
                    <td>${userAccount.loginName!''}</td>
                    <td>${userAccount.account.userName!''}</td>
                    <td>${userAccount.mobile}</td>
                    <td>${userAccount.province!''}</td>
                    <td>${userAccount.account.balance/100}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${count}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/finance-manage/account-balance?currentPageNo=${currentPageNo-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${currentPageNo}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/finance-manage/account-balance?currentPageNo=${currentPageNo+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>
