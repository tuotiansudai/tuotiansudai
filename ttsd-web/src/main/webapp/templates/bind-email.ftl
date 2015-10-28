<!DOCTYPE html>
<html lang="en">
<#import "macro/global.ftl" as global>
<@global.head title="邮箱提示" pageCss="${css.global}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="emailBindBox">
    <div class="emailBindTip pad-s">
        <h2 class="tl"><i class="icon-msg1"></i>绑定邮箱提示</h2>
        <p class="pad-m">您的邮箱 liuhainingvip@sina.com 已经绑定成功！</p>
        <button class="btn btn-normal" type="button">
            确定
        </button>
    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.index}">
</@global.javascript>
</body>
</html>