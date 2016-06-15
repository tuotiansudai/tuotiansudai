<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<#macro main pageCss="" pageJavascript=""  title="拓天速贷" >
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>${title}</title>
    <link href="${staticServer}/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}${pageCss}" charset="utf-8" />
    </#if>
</head>
<body>
    <script>
        var staticServer = '${staticServer}';
    </script>
    <script src="${staticServer}${jsPath}${js.config}" type="text/javascript" charset="utf-8"></script>

    <#if pageJavascript?? && pageJavascript?length gt 0>
    <script src="${staticServer}/api/js/libs/require-2.1.20.min.js" type="text/javascript" charset="utf-8" defer="defer" async="async"
            data-main="${staticServer}${jsPath}${pageJavascript}">

    </script>
    </#if>
    <div class="main-frame full-screen clearfix">
        <#nested>
    </div>

</body>
</html>
</#macro>