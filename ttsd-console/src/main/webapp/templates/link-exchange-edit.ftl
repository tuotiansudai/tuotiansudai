<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="linkexchange-edit.js" headLab="content-manage" sideLab="linkExchangeMan" title="添加友链">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal jq-form">
            <input type="hidden" class="jq-id" value="${(linkExchange.id?string('0'))!}">

            <div class="form-group">
                <label class="col-sm-1 control-label">标题: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control jq-title" placeholder="" datatype="*" errormsg="标题不能为空"
                           <#if linkExchange??>value="${linkExchange.title!}"</#if>>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">链接: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control jq-linkurl" placeholder="" datatype="*" errormsg="链接地址不能为空"
                           <#if linkExchange??>value="${linkExchange.linkUrl!}"</#if>>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">noFollow: </label>

                <div class="col-sm-4">
                    <input type="checkbox" class="jq-noFollow" name="noFollow"
                           <#if ((linkExchange.noFollow)!true)>value="true" checked="checked"
                           <#else>value="false"</#if>>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">显示位置: </label>
                <div class="col-sm-4">
                    <#list webSiteTypeList as webSiteType>
                        <input type="checkbox" class="jq-webSiteType" name="webSiteTypes"
                               <#if linkExchange?? && linkExchange.webSiteTypes?contains(webSiteType.name())>checked="checked"</#if>
                               value="${webSiteType.name()}">${webSiteType.name()}
                    </#list>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"></label>
                <div class="col-sm-4 form-error">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"></label>
                <div class="col-sm-4">
                    <button type="button" class="btn jq-btn-form btn-primary jq-save">发布</button>
                    <button type="button" class="btn btn-primary jq-cancel">取消</button>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>