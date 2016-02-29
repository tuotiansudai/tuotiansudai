<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-point-list.js" headLab="point-manage" sideLab="userPointList" title="用户财豆查询">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="control-label">用户名：</label>
            <input type="text" class="form-control jq-loginName" name="loginName" value="${loginName!}">
        </div>
        <div class="form-group">
            <label for="control-label">真实姓名：</label>
            <input type="text" class="form-control jq-userName" name="userName" value="${userName!}">
        </div>
        <div class="form-group">
            <label for="control-label">手机号：</label>
            <input type="text" class="form-control jq-mobile" name="mobile" value="${mobile!}">
        </div>

        <button class="btn btn-primary" type="submit">查询</button>
        <button class="btn btn-default" type="reset">重置</button>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户名</th>
                <th>真实姓名</th>
                <th>手机号</th>
                <th>可用财豆</th>
                <th>累计财豆</th>
                <th>明细记录</th>
            </tr>
            </thead>
            <tbody>
                <#list userPointList as userPointItem>
                <tr>
                    <td>${userPointItem.loginName!''}
                        <#if userPointItem.staff>
                            <span class="glyphicon glyphicon glyphicon-user" aria-hidden="true"></span>
                        </#if>
                    </td>
                    <td>${userPointItem.userName!''}</td>
                    <td>${userPointItem.mobile}</td>
                    <td>${userPointItem.availablePoint!''}</td>
                    <td>${userPointItem.point!''}</td>
                    <td><a href="#">查看明细</a></td>
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
                    <a href="/point-manage/user-point-list?loginName=${loginName!}&userName=${userName!}&mobile=${mobbile!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/point-manage/user-point-list?loginName=${loginName!}&userName=${userName!}&mobile=${mobbile!}&index=${index-1}&pageSize=${pageSize}">
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
