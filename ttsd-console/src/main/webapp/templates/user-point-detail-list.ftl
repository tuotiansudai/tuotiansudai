<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="point-manage" sideLab="userPointDetailList" title="用户财豆明细">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">

    </form>

    <div class="row">
        <div>用户名:${loginName}</div>
        <div>可用财豆:${point}</div>
        <div>累计财豆:${totalPoint}</div>
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>时间</th>
                <th>行为</th>
                <th>财豆数(个)</th>
                <th>备注</th>
            </tr>
            </thead>
            <tbody>
                <#list userPointDetailList as userPointDetailItem>
                <tr>
                    <td>${userPointDetailItem.createdTime?string("yyyy-MM-dd HH:mm:ss")}  </td>
                    <td>
                        <#if userPointDetailItem.businessType =='SIGN_IN'>
                                签到奖励
                        <#elseif userPointDetailItem.businessType =='TASK'>
                                任务奖励
                        <#elseif userPointDetailItem.businessType =='EXCHANGE'>
                                财豆兑换
                        <#elseif userPointDetailItem.businessType =='INVEST'>
                                投资奖励
                        <#else>
                                未知
                        </#if>
                    </td>
                    <td>${userPointDetailItem.point!''}</td>
                    <td>${userPointDetailItem.note!''}</td>
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
                    <a href="/point-manage/user-point-detail-list?loginName=${loginName}&point=${point}&totalPoint=${totalPoint}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/point-manage/user-point-detail-list?loginName=${loginName}&point=${point}&totalPoint=${totalPoint}&index=${index+1}&pageSize=${pageSize}">
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
