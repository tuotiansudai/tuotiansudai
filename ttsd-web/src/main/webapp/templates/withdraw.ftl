<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="提现" pageCss="${css.bind_card}">
</@global.head>
<body>
<#include "header.ftl" />
<form action="/withdraw" method="post" target="_blank">
    <input type="text" name="amount" value="1.00"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="submit"/>
</form>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.withdraw}">
</@global.javascript>
</body>
</html>