<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="point-manage" sideLab="userPointDetailList" title="用户积分明细">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">

    </form>

    <div class="row">
        <div>用户名:${loginName}</div>
        <div>可用积分:${point}</div>
        <div>累计积分:${totalPoint}</div>
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>时间</th>
                <th>行为</th>
                <th>渠道积分</th>
                <th>速贷积分</th>
                <th>总积分</th>
                <th>备注</th>
            </tr>
            </thead>
            <tbody>
                <#list userPointDetailList as userPointDetailItem>
                <tr>
                    <td>${userPointDetailItem.createdTime?string("yyyy-MM-dd HH:mm:ss")}  </td>
                    <td>
                        ${userPointDetailItem.businessType.getDescription()}
                    </td>
                    <td>${userPointDetailItem.channelPoint!''}</td>
                    <td>${userPointDetailItem.sudaiPoint!''}</td>
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
            <div><span class="bordern">总共${count?string.computer}条,每页显示${pageSize?string.computer}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/point-manage/user-point-detail-list?loginName=${loginName}&point=${(point?string.computer)!}&totalPoint=${(totalPoint?string.computer)!}&index=${(index-1)?string.computer}&pageSize=${pageSize?string.computer}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${index?string.computer}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/point-manage/user-point-detail-list?loginName=${loginName}&point=${(point?string.computer)!}&totalPoint=${!totalPoint?string.computer)!}&index=${(index+1)?string.computer}&pageSize=${pageSize?string.computer}">
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
