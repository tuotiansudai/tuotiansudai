<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="login-log.js" headLab="security" sideLab="loginLog" title="登录日志">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/" class="form-inline query-build">
        <div class="form-group">
            <label for="loginName">用户名</label>
            <input type="text" id="login-name" name="loginName" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${loginName!}"/>
        </div>

        <div class="form-group">
            <label for="selected-year">年</label>
            <select class="selectpicker" id="selected-year" name="selectedYear">
                <#list years as year>
                    <option value="${year}" <#if selectedYear==year>selected</#if>>${year}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="selected-month">月</label>
            <select class="selectpicker" id="selected-month" name="selectedMonth">
                <#list ["1","2","3","4","5","6","7","8","9","10","11","12"] as month>
                    <option value="${month}" <#if selectedMonth==month>selected</#if>>${month}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="status">状态</label>
            <select class="selectpicker" id="status" name="success">
                <option value="">全部</option>
                <option value="true" <#if success?? && success>selected</#if>>成功</option>
                <option value="false" <#if success?? && !success>selected</#if>>失败</option>
            </select>
        </div>

        <button class="btn btn-sm btn-primary query">查询</button>
        <a href="/security-log/login-log" class="btn btn-sm btn-default">重置</a>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户名</th>
                <th>来源</th>
                <th>IP</th>
                <th>设备</th>
                <th>登录时间</th>
                <th>状态</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as record>
                <tr>
                    <td>${record.loginName}</td>
                    <td>${record.source}</td>
                    <td>${record.ip!}</td>
                    <td>${record.device!}</td>
                    <td>${record.loginTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${record.success?string("成功", "失败")}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="6">无记录</td>
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