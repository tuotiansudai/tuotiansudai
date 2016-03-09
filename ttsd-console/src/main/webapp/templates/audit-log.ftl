<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="audit-log.js" headLab="security" sideLab="auditLog" title="用户管理">
<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">

        <div class="form-group">
            <label for="operationType">业务类型</label>
            <select class="selectpicker" id="operation-type" name="operationType">
                <option value="">全部</option>
                <#list operationTypes as operationTypeItem>
                    <option value="${operationTypeItem.name()}"
                            <#if (operationType.name())?has_content && operationType.name() == operationTypeItem.name()>selected</#if>
                            >${operationTypeItem.targetType}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="targetId">操作对象ID</label>
            <input type="text" id="target-id" name="targetId" class="form-control" datatype="*" autocomplete="off" value="${targetId!}"/>
        </div>

        <div class="form-group">
            <label for="operatorLoginName">操作人</label>
            <input type="text" id="operator-login-name" name="operatorLoginName"
                   class="form-control ui-autocomplete-input" datatype="*" autocomplete="off"
                   value="${operatorLoginName!}"/>
        </div>

        <div class="form-group">
            <label for="auditorLoginName">审核人</label>
            <input type="text" id="auditor-login-name" name="auditorLoginName"
                   class="form-control ui-autocomplete-input" datatype="*" autocomplete="off"
                   value="${auditorLoginName!}"/>
        </div>

        <div class="form-group">
            <label>日期</label>

            <div class='input-group date' id='startTime'>
                <input type='text' class="form-control" name="startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
            </div>
            -
            <div class='input-group date' id='endTime'>
                <input type='text' class="form-control" name="endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
            </div>
        </div>

        <button type="submit" class="btn btn-sm btn-primary">查询</button>
        <button type="reset" class="btn btn-sm btn-default">重置</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>操作时间</th>
                <th>IP</th>
                <th>操作对象</th>
                <th>操作人</th>
                <th>审核人</th>
                <th>操作详情</th>

            </tr>
            </thead>
            <tbody>
                <#list data.records as record>
                <tr>
                    <td>${record.operationTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${record.ip}</td>
                    <td>${record.operationType.getTargetType()}<br>${record.targetId}</td>
                    <td>${record.operatorLoginName!}</td>
                    <td>${record.auditorLoginName!}</td>
                    <td>${record.description}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="5">无记录</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <nav>
        <div>
            <span class="bordern">总共${data.count!('0')}条,每页显示${data.pageSize}条</span>
        </div>
        <ul class="pagination">
            <li <#if !data.hasPreviousPage>class="disabled"</#if>>
                <a href="?operationType=${operationType!}&targetId=${targetId!}&operatorLoginName=${operatorLoginName!}&auditorLoginName=${auditorLoginName!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&pageSize=${pageSize}&index=${index-1}"
                   class="previous <#if !data.hasPreviousPage>disabled</#if>">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="disabled"><a class="current-page" data-index="${data.index}">${data.index}</a></li>
            <li <#if !data.hasNextPage>class="disabled"</#if>>
                <a href="?operationType=${operationType!}&targetId=${targetId!}&operatorLoginName=${operatorLoginName!}&auditorLoginName=${auditorLoginName!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&pageSize=${pageSize}&index=${index+1}"
                   class="next <#if !data.hasNextPage>disabled</#if>">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
</div>
<!-- content area end -->
</@global.main>