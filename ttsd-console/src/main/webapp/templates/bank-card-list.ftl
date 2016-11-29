<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="bank-card.js" headLab="user-manage" sideLab="bindCard" title="换卡管理">
<!-- content area begin -->

<div class="col-md-10">
    <form action="/bank-card-manager/bind-card" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">手机号</label>
                <input type="text" class="form-control jq-loginName" name="mobile" value="${mobile!}">
            </div>
            <button class="btn btn-primary search" type="submit">查询</button>
        </div>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
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
                <tr class="<#if replace.activeStatus == 'inRecheck'>bg-danger</#if>">
                    <td>${replace.userName!}</td>
                    <td>${replace.mobile!}</td>
                    <td>${replace.oldCard!}</td>
                    <td>${replace.applyCard!}</td>
                    <td>${(replace.applyDate?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>
                        <#if replace.status == 'PASSED'>
                            换卡成功
                        <#elseif replace.status == 'UNCHECKED' || replace.status == 'APPLY'>
                            处理中
                        <#elseif replace.status == 'REJECT'>
                            终止订单
                        <#else>
                            失败
                        </#if>
                    </td>
                    <td style="text-align:left;" width="160">
                        <#if replace.remark??>
                            <span class="tooltip-list"
                                <#if replace.remark?length gt 20 && replace.remark?contains('|')>
                                  data-original-title="${replace.remark?replace('|','—————————————————')!}">${(replace.remark?replace('|',''))?substring(0,20)!}...
                                <#elseif replace.remark?length gt 20 && !replace.remark?contains('|')>
                                    data-original-title="${replace.remark!}">${replace.remark?substring(0,20)!}...
                                <#elseif replace.remark?length lt 20 && replace.remark?contains('|')>
                                    data-original-title="${replace.remark?replace('|','—————————————————')!}">${(replace.remark?replace('|',' '))!}
                                <#else>
                                    data-original-title="${replace.remark!}">${replace.remark!}
                                </#if>
                            </span>
                        </#if>
                    </td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('CUSTOMER_SERVICE')">
                            <#if replace.activeStatus == "noRecheck" && replace.status == "APPLY" >
                                <a class="stop-bank-card btn btn-link" data-active-status="${replace.activeStatus!}" data-replace-id="${replace.id?c}" data-replace-name="${replace.loginName!}" data-replace-status="${replace.status!}" >终止订单 | </a>
                            </#if>
                        </@security.authorize>
                        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                            <#if replace.activeStatus == "inRecheck">
                                <a class="audit-bank-card btn btn-link" data-active-status="${replace.activeStatus!}" data-replace-id="${replace.id?c}" data-replace-name="${replace.loginName!}" data-replace-status="${replace.status!}">审核 | </a>
                            </#if>
                        </@security.authorize>
                        <a class="replace-remark btn btn-link" data-replace-id="${replace.id?c}" >添加备注</a>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>


    <!-- 模态框（Modal） -->
    <div class="modal fade" id="update" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" id="myModalLabel">添加备注</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="replaceId" id="replaceId"/>
                    备注信息：<br/>
                    <textarea class="form-control" name="remark" id="remark" rows="3"></textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="update()">提交</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal -->
    </div>
    <!-- 模态框（Modal）end -->

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