<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="activity-center-list.js" headLab="activity-manage" sideLab="activityCenter" title="活动中心管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" method="get" class="form-inline query-build">

        <div class="form-group">
            <label>发布日期</label>

            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="startTime"
                       value="${(startTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="endTime"
                       value="${(endTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>

        <div class="form-group">
            <label>活动状态</label>
            <select class="selectpicker" name="activityStatus">
                <option value="" <#if !(activityStatus??)>selected</#if>>全部</option>
                <#list activityStatusList as status>
                    <#if status != 'APPROVED' >
                        <option value="${status}" <#if activityStatus?? && status==activityStatus>selected</#if>>
                        ${status.description}
                        </option>

                    </#if>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label>渠道</label>
            <select class="selectpicker" name="source">
                <option value="">全部</option>
                <#list sourceList as sourceItem>
                    <option value="${sourceItem.name()}"
                            <#if (source?has_content && source == sourceItem.name()) >selected</#if>
                    >${sourceItem.name()}</option>
                </#list>
            </select>
        </div>
        <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
        <button type="reset" class="btn btn-sm btn-default btnSearch">重置</button>

        <@security.authorize access="hasAnyAuthority('OPERATOR', 'ADMIN')">
            <button type="button" class="btn btn-sm btn-default btnSearch btnAddActivity">添加活动</button>
        </@security.authorize>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>Web活动图</th>
                <th>App活动图</th>
                <th>活动名称</th>
                <th>发布日期</th>
                <th>结束时间</th>
                <th>目标地址</th>
                <th>活动介绍</th>
                <th>渠道</th>
                <th>分享标题</th>
                <th>分享内容</th>
                <th>分享链接</th>
                <th>活动状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#if activityCenterList?? >
                    <#list activityCenterList as activity>
                    <tr>
                        <td><span class="webImg"><img id="webPicture" src="${staticServer}${activity.webPictureUrl!}"/></span></td>
                        <td><span class="appImg"><img id="appPicture" src="${staticServer}${activity.appPictureUrl!}"/></span></td>
                        <td>${activity.title!}</td>
                        <td><#if activity.activatedTime??>${activity.activatedTime?string('yyyy-MM-dd')}</#if></td>
                        <td><#if activity.expiredTime??>${activity.expiredTime?string('yyyy-MM-dd HH:mm')}</#if></td>
                        <td><a href="${activity.webActivityUrl!}" target="_blank">${activity.webActivityUrl!}</a><br/>${activity.appActivityUrl!}</td>
                        <td>${activity.description!}</td>
                        <td>
                            <#list activity.source as source>
                            ${source.name()}<#sep>, </#sep>
                            </#list>
                        </td>
                        <td>${activity.shareTitle!}</td>
                        <td>${activity.shareContent!}</td>
                        <td>${activity.shareUrl!}</td>
                        <#if activity.expiredTime??>
                            <#assign expiredTime = activity.expiredTime?string('yyyy-MM-dd HH:mm')>
                            <#assign currentTime = .now?string('yyyy-MM-dd HH:mm')>
                            <td>
                                <#if activity.status == 'APPROVED' && (expiredTime?date('yyyy-MM-dd HH:mm') gt currentTime?date('yyyy-MM-dd HH:mm'))>
                                    进行中
                                <#elseif (expiredTime?date('yyyy-MM-dd HH:mm') lt currentTime?date('yyyy-MM-dd HH:mm'))>
                                    已结束
                                <#else>
                                ${activity.status.getDescription()!}
                                </#if>
                            </td>

                        <#else>
                            <td>${activity.status.getDescription()!}</td>
                        </#if>
                        <td>
                            <#if activity.status == 'TO_APPROVE' >
                                <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                                    <a href="/activity-manage/activity-center/${activity.activityId?c!}">审核</a>
                                </@security.authorize>
                            <#elseif activity.status == 'REJECTION' || (activity.status == 'APPROVED' )>
                                <@security.authorize access="hasAnyAuthority('OPERATOR', 'ADMIN')">
                                    <a href="/activity-manage/activity-center/${activity.activityId?c!}">修改</a>
                                </@security.authorize>
                            </#if>
                        </td>
                    </tr>
                    <#else>
                    <tr>
                        <td colspan="13">暂时没有活动</td>
                    </tr>
                    </#list>

                </#if>
            </tbody>

        </table>
    </div>

</div>
<!-- content area end -->
</@global.main>