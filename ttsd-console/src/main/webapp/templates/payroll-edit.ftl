<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="payroll-create.js" headLab="finance-manage" sideLab="payrollCreate" title="创建工资单">

<!-- content area begin -->
<div class="col-md-10">

    <form action="/payroll-manage/payroll" method="post" class="form-horizontal form-payroll">

        <div class="form-group">
            <label class="col-sm-2 control-label">标题</label>

            <div class="col-sm-4">
                <input type="text" class="form-control payroll-title" name="title" value="" maxlength="50"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <div class="file-btn">
                    <input type="file" id="file-in">
                    重新上传发放名单
                </div>
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
<!-- content area end -->
</@global.main>