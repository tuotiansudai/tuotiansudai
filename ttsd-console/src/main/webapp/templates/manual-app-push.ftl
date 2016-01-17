<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="manual-app-push.js" headLab="app-push-manage" sideLab="createManualAppPush" title="创建手动推送">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/app-push-manage/manual-app-push" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label">通知名称:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control name"  name="name" placeholder="" <#if jPushAlert??>value="${jPushAlert.name!}"</#if> readonly datatype="*" errormsg="通知名称不能为空">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">推送类型: </label>
            <div class="col-sm-4">
                <select class="selectpicker pushType" name="pushType">
                    <#list pushTypes as pushType>
                        <option value="${pushType.name()}" <#if jPushAlert?? && jPushAlert.pushType == pushType>selected</#if> >${pushType.getDescription()}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">推送对象: </label>
            <div class="col-sm-4">
                <input type="radio"   checked name="pushObjectChoose" placeholder=""  datatype="*" >全部
                <input type="radio"   name="pushObjectChoose" placeholder=""  datatype="*" >地区
                <input type="hidden"  name="pushObjects" placeholder=""  datatype="*" value="ALL">

            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">推送渠道: </label>
            <div class="col-sm-2">
                <select class="selectpicker" name="pushSource">
                    <#list pushSources as pushSource>
                        <option <#if jPushAlert?? && jPushAlert.pushSource == pushSource>selected</#if>>${pushSource.name()}</option>
                    </#list>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">推送模板: </label>
            <div class="col-sm-4">
                <textarea rows="4" cols="58" maxlength="40" <#if jPushAlert??>value="${jPushAlert.content!}"</#if> name="content" errormsg="通知模板不能为空">

                </textarea>

            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">打开消息定位到: </label>
            <div class="col-sm-4">
                <select class="selectpicker jumpTo" name="jumpTo">
                    <#list jumpTos as jumpTo>
                        <option value="${jumpTo.name()}" <#if jPushAlert?? && jPushAlert.jumpTo == jumpTo>selected</#if>> ${jumpTo.getDescription()}</option>
                    </#list>
                </select>
                <div class="app-push-link jump-to-link">链接地址:<input type="text" class="form-control jump-link-text" name="jumpToLink" <#if jPushAlert??>value="${jPushAlert.jumpToLink}"</#if> placeholder=""  datatype="*" errormsg="链接地址不能为空"></div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
                <#if errorMessage?has_content>
                    <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button><span class="txt">创建失败：${errorMessage!}</span></div>
                </#if>
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave">保存</button>
                <button type="reset" class="btn btn-sm btn-primary btnSearch" id="btnReset">重置</button>
            </div>
        </div>
    </form>
</div>
<!-- content area end -->
</@global.main>