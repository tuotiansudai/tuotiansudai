<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="audit-log.js" headLab="security" sideLab="auditLog" title="用户管理">
<!-- content area begin -->
<div class="col-md-10">
    <form action="/" class="form-inline query-build">
        <div class="form-group">
            <label for="loginName">用户名</label>
            <input type="text" id="login-name" name="loginName" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${loginName!}"/>
        </div>

        <div class="form-group">
            <label for="loginName">操作人</label>
            <input type="text" id="operator-login-name" name="operatorLoginName"
                   class="form-control ui-autocomplete-input" datatype="*" autocomplete="off"
                   value="${operatorLoginName!}"/>
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

        <button class="btn btn-sm btn-primary query">查询</button>
        <a href="/security-log/audit-log" class="btn btn-sm btn-default">重置</a>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>操作时间</th>
                <th>IP</th>
                <th>用户名</th>
                <th>操作人</th>
                <th>操作详情</th>

            </tr>
            </thead>
            <tbody>
                <#list data.records as record>
                <tr>
                    <td>${record.operationTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${record.ip}</td>
                    <td>${record.loginName}</td>
                    <td>${record.operatorLoginName!}</td>
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
                <a href="javascript:" class="previous <#if !data.hasPreviousPage>disabled</#if>"><span
                        aria-hidden="true">&laquo;</span></a>
            </li>
            <li class="disabled"><a class="current-page" data-index="${data.index}">${data.index}</a></li>
            <li <#if !data.hasNextPage>class="disabled"</#if>>
                <a href="javascript:" class="next <#if !data.hasNextPage>disabled</#if>"><span
                        aria-hidden="true">&raquo;</span></a>
            </li>
        </ul>
    </nav>
</div>
<!-- content area end -->
</@global.main>