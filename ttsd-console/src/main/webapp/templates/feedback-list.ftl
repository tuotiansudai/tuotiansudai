<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="feedback-list.js" headLab="announce-manage" sideLab="feedbackMan" title="意见反馈">
<!-- content area begin -->

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">用户名</label>
                <input type="text" class="form-control jq-title jq-loginName" name="loginName" value="${loginName!}">
            </div>

            <div class="form-group">
                <label>反馈渠道：</label>
                <select class="selectpicker" name="source">
                    <option value="" <#if !(source??)>selected</#if>>全部</option>
                    <#list sourceList as item>
                        <option value="${item}" <#if source?? && item==source>selected</#if>>
                        ${item}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label>反馈类型：</label>
                <select class="selectpicker" name="type">
                    <option value="" <#if !(type??)>selected</#if>>全部</option>
                    <#list typeList as item>
                        <option value="${item}" <#if type?? && item==type>selected</#if>>
                        ${item.desc}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label>状态：</label>
                <select class="selectpicker" name="status">
                    <option value="" <#if !(status??)>selected</#if>>全部</option>
                    <#list statusList as item>
                        <option value="${item}" <#if status?? && item==status>selected</#if>>
                        ${item.desc}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label>反馈时间：</label>

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

            <button class="btn btn-primary search" type="submit">查询</button>
            <a href="/announce-manage/feedback" class="btn btn-sm btn-default">重置</a>
        </div>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>编号</th>
                <th>用户名</th>
                <th>联系电话</th>
                <th>反馈渠道</th>
                <th>反馈类型</th>
                <th>内容</th>
                <th>时间</th>
                <th>是否处理</th>
            </tr>
            </thead>
            <tbody>
                <#list feedbackList as feedback>
                <tr>
                    <td width="50">${(feedback.id?string('0'))!}</td>
                    <td width="150">${feedback.loginName!}</td>
                    <td width="150">${feedback.contact!}</td>
                    <td width="150">${feedback.source!}</td>
                    <td width="150">${(feedback.type.desc)!}</td>
                    <td style="text-align:left;">${feedback.content?replace('\n','<br>')}</td>
                    <td width="150">${(feedback.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <#if feedback.status == 'NOT_DONE' >
                        <td width="100"><input type="checkbox" class="feedback-status" data-id="${feedback.id?c}"/></td>
                    <#else>
                        <td width="100"><input type="checkbox" class="feedback-status" data-id="${feedback.id?c}" checked/></td>
                    </#if>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${feedbackCount}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="?loginName=${loginName!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?loginName=${loginName!}&index=${index+1}&pageSize=${pageSize}">
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