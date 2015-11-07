<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>发布公告</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/index.css">
    <@global.javascript pageJavascript="announce.js"></@global.javascript>
<@global.csrf></@global.csrf>
</head>
<body>
<@menu.header label="announceMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="announceMan" sideLab="announceMan"></@menu.sidebar>
            <!-- content area begin -->

            <div class="col-md-10">
                <div class="row">
                <form class="form-horizontal jq-form">
                    <input type="hidden" class="jq-id" value="${(announcementManagement.id?string('0'))!}">
                    <div class="form-group">
                        <label class="col-sm-1 control-label">标题: </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-title" placeholder="" datatype="*" errormsg="标题不能为空" <#if announcementManagement??>value="${announcementManagement.title!}"</#if>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-1 control-label">项目描述: </label>

                        <div class="col-sm-10">
                            <script id="editor" type="text/plain"><#if announcementManagement??>${announcementManagement.content!}</#if></script>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-1 control-label">属性: </label>

                        <div class="col-sm-4">
                            <div class="checkbox jq-checkbox">
                                <label>
                                    <input type="checkbox" class="jq-index" <#if announcementManagement?? && announcementManagement.showOnHome>value="1"  checked<#else>value="0"</#if>>
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
                            <button type="button" class="btn jq-btn-form btn-primary jq-save" >保存</button>
                        </div>
                    </div>
                </form>
            </div>

            </div>

            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->
</body>
</html>