<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-payroll.js" headLab="finance-manage" sideLab="payroll" title="创建">

<div class="col-md-10">
    <form action="/finance-manage/payroll-manage/create" method="post" class="form-horizontal form-payroll">

        <div class="form-group">
            <label class="col-sm-2 control-label">标题</label>
            <div class="col-sm-4">
                <input type="text" class="form-control payroll-title" name="title" value="" maxlength="20"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <div id="payroll-details"></div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <div class="file-btn">
                    <input type="file" id="file-in">
                    上传发放名单
                </div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" class="form-control payroll-totalAmount" name="totalAmount" value=""/>
        <input type="hidden" class="form-control payroll-headCount" name="headCount" value="" />
        <input type="hidden" class="form-control payroll-uuid" name="uuid" value="" />

        <div class="form-group ">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
                <#if errorMessage?has_content>
                    <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button><span class="txt">创建失败：${errorMessage!}</span></div>
                </#if>
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <@security.authorize access="hasAnyAuthority('OPERATOR')">
                    <button type="button" class="btn btn-sm btn-primary" id="btnSave">提 交</button>
                </@security.authorize>
            </div>
        </div>

    </form>

</div>

</@global.main>