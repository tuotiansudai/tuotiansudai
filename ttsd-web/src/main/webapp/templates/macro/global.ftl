<#macro head title pageCss>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="${requestContext.getContextPath()}/style/dest/${css.global}">
    <link rel="stylesheet" type="text/css" href="${requestContext.getContextPath()}/style/dest/${pageCss}">
</head>
</#macro>

<#macro javascript pageJavascript>
<script src="${requestContext.getContextPath()}/js/dest/${js.config}"></script>
<script src="${requestContext.getContextPath()}/js/libs/require-2.1.20.min.js"
        defer
        async="true"
        data-main="${requestContext.getContextPath()}/js/dest/${pageJavascript}"></script>
</#macro>