<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="payroll-list.js" headLab="finance-manage" sideLab="payroll" title="用户资金发放">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" method="get" class="form-inline query-build">
        <div class="form-group">
            <label>创建时间:</label>
            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="createStartTime"
                       value="${(createStartTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="createEndTime"
                       value="${(createEndTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>

        <div class="form-group">
            <label for="control-label">发放总金额:</label>
            <input type="text" class="form-control jq-balance-min" name="amountMin" value="${amountMin!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">-
            <input type="text" class="form-control jq-balance-max" name="amountMax" value="${amountMax!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label>发放时间:</label>
            <div class='input-group date' id='datetimepicker3'>
                <input type='text' class="form-control" name="sendStartTime"
                       value="${(sendStartTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker4'>
                <input type='text' class="form-control" name="sendEndTime"
                       value="${(sendEndTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>

        <div class="form-group">
            <label for="control-label">标题:</label>
            <input type="text" class="form-control jq-loginName" name="title" value="${title!}">
        </div>

        <div class="form-group">
            <label>发放状态:</label>
            <select class="selectpicker" name="sendStatus">
                <option value="" >全部</option>
            <#--<#list investStatusList as status>-->
            <#--<option value="${status}" <#if investStatus?? && status==investStatus>selected</#if>>-->
            <#--${status.description}-->
            <#--</option>-->
            <#--</#list>-->
            </select>
        </div>



        <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>


    </form>
    <button data-payroll-id="1" data-remark="qwe" class="btn btn-sm btn-primary btnRemark">备注</button>

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

            </tbody>

        </table>
    </div>

    <div class="modal fade" id="remarkModal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" >备注</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="id" name="id">
                    <form action="" method="post" id="remarkForm">
                        <textarea type="text" id="remark" name="remark" class="form-control" STYLE="height: 100px; resize: none"></textarea>
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
    <#--<nav class="pagination-control">-->
        <#--<div>-->
            <#--<span class="bordern">总共${data.count}条, 每页显示${data.pageSize}条</span>-->
        <#--</div>-->
        <#--<#if data.records?has_content>-->
            <#--<ul class="pagination pull-left">-->
                <#--<li>-->
                    <#--<#if data.hasPreviousPage >-->
                    <#--<a href="?index=${data.index - 1}&<#if loanId??>loanId=${loanId?string.computer}&</#if><#if mobile??>mobile=${mobile}&</#if><#if startTime??>startTime=${startTime?string('yyyy-MM-dd')}&</#if><#if endTime??>endTime=${endTime?string('yyyy-MM-dd')}&</#if><#if investStatus??>investStatus=${investStatus}&</#if><#if channel??>channel=${channel}&</#if><#if source??>source=${source}&</#if><#if role??>role=${role}&</#if><#if selectedPreferenceType??>usedPreferenceType=${selectedPreferenceType.name()}</#if>"-->
                       <#--aria-label="Previous">-->
                    <#--<#else>-->
                    <#--<a href="#" aria-label="Previous">-->
                    <#--</#if>-->
                    <#--<span aria-hidden="true">&laquo; Prev</span>-->
                <#--</a>-->
                <#--</li>-->
                <#--<li><a>${data.index}</a></li>-->
                <#--<li>-->
                    <#--<#if data.hasNextPage>-->
                    <#--<a href="?index=${data.index + 1}&<#if loanId??>loanId=${loanId?string.computer}&</#if><#if mobile??>mobile=${mobile}&</#if><#if startTime??>startTime=${startTime?string('yyyy-MM-dd')}&</#if><#if endTime??>endTime=${endTime?string('yyyy-MM-dd')}&</#if><#if investStatus??>investStatus=${investStatus}&</#if><#if channel??>channel=${channel}&</#if><#if source??>source=${source}&</#if><#if role??>role=${role}&</#if><#if selectedPreferenceType??>usedPreferenceType=${selectedPreferenceType.name()}</#if>"-->
                       <#--aria-label="Next">-->
                    <#--<#else>-->
                    <#--<a href="#" aria-label="Next">-->
                    <#--</#if>-->
                    <#--<span aria-hidden="true">Next &raquo;</span>-->
                <#--</a>-->
                <#--</li>-->
            <#--</ul>-->
            <#--<@security.authorize access="hasAnyAuthority('DATA')">-->
                <#--<button class="btn btn-default pull-left down-load" type="button">导出Excel</button>-->
            <#--</@security.authorize>-->
        <#--</#if>-->
    <@security.authorize access="hasAnyAuthority('DATA')">
        <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
    </@security.authorize>
    <#--</nav>-->
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>