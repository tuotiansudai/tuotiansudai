<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-payroll.js" headLab="finance-manage" sideLab="payroll" title="编辑">

<div class="col-md-10">
    <form action="/finance-manage/payroll-manage/edit" method="post" class="form-horizontal form-payroll">

        <div class="form-group">
            <label class="col-sm-2 control-label">标题</label>
            <div class="col-sm-4">
                <input type="text" class="form-control payroll-title" name="title" value="${payrollModel.title!}"
                       maxlength="20"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <div id="old-payroll-details">
                    <table class="table table-bordered">
                        <tr>
                            <td>用户姓名</td>
                            <td>用户手机号</td>
                            <td>发放金额（元）</td>
                        </tr>
                        <#list payrollDetailModels as payrollDetailModel>
                            <tr>
                                <td>${payrollDetailModel.userName!}</td>
                                <td>${payrollDetailModel.mobile!}</td>
                                <td>${(payrollDetailModel.amount/100)?string('#.##')}</td>
                            </tr>
                        </#list>
                    </table>
                </div>
                <div id="payroll-details"></div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <div class="file-btn">
                    <input type="file" id="file-in">
                    重新上传名单
                </div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" class="form-control payroll-id" name="id" value="${payrollModel.id?c}"/>
        <input type="hidden" class="form-control payroll-totalAmount" name="totalAmount"
               value="${payrollModel.totalAmount?c}"/>
        <input type="hidden" class="form-control payroll-headCount" name="headCount"
               value="${payrollModel.headCount!}"/>
        <input type="hidden" class="form-control payroll-uuid" name="uuid" value=""/>

        <div class="form-group ">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
                <#if errorMessage?has_content>
                    <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close"
                         role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                                aria-hidden="true">×</span></button>
                        <span class="txt">创建失败：${errorMessage!}</span></div>
                </#if>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <@security.authorize access="hasAnyAuthority('ADMIN','OPERATOR')">
                    <button type="button" class="btn btn-sm btn-primary" id="btnSave">提 交</button>
                </@security.authorize>
            </div>
        </div>

    </form>

</div>

</@global.main>