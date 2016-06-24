<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="banner.js" headLab="announce-manage" sideLab="bannerMan" title="banner管理">

<div class="col-md-10">

    <form action="" method="post" class="form-horizontal form-list banner-form">

        <div class="form-group">
            <label class="col-sm-2 control-label">名称:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="name" placeholder="" value="<#if banner??>${banner.name!}</#if>" datatype="*" errormsg="名称不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">大图:</label>

            <div class="col-sm-4 webImageUrl">
                <input type="file" imageWidth="1920" imageHeight="350"/>
            </div>
            图片必须是1920px * 350px
            <div class="col-sm-4 ">
                <input type="hidden" name="webImageUrl" class="form-control banner-webImageUrl" placeholder="" datatype="*" errormsg="大图不能为空" >
                <div class="webImageUrlImage">

                </div>
            </div>

        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">小图:</label>

            <div class="col-sm-4 appImageUrl">
                <input type="file" imageWidth="750" imageHeight="340"/>
            </div>
            图片必须是750px * 340px
            <div class="col-sm-4 ">
                <input type="hidden" name="appImageUrl" class="form-control banner-appImageUrl" placeholder="" datatype="*" errormsg="小图不能为空" >
                <div class="appImageUrlImage">

                </div>
            </div>

        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">链接:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="url" placeholder="" value="<#if banner??>${banner.url!}</#if>" datatype="*" errormsg="链接不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">分享后标题:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="title" placeholder="" value="<#if banner??>${banner.sharedUrl!}</#if>" datatype="*" errormsg="分享后标题不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">分享后描述:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="content" placeholder="" datatype="*" errormsg="分享后描述不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">分享后链接:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="sharedUrl" placeholder="" datatype="*" errormsg="分享后链接不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">终端:</label>
            <div class="col-sm-4">
            <#list sources as source>
                <input type="checkbox" name="source" class="source" value="${source.name()}">${source.name()}
            </#list>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">属性:</label>
            <div class="col-sm-4">
                <input type="checkbox" name="authenticated" >登录后可见
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">顺序:</label>
            <div class="col-sm-1">
                <input type="text" class="form-control order" name="order" placeholder="" datatype="*" errormsg="顺序不能为空">
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
            <label  class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave">上线</button>
            </div>
        </div>
        </@security.authorize>

    </form>

</div>

</@global.main>