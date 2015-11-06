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
        <form action="/personal-info" method="get">
            <#if email?has_content>
                <p class="pad-m">您的邮箱 ${email}已经绑定成功！</p>
                <button class="btn btn-normal" type="submit" >
                    确定
                </button>
            <#else>
                <p class="pad-m">您的邮箱验证失败！请重新验证 </p>
                <button class="btn btn-normal" type="submit">
                    确定
                </button>
            </#if>
        </form>


    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.index}">
</@global.javascript>
</body>
</html>