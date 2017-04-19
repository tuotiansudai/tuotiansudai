<#macro main pageCss pageJavascript="" >
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>拓天财富登录授权</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <link href="${commonStaticServer}/images/favicon.ico" id="icoFavicon" rel="shortcut icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${css.globalFun_page!}" charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="${css.we_start}" charset="utf-8" />
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${pageCss}" charset="utf-8"/>
    </#if>
</head>
<body>
    <#nested>
<script>
    window.staticServer='${commonStaticServer}';
</script>

<script src="${js.jquerydll}" type="text/javascript" defer></script>
<script src="${js.globalFun_page!}" type="text/javascript" defer></script>
<#--<script src="${js.we_start}" type="text/javascript" defer></script>-->
<script src="${pageJavascript}" type="text/javascript" id="currentScript" defer></script>
</body>
</html>
</#macro>