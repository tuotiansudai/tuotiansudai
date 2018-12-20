<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="loan-manage" sideLab="" title="贷后跟踪">

<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal showForm" method="post" action="/project-manage/loan/${data.loanId?c}/tail-after">
        <input type="hidden" name="id" value="${data.loanId?c}">
        <div class="form-group">
            <label class="col-sm-2 control-label">经营及财务状况: </label>

            <div class="col-sm-4">
                <input name="financeState" value="${data.financeState}" type="text" class="form-control" errormsg="经营及财务状况填写有误">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">还款能力变化: </label>

            <div class="col-sm-4">
                <input name="repayPower" value="${data.repayPower}" type="text" class="form-control" errormsg="还款能力变化填写有误">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">是否逾期: </label>

            <div class="col-sm-3 checkbox" id="estimates">
                <label>
                    <input name="isOverdue" type="radio"
                           <#if data.overdue>checked="checked"</#if>
                           value="1">是
                </label>
                <label>
                    <input name="isOverdue" type="radio"
                           <#if !data.overdue>checked="checked"</#if>
                           value="0">否
                </label>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">是否受行政处罚: </label>

            <div class="col-sm-3 checkbox" id="estimates">
                <label>
                    <input name="isAdministrativePenalty" type="radio"
                           <#if data.administrativePenalty>checked="checked"</#if>
                           value="1">是
                </label>
                <label>
                    <input name="isAdministrativePenalty" type="radio"
                           <#if !data.administrativePenalty>checked="checked"</#if>
                           value="0">否
                </label>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">资金运用情况: </label>

            <div class="col-sm-4">
                <input name="amountUsage" value="${data.amountUsage}" type="text" class="form-control" errormsg="资金运用情况填写有误">
            </div>
        </div>

        <div class="form-group">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <label class="col-sm-1 control-label"></label>
            <div class="col-sm-2">
                <button type="submit" class="btn btn-primary ">保存</button>
                <a class="btn btn-default btn-primary" href="/project-manage/loan-list">取消</a>
            </div>
        </div>
    </form>

</div>
<!-- content area end -->
</@global.main>
