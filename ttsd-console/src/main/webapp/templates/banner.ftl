<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="banner.js" headLab="announce-manage" sideLab="bannerMan" title="banner管理">

<div class="col-md-10">

    <form action="" method="post" class="form-horizontal form-list banner-form">
        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">名称:</label>
            <#if banner??>
                <input type="hidden" class="jq-id" name="id" value="${(banner.id?string('0'))!}">
            </#if>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="name" placeholder=""
                       value="<#if banner??>${banner.name!}</#if>" datatype="*" errormsg="名称不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">大图:</label>

            <div class="col-sm-4 ">
                <input type="text" name="webImageUrl" class="form-control banner-webImageUrl" readonly placeholder=""
                       value="<#if banner??>${banner.webImageUrl!}</#if>" datatype="*" errormsg="大图不能为空">
            </div>

            <div class="col-sm-4 webImageUrl">
                <input type="file" imageWidth="1920" imageHeight="350"/>
            </div>
            <div class="col-sm-4 text-danger">
                (图片必须是1920px * 350px)
            </div>
            <div class="col-sm-4" style="margin-left: 10%;">
                <div class="webImageUrlImage">
                    <#if banner?? && banner.webImageUrl??>
                        <img style="width:100%" src="${staticServer}${banner.webImageUrl!}" alt="缩略图"/>
                    </#if>
                </div>
            </div>

        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">小图:</label>

            <div class="col-sm-4 ">
                <input type="text" name="appImageUrl" class="form-control banner-appImageUrl" readonly placeholder=""
                       value="<#if banner??>${banner.appImageUrl!}</#if>" datatype="*" errormsg="小图不能为空">
            </div>
            <div class="col-sm-4 appImageUrl">
                <input type="file" imageWidth="750" imageHeight="350"/>
            </div>
            <div class="col-sm-4 text-danger">
                (图片必须是750px * 350px)
            </div>
            <div class="col-sm-4" style="margin-left: 10%;">
                <div class="appImageUrlImage">
                    <#if banner?? && banner.appImageUrl??>
                        <img style="width:100%" src="${staticServer}${banner.appImageUrl!}" alt="缩略图"/>
                    </#if>
                </div>
            </div>

        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">WEB链接:</label>

            <div class="col-sm-4">
                <input type="text" class="form-control" name="url" placeholder=""
                       value="<#if banner??>${banner.url!}</#if>" datatype="*" errormsg="WEB链接不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">APP链接:</label>
            <div class="col-sm-4">
                <select class="selectpicker appUrl" name="appUrl">
                    <#list appUrls as url>
                        <option value="${url.path}" <#if banner?? && url.path == banner.appUrl>selected</#if>>${url.description}</option>
                    </#list>
                </select>
                <div class="app-push-link jump-to-link">定位地址:<input type="text" class="form-control jump-link-text" name="jumpToLink" <#if banner??>value="${banner.jumpToLink!}"</#if> placeholder=""  maxlength="100" datatype="*" errormsg="定位地址不能为空"></div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">分享后标题:</label>

            <div class="col-sm-4">
                <input type="text" class="form-control" name="title" placeholder=""
                       value="<#if banner??>${banner.title!}</#if>">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">分享后描述:</label>

            <div class="col-sm-4">
                <input type="text" class="form-control" name="content" placeholder=""
                       value="<#if banner??>${banner.content!}</#if>">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">分享后链接:</label>

            <div class="col-sm-4">
                <input type="text" class="form-control sharedUrl" name="sharedUrl" placeholder=""
                       value="<#if banner??>${banner.sharedUrl!}</#if>">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">终端:</label>

            <div class="col-sm-4">
                <#list sources as source>
                    <label><input type="checkbox" name="source" class="source"
                                  <#if banner?? && banner.source?seq_contains(source.name())>checked="checked"</#if>
                                  value="${source.name()}">${source.name()}
                    </label>
                </#list>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">属性:</label>

            <div class="col-sm-4">
                <input type="checkbox" name="authenticated" <#if banner?? && banner.authenticated>checked</#if>>登录后可见
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label" style="width: 10%">顺序:</label>

            <div class="col-sm-1">
                <input type="text" class="form-control order" name="order" placeholder=""
                       value="<#if banner??>${banner.order!}</#if>" datatype="*" errormsg="顺序不能为空">
            </div>
            需要在第X位展示，就输入数字X（1、2、3、4等）
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>

            <div class="col-sm-4 form-error">
            </div>
        </div>

        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
            <div class="form-group">
                <label class="col-sm-2 control-label"></label>

                <div class="col-sm-4">
                    <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave">上线</button>
                </div>
            </div>
        </@security.authorize>

    </form>

</div>

</@global.main>