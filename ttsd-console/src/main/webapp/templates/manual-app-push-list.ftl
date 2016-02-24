<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="app-push-list.js" headLab="app-push-manage" sideLab="manualAppPushManage" title="手动推送管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
    <form action="/app-push-manage/manual-app-push-list" class="form-inline query-build">
    <div class="form-group">
        <label for="name">通知名称</label>
        <input type="text" id="name" name="name" class="form-control ui-autocomplete-input" datatype="*"
               autocomplete="off" value="${name!}"/>
    </div>
    <button class="btn btn-sm btn-primary query">查询</button>
    <a href="/app-push-manage/manual-app-push-list" class="btn btn-sm btn-default">重置</a>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
        <thead>
        <tr>
            <th>
                编号
            </th>
            <th>
                创建日期
            </th>
            <th>
                推送类型
            </th>
            <th>
                推送名称
            </th>
            <th>
                推送渠道
            </th>
            <th>
                推送地区
            </th>
            <th>
                用户类型
            </th>
            <th>
                推送模板
            </th>
            <th>
                推送时间
            </th>
            <th>
                定位到页面
            </th>
            <th>
                <span class="invest-IOS"></span>&nbsp;目标｜送达
            </th>
            <th>
                <span class="invest-ANDROID"></span>&nbsp;目标｜送达
            </th>
            <th>
                创建人｜最后编辑人
            </th>
            <th>
                审核人
            </th>
            <th>
                状态
            </th>
            <th>
                操作
            </th>
        </tr>
        </thead>
        <tbody>
        <#list pushAlerts as pushAlert>
        <tr>
            <td>
                ${pushAlert.id?string('0')}
            </td>
            <td>
                ${pushAlert.createdTime?string('yyyy-MM-dd')}
            </td>
            <td>
                ${pushAlert.pushType.getDescription()}
            </td>
            <td>
                ${(pushAlert.name)!}
            </td>
            <td>
                ${(pushAlert.pushSource)!}
            </td>
            <td>
                <#if pushAlert.pushObjects?has_content>
                    <#list pushAlert.pushObjects as pushObject>
                        <#list provinces?keys as key>
                            <#if pushObject == key>
                                ${provinces[key]}
                            </#if>
                        </#list>

                    </#list>
                <#else >
                    全部
                </#if>
            </td>
            <th>
                <#if pushAlert.pushUserType ??>
                    ${pushAlert.pushUserType.getDescription()!}
                </#if>
            </th>
            <td>
                ${(pushAlert.content)!}
            </td>
            <td>
                ${(pushAlert.expectPushTime?string('yyyy-MM-dd HH:mm:ss'))!}
            </td>
            <th>
                ${pushAlert.jumpTo.getDescription()}
            </th>
            <th>
                <a href="/app-push-manage/manual-app-push/${pushAlert.id?c}/refreshReport">${pushAlert.iosTargetNum!}｜${pushAlert.iosArriveNum!}</a>
            </th>
            <th>
                <a href="/app-push-manage/manual-app-push/${pushAlert.id?c}/refreshReport">${pushAlert.androidTargetNum!}｜${pushAlert.androidArriveNum!}</a>
            </th>
            <td>
                ${(pushAlert.createdBy)!}｜<#if (pushAlert.updatedBy)??>${pushAlert.updatedBy}<#else>无</#if>
            </td>
            <th>
                ${(pushAlert.auditor)!}
            </th>
            <td>
                <span class="push-status-${pushAlert.status}">${pushAlert.status.getDescription()}</span>
            </td>
            <td>
                <#if pushAlert.status == "WAIT_AUDIT" || pushAlert.status == "CREATED">
                    <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                        <a class="pass" href="#" data-pushId="${pushAlert.id?c}">同意</a>｜
                        <a href="/app-push-manage/manual-app-push/${pushAlert.id?c}/reject" onclick="return confirm('确定驳回吗?')">驳回</a>
                    </@security.authorize>
                    <@security.authorize access="hasAuthority('ADMIN')">｜</@security.authorize>
                    <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                        <a href="/app-push-manage/manual-app-push/${pushAlert.id?c}/edit">编辑</a>｜
                        <a href="/app-push-manage/manual-app-push/${pushAlert.id?c}/delete" onclick="return confirm('确定删除吗?')">删除</a>
                    </@security.authorize>
                </#if>
                <#if pushAlert.status == "WILL_SEND">
                    <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                        <a href="/app-push-manage/manual-app-push/${pushAlert.id?c}/delete" onclick="return confirm('确定删除吗?')">删除</a>
                    </@security.authorize>
                </#if>
                <#if pushAlert.status == "REJECTED">
                    <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                        <a href="/app-push-manage/manual-app-push/${pushAlert.id?c}/edit">编辑</a>
                    </@security.authorize>
                    <@security.authorize access="hasAuthority('ADMIN')">｜</@security.authorize>
                    <@security.authorize access="hasAnyAuthority('OPERATOR','OPERATOR_ADMIN','ADMIN')">
                        <a href="/app-push-manage/manual-app-push/${pushAlert.id?c}/delete" onclick="return confirm('确定删除吗?')">删除</a>
                    </@security.authorize>
                </#if>
                <#if pushAlert.status == "SEND_SUCCESS">
                    <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                        <a href="/app-push-manage/manual-app-push/${pushAlert.id?c}/clone">复用</a>
                    </@security.authorize>
                </#if>
            </td>
        </tr>
        </#list>
        </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
    <div>
        <span class="bordern">总共${jPushAlertCount}条,每页显示${pageSize}条</span>
    </div>
    <#if pushAlerts?has_content>
    <ul class="pagination">
        <li>
        <#if hasPreviousPage>
        <a href="?index=${index-1}&pageSize=${pageSize}&name=${name!}" aria-label="Previous">
        <#else>
        <a href="#" aria-label="Previous">
        </#if>
        <span aria-hidden="true">&laquo; Prev</span>
        </a>
        </li>
        <li><a>${index}</a></li>
        <li>
        <#if hasNextPage>
        <a href="?index=${index+1}&pageSize=${pageSize}&name=${name!}" aria-label="Next">
        <#else>
        <a href="#" aria-label="Next">
        </#if>
        <span aria-hidden="true">Next &raquo;</span>
        </a>
        </li>
    </ul>
    </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>