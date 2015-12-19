<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="agent.js" headLab="user-manage" sideLab="agentMan" title="用户管理">
<!-- content area begin -->

<div class="col-md-10">
    <form action="/user-manage/agents" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">用户名</label>
                <input type="text" class="form-control jq-loginName" name="loginName" value="${loginName!}">
            </div>
            <button class="btn btn-primary search" type="submit">查询</button>
            <a class="btn btn-default pull-right btn-add-agent-rate" href="/user-manage/agent/create">添加代理人层级/收益比例</a>
        </div>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>代理人</th>
                <th>代理层级</th>
                <th>收益比例(%)</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list agentLevelRateList as agent>
                <tr>
                    <td>${agent.loginName!}</td>
                    <td>${agent.level!}</td>
                    <td>${(agent.rate * 100)?string('0.00')!}</td>
                    <td><a href="/user-manage/agent/${(agent.id?string('0'))!}" class="btn btn-link"> 编辑</a>
                        | <a href="/user-manage/agent/delete/${(agent.id?string('0'))!}" class="btn btn-link jq-delete"
                             data-id="${(agent.id?string('0'))!}">删除</a></td>
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