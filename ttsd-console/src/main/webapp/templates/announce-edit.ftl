<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="announce-edit.js" headLab="announceMan" sideLab="announceMan" title="发布公告">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal jq-form">
            <input type="hidden" class="jq-id" value="${(announce.id?string('0'))!}">

            <div class="form-group">
                <label class="col-sm-1 control-label">标题: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control jq-title" placeholder="" datatype="*" errormsg="标题不能为空"
                           <#if announce??>value="${announce.title!}"</#if>>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">项目描述: </label>

                <div class="col-sm-10">
                    <script id="editor"
                            type="text/plain"><#if announce??>${announce.content!}</#if></script>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">属性: </label>

                <div class="col-sm-4">
                    <div class="checkbox jq-checkbox">
                        <label>
                            <input type="checkbox" class="jq-index"
                                   <#if announce?? && announce.showOnHome>value="1"
                                   checked<#else>value="0"</#if>>
                            首页
                        </label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"></label>

                <div class="col-sm-4 form-error">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">操作: </label>

                <div class="col-sm-4">
                    <button type="button" class="btn jq-btn-form btn-primary jq-save">保存</button>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>