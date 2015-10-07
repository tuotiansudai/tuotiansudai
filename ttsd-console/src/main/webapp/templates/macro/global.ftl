<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<#macro csrf>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</#macro>

<#macro javascript pageJavascript>
<script src="${requestContext.getContextPath()}/js/libs/config.js"></script>
<script src="${requestContext.getContextPath()}/js/libs/require.js"
        defer
        async="true"
        data-main="${requestContext.getContextPath()}/js/${pageJavascript}"></script>
</#macro>