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
            <label>推送类型</label>
            <select class="selectpicker" name="pushType">
                <option value="" <#if !(pushTypeInput??)>selected</#if>>全部</option>
                <#list pushTypes as pushType>
                    <#if pushType.getType()=='MANUAL'>
                        <option value="${pushType.name()}" <#if pushTypeInput?? && pushType==pushTypeInput>selected</#if>>${pushType.getDescription()}</option>
                    </#if>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>推送渠道</label>
            <select class="selectpicker" name="pushSource">
                <option value="" <#if !(pushSourceInput??)>selected</#if>>全部</option>
                <#list pushSources as pushSource>
                    <option value="${pushSource.name()}" <#if pushSourceInput?? && pushSource==pushSourceInput>selected</#if>>${pushSource.name()}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>用户类型</label>

            <select class="selectpicker" name="pushUserType">
                <option value="" <#if !(pushUserTypeInput??)>selected</#if>>所有</option>
                <#list pushUserTypes as pushUserType>
                    <option value="${pushUserType.name()}" <#if pushUserTypeInput?? && pushUserType==pushUserTypeInput>selected</#if>>${pushUserType.getDescription()}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>状态</label>
            <select class="selectpicker" name="pushStatus">
                <option value="" <#if !(pushStatusInput??)>selected</#if>>全部</option>
                <#list pushStatuses as pushStatus>
                    <#if pushStatus.name() != 'ENABLED' && pushStatus.name() != 'DISABLED'  >
                        <option value="${pushStatus.name()}" <#if pushStatusInput?? && pushStatus==pushStatusInput>selected</#if>>${pushStatus.getDescription()}</option>
                    </#if>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>推送时间</label>

            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
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
                <#if pushAlert.pushDistricts?has_content>
                    <#list pushAlert.pushDistricts as pushDistrict>
                        <#list provinces?keys as key>
                            <#if pushDistrict == key>
                                ${provinces[key]}
                            </#if>
                        </#list>

                    </#list>
                <#else >
                    全部
                </#if>
            </td>
            <th>
                <#if pushAlert.pushUserType?has_content>
                    <#list pushAlert.pushUserType as userType>
                        ${userType.getDescription()!}<#sep>, </#sep>
                    </#list>
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
                <div class="jpushReport" data-pushId="${pushAlert.id?c}">${pushAlert.iosTargetNum!}｜<span class="iosReport">${pushAlert.iosArriveNum!}</span></div>
            </th>
            <th>
                <div class="jpushReport" data-pushId="${pushAlert.id?c}">${pushAlert.androidTargetNum!}｜<span class="androidReport">${pushAlert.androidArriveNum!}</span></div>
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
                    <a href="?index=${index-1}&pageSize=${pageSize} <#if pushTypeInput??>&pushType=${pushTypeInput}</#if> <#if pushSourceInput??>&pushSource=${pushSourceInput}</#if> <#if pushUserTypeInput??>&pushUserType=${pushUserTypeInput}</#if> <#if pushStatusInput??>&pushStatus=${pushStatusInput}</#if> <#if startTime??>&startTime=${startTime?string('yyyy-MM-dd')}</#if> <#if endTime??>&endTime=${endTime?string('yyyy-MM-dd')}</#if>" aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?index=${index+1}&pageSize=${pageSize} <#if pushTypeInput??>&pushType=${pushTypeInput}</#if> <#if pushSourceInput??>&pushSource=${pushSourceInput}</#if> <#if pushUserTypeInput??>&pushUserType=${pushUserTypeInput}</#if> <#if pushStatusInput??>&pushStatus=${pushStatusInput}</#if> <#if startTime??>&startTime=${startTime?string('yyyy-MM-dd')}</#if> <#if endTime??>&endTime=${endTime?string('yyyy-MM-dd')}</#if>" aria-label="Next">
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