<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="transfer_list.js" headLab="transfer-manage" sideLab="transfer-list" title="所有的转让项目">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" method="get" class="form-inline query-build">
        <div class="form-group">
            <label>编号</label>
            <input type="text" class="form-control" name="transferApplicationId" placeholder=""
                   value="${(transferApplicationId?string.computer)!}">
        </div>
        <div class="form-group">
            <label>日期</label>

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
            <label>转让状态</label>
            <select class="selectpicker" name="status">
                <option value="" <#if !(status??)>selected</#if>>全部</option>
                <#list transferStatusList as transferStatus>
                    <#if transferStatus.name() != 'TRANSFERABLE' && transferStatus.name() != 'NONTRANSFERABLE'>
                        <option value="${transferStatus.name()}" <#if status?? && transferStatus==status>selected</#if>>
                        ${transferStatus.getDescription()}
                        </option>
                    </#if>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>转让人</label>
            <input type="text" name="transferrerLoginName" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${transferrerLoginName!}"/>
        </div>
        <div class="form-group">
            <label>承接人</label>
            <input type="text" name="transfereeLoginName" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${transfereeLoginName!}"/>
        </div>
        <div class="form-group">
            <label>原始项目</label>
            <input type="text" class="form-control" name="loanId" placeholder=""
                   value="${(loanId?string.computer)!}">
        </div>

        <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
        <button type="reset" class="btn btn-sm btn-default btnSearch">重置</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>编号</th>
                <th>原始项目</th>
                <th>转让人</th>
                <th>转让价格(元)</th>
                <th>剩余期限</th>
                <th>转让状态</th>
                <th>转让发起时间</th>
                <th>承接人</th>
                <th>转让手续费(元)</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as transferApplication>
                <tr>
                    <td >
                        <a href="${webServer}/loan/${transferApplication.transferApplicationId}" target="_blank"><span class="text-danger">${transferApplication.transferApplicationId}</span></a>
                    </td>
                    <td >
                        <a href="${webServer}/loan/${transferApplication.loanId?string.computer}" target="_blank"><span class="text-success">${transferApplication.loanId?string.computer}</span></a>
                    </td>
                    <td>${transferApplication.transferrerLoginName!}</td>
                    <td>${transferApplication.transferAmount}</td>
                    <td>${transferApplication.leftPeriod!}</td>
                    <td>
                            <#switch transferApplication.transferStatus>
                                <#case "TRANSFERABLE">
                                    申请转让
                                    <#break>
                                <#case "TRANSFERRING">
                                    转让中
                                    <#break>
                                <#case "SUCCESS">
                                    已转让
                                    <#break>
                                <#case "CANCEL">
                                    取消转让
                                    <#break>
                                <#case "NONTRANSFERABLE">
                                    不可转让
                                    <#break>

                            </#switch>
                    </td>
                    <td>${transferApplication.transferTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${transferApplication.transfereeLoginName!}</td>
                    <td>${transferApplication.transferFee}</td>
                </tr>
                </#list>
            </tbody>

        </table>
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
                    <a href="?index=${data.index - 1}&<#if transferApplicationId??>transferApplicationId=${transferApplicationId?string.computer}&</#if>    <#if startTime??>startTime=${startTime?string('yyyy-MM-dd')}&</#if><#if endTime??>endTime=${endTime?string('yyyy-MM-dd')}&</#if><#if status??>status=${status}&</#if><#if transferrerLoginName??>transferrerLoginName=${transferrerLoginName}&</#if><#if transfereeLoginName??>transfereeLoginName=${transfereeLoginName}&</#if><#if loanId??>loanId=${loanId?string.computer}&</#if>" aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage>
                        <a href="?index=${data.index + 1}&<#if transferApplicationId??>transferApplicationId=${transferApplicationId?string.computer}&</#if>    <#if startTime??>startTime=${startTime?string('yyyy-MM-dd')}&</#if><#if endTime??>endTime=${endTime?string('yyyy-MM-dd')}&</#if><#if status??>status=${status}&</#if><#if transferrerLoginName??>transferrerLoginName=${transferrerLoginName}&</#if><#if transfereeLoginName??>transfereeLoginName=${transfereeLoginName}&</#if><#if loanId??>loanId=${loanId?string.computer}&</#if>" aria-label="Next">
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