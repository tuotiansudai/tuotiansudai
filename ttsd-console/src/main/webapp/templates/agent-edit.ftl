<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="agent-edit.js" headLab="user-manage" sideLab="agentMan" title="用户管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal jq-form" action="<#if edit??>/user-manage/agent/edit<#else >/user-manage/agent/create</#if>" method="post">

            <div class="form-group">
                <label class="col-sm-1 control-label">代理人: </label>

                <div class="col-sm-4">
                    <input type="hidden" id = "id" name="id" <#if agent??>value="${agent.id!}"</#if> />
                    <input type="text" name="loginName" <#if agent??>value="${agent.loginName!}"</#if> class="form-control jq-loginName  <#if edit??>hide-int</#if> " placeholder="" datatype="*" errormsg="代理人不能为空"
                           <#if edit??>readonly</#if>/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">代理层级: </label>

                <div class="col-sm-4">
                    <input type="text" name="level" <#if agent??>value="${agent.level!}"</#if> class="form-control jq-level <#if edit??>hide-int</#if>" placeholder="" datatype="*" errormsg="代理层级不能为空"
                           <#if edit??>readonly</#if>/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">收益比例: </label>

                <div class="col-sm-4">
                    <input type="text" name="rate" <#if agent??>value="${agent.rate!}"</#if> class="form-control jq-rate" placeholder="" datatype="*" errormsg="收益比例不能为空"/>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="form-group">
                <label class="col-sm-1 control-label"></label>
                <div class="col-sm-4 form-error">
                    <#if errorMessage?has_content>
                        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button><span class="txt">创建失败：${errorMessage!}</span></div>
                    </#if>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">操作: </label>

                <div class="col-sm-4">
                    <button type="button" class="btn jq-btn-form btn-primary jq-save">保存</button>
                    <button type="reset" class="btn jq-btn-form btn-primary jq-reset">重置</button>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>