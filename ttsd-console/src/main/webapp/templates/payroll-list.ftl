<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="payroll.js" headLab="finance-manage" sideLab="payroll" title="用户资金发放">

<!-- content area begin -->
<div class="col-md-10" xmlns="http://www.w3.org/1999/html">
    <form action="" method="get" class="form-inline query-build">
        <div class="form-group">
            <label>创建时间:</label>
            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="createStartTime"

                       value="${(payrollQueryDto.createStartTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="createEndTime"

                       value="${(payrollQueryDto.createEndTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>

        <div class="form-group">

            <label for="control-label">发放总金额(分):</label>
            <input type="text" class="form-control jq-balance-min" id="amountMin" name="amountMin"
                   value="${(payrollQueryDto.amountMin?string("0"))!}" onblur="this.value=this.value.replace(/\D/g,'')"
                   onkeyup="this.value=this.value.replace(/\D/g,'')"
                   onafterpaste="this.value=this.value.replace(/\D/g,'')">-
            <input type="text" class="form-control jq-balance-max" id="amountMax" name="amountMax"
                   value="${(payrollQueryDto.amountMax?string("0"))!}" onblur="this.value=this.value.replace(/\D/g,'')"
                   onkeyup="this.value=this.value.replace(/\D/g,'')"
                   onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label>发放时间:</label>
            <div class='input-group date' id='datetimepicker3'>
                <input type='text' class="form-control" name="sendStartTime"
                       value="${(payrollQueryDto.sendStartTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker4'>
                <input type='text' class="form-control" name="sendEndTime"
                       value="${(payrollQueryDto.sendEndTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>

        <div class="form-group">
            <label for="control-label">标题:</label>
            <input type="text" class="form-control jq-loginName" name="title" value="${payrollQueryDto.title!}">
        </div>

        <div class="form-group">
            <label>状态:</label>
            <select class="selectpicker" name="payrollStatusType">
                <option value="">全部</option>
                <#list payrollStatusTypes as status>
                    <option value="${status}"
                            <#if payrollQueryDto.payrollStatusType?? && payrollQueryDto.payrollStatusType==status>selected</#if>>
                    ${status.description}
                    </option>
                </#list>
            </select>
        </div>

        <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
    </form>

    <div class="form-group">
        <a href="/finance-manage/payroll-manage/create" class="btn btn-sm btn-primary">创建</a>
    </div>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>创建时间</th>
                <th>标题</th>
                <th>发放时间</th>
                <th>发放总金额(元)</th>
                <th>发放总人数</th>
                <th>状态</th>
                <th>备注</th>
                <th>编辑</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as payroll>
                <tr>
                    <td>${payroll.createdTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${payroll.title!}</td>
                    <td>
                        <#if payroll.grantTime??>
                        ${(payroll.grantTime?string('yyyy-MM-dd HH:mm:ss'))!}
                        <#else >
                            --
                        </#if>
                    </td>
                    <td>${(payroll.totalAmount/100)?string('0.00')}</td>
                    <td>${payroll.headCount!}</td>
                    <td>
                        <#list payrollStatusTypes as status>
                            <#if payroll.status == status>
                        ${status.description}
                        </#if>
                        </#list>
                    </td>
                    <td style="text-align:left;" width="160">
                        <#if payroll.remark??>
                            <span class="tooltip-list"
                                <#if payroll.remark?length gt 20 && payroll.remark?contains('|')>
                                  data-original-title="${payroll.remark?replace('|','—————————————————')!}">${(payroll.remark?replace('|',''))?substring(0,20)!}
                                    ...
                                <#elseif payroll.remark?length gt 20 && !payroll.remark?contains('|')>
                                    data-original-title="${payroll.remark!}">${payroll.remark?substring(0,20)!}...
                                <#elseif payroll.remark?length lt 20 && payroll.remark?contains('|')>
                                    data-original-title="${payroll.remark?replace('|','—————————————————')!}
                                    ">${(payroll.remark?replace('|',' '))!}
                                <#else>
                                    data-original-title="${payroll.remark!}">${payroll.remark!}
                                </#if>
                            </span>
                        </#if>
                    </td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('ADMIN','OPERATOR')">
                            <#if payroll.status!='REJECTED'>
                                <a href="/finance-manage/payroll-manage/${payroll.id?string('0')}/detail"
                                   class="btn btn-sm btn-primary">详情</a>
                            <#else >
                                <a href="/finance-manage/payroll-manage/edit/${payroll.id?string('0')}"
                                   class="btn btn-sm btn-primary">编辑</a>
                            </#if>
                        </@security.authorize>
                        <@security.authorize access="not hasAnyAuthority('ADMIN','OPERATOR')">
                            <a href="/finance-manage/payroll-manage/${payroll.id?string('0')}/detail"
                               class="btn btn-sm btn-primary">详情</a>
                        </@security.authorize>

                        <button data-payroll-id="${payroll.id?string('0')}"
                                class="btn btn-sm btn-primary btnRemark">备注
                        </button>
                    </td>
                </tr>
                <#else>
                <tr>
                    <td colspan="18">Empty</td>
                </tr>
                </#list>

            </tbody>

        </table>
    </div>

    <div class="modal fade" id="remarkModal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">备注</h4>
                </div>
                <div class="modal-body">
                    <form action="/payroll-manage/update-remark" method="post" id="remarkForm">
                        <input type="hidden" id="id" name="id">
                        <textarea type="text" id="remark" name="remark" class="form-control"
                                  STYLE="height: 100px; resize: none"></textarea>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button class="btn btn-default btnSubmit">确认</button>
                </div>
            </div>
        </div>
    </div>
    <!-- pagination  -->
    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${data.count}条, 每页显示${data.pageSize}条</span>
        </div>
        <#if data.records?has_content>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                    <a href="?index=${data.index - 1}&createStartTime=${(payrollQueryDto.createStartTime?string('yyyy-MM-dd'))!}&createEndTime=${(payrollQueryDto.createEndTime?string('yyyy-MM-dd'))!}&amountMin=${(payrollQueryDto.amountMin?string("0"))!}&amountMax=${(payrollQueryDto.amountMax?string("0"))!}&sendStartTime=${(payrollQueryDto.sendStartTime?string('yyyy-MM-dd'))!}&sendEndTime=${(payrollQueryDto.sendEndTime?string('yyyy-MM-dd'))!}&title=${payrollQueryDto.title!}&payrollStatusType=${payrollQueryDto.payrollStatusType!}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage>
                    <a href="?index=${data.index + 1}&createStartTime=${(payrollQueryDto.createStartTime?string('yyyy-MM-dd'))!}&createEndTime=${(payrollQueryDto.createEndTime?string('yyyy-MM-dd'))!}&amountMin=${(payrollQueryDto.amountMin?string("0"))!}&amountMax=${(payrollQueryDto.amountMax?string("0"))!}&sendStartTime=${(payrollQueryDto.sendStartTime?string('yyyy-MM-dd'))!}&sendEndTime=${(payrollQueryDto.sendEndTime?string('yyyy-MM-dd'))!}&title=${payrollQueryDto.title!}&payrollStatusType=${payrollQueryDto.payrollStatusType!}"
                       aria-label="Next">
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