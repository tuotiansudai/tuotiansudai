<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="bank.js" headLab="finance-manage" sideLab="bankMange" title="银行卡编辑">

<div class="col-md-10">

    <form action="" method="post" class="form-horizontal form-list bank-form">
        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">名称:</label>
            <#if bank??>
                <input type="hidden" class="jq-id" name="id" value="${(bank.id?string('0'))!}">
            </#if>
            <#if bank??>
                <input type="hidden" class="jq-id" name="isBank" value="${bank.isBank?string("true","false")}">
            </#if>
            <div class="col-sm-4">
                <span class="info-text">
                    <#if bank??>${bank.name!}</#if>
                </span>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">简称:</label>

            <div class="col-sm-4 ">
                <span class="info-text">
                    <#if bank??>${bank.bankCode!}</#if>
                </span>
            </div>

        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">顺序:</label>

            <div class="col-sm-4 ">
                <div class="int-item">
                    <input type="text" class="form-control seq" id="seq" name="seq" datatype="n" value="<#if bank??>${bank.seq!}</#if>" errormsg="顺序只能为数字,按照从小到大排序">
                    <span>(数字越小排序越靠前)</span>
                </div>
            </div>

        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">快捷支付限额:</label>
            <div class="col-sm-4">
                <div class="int-item">
                    <span>单笔不得超过:</span>
                    <input type="text" class="form-control singleAmount" id="singleAmount" name="singleAmount" datatype="money_fl" value="<#if bank??>${(bank.singleAmount/100)?string('0.00')}</#if>" errormsg="单笔金额不能小于0">
                    <span>元</span>
                </div>
                <div class="int-item">
                    <span>单日不得超过:</span>
                    <input type="text" class="form-control singleDayAmount" id="singleDayAmount" name="singleDayAmount" datatype="money_fl" value="<#if bank??>${(bank.singleDayAmount/100)?string('0.00')}</#if>" errormsg="单日金额不能小于0">
                    <span>元</span>
                </div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <@security.authorize access="hasAnyAuthority('ADMIN','OPERATOR','OPERATOR_ADMIN')">
                    <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave">发布</button>
                </@security.authorize>
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnBack">返回</button>
            </div>
        </div>

    </form>

</div>

</@global.main>