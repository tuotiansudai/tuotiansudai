<#macro main pageCss pageJavascript="" title="拓天速贷登录授权">
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>${title}</title>
    <meta name="_csrf" content="${(_csrf.token)!}"/>
    <meta name="_csrf_header" content="${(_csrf.headerName)!}"/>
    <link href="${commonStaticServer}/images/favicon.ico" id="icoFavicon" rel="shortcut icon" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css" href="${css.globalFun_page!}" charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="${pageCss}" charset="utf-8"/>
</head>
<body>
    <#nested>
<script>
    window.commonStaticServer = '${commonStaticServer}';
</script>

<script src="${js.jquerydll}" type="text/javascript" defer></script>
<script src="${js.globalFun_page!}" type="text/javascript" defer></script>
<script src="${pageJavascript}" type="text/javascript" id="currentScript" defer></script>
</body>
</html>
</#macro>