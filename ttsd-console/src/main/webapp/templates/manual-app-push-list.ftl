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
                通知类型
            </th>
            <th>
                通知名称
            </th>
            <th>
                推送对象
            </th>
            <th>
                推送渠道
            </th>
            <th>
                推送模板
            </th>
            <th>
                状态
            </th>
            <th>
                操作
            </th>
            <th>
                创建人
            </th>
            <th>
                发送日期
            </th>
            <th>
                最后操作人
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
            <td>
                ${(pushAlert.content)!}
            </td>
            <td>
                ${pushAlert.status.getDescription()}
            </td>
            <td>
                <#if pushAlert.status != "SEND_SUCCESS">
                    <a href="/app-push-manage/manual-app-push/${pushAlert.id?string('0')}/edit">编辑</a>|<a class="send-push-link" href="/app-push-manage/manual-app-push/${pushAlert.id?string('0')}/send">推送</a>
                </#if>
            </td>
            <td>
                ${(pushAlert.createdBy)!}
            </td>
            <td>
                <#if pushAlert.status == "SEND_SUCCESS">
                    ${(pushAlert.updatedTime?string('yyyy-MM-dd'))!}
                </#if>
            </td>
            <td>
                ${(pushAlert.updatedBy)!}
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
        <a href="?index=${index-1}&pageSize=${pageSize}" aria-label="Previous">
        <#else>
        <a href="#" aria-label="Previous">
        </#if>
        <span aria-hidden="true">&laquo; Prev</span>
        </a>
        </li>
        <li><a>${index}</a></li>
        <li>
        <#if hasNextPage>
        <a href="?index=${index+1}&pageSize=${pageSize}" aria-label="Next">
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