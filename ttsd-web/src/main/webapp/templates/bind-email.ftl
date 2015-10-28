<!DOCTYPE html>
<html lang="en">
<#import "macro/global.ftl" as global>
<@global.head title="邮箱提示" pageCss="${css.index}">
</@global.head>
<body>
<#include "header.ftl" />
    bind-email
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.index}">
</@global.javascript>
</body>
</html>