<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="bank-card.js" headLab="user-manage" sideLab="bindCard" title="换卡管理">
<!-- content area begin -->

<div class="col-md-10">
    <form action="/user-manage/bind-card" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">用户名</label>
                <input type="text" class="form-control jq-loginName" name="loginName" value="${loginName!}">
            </div>
            <button class="btn btn-primary search" type="submit">查询</button>
        </div>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>编号</th>
                <th>申请人姓名</th>
                <th>申请人手机号</th>
                <th>原银行卡（银行卡号）</th>
                <th>申请银行卡（银行卡号）</th>
                <th>申请时间</th>
                <th>状态</th>
                <th>备注</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list replaceBankCardDtoList as replace>
                <tr>
                    <td>${replace.id!}</td>
                    <td>${replace.loginName!}</td>
                    <td>${replace.mobile!}</td>
                    <td>${replace.oldCard!}</td>
                    <td>${replace.applyCard!}</td>
                    <td>${replace.loginName!}</td>
                    <td>
                        <#if replace.status == 'PASSED'>
                            换卡成功
                        <#elseif replace.status == 'UNCHECKED' || replace.status == 'APPLY'>
                            处理中
                        <#elseif replace.status == 'STOP'>
                            订单终止
                        <#else>
                            失败
                        </#if>
                    </td>
                    <td>${replace.loginName!}</td>
                    <td>
                        <a href="/user-manage/agent/${(agent.id?string('0'))!}" class="btn btn-link">终止订单</a> |
                        <a href="/user-manage/agent/${(agent.id?string('0'))!}" class="btn btn-link">添加备注</a>
                    </td>
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