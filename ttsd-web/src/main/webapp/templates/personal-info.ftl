<!DOCTYPE html>
<html lang="en">
<#import "macro/global.ftl" as global>
<@global.head title="个人资料" pageCss="${css.global}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="mainFrame AccountOverview">
    <aside class="menuBox fl">
        <ul class="menu-list">
            <li><a href="javascript:" class="active">账户总览</a></li>
            <li><a href="javascript:">投资记录</a></li>
            <li><a href="javascript:">债权转让</a></li>
            <li><a href="javascript:">资金管理</a></li>
            <li><a href="javascript:">个人资产</a></li>
            <li><a href="javascript:" >自动投标</a></li>
            <li><a href="javascript:">积分红包</a></li>
            <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>
    <div class="contentContainer fr autoHeight">
        <h4><em class="tc">个人资料</em></h4>
    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.index}">
</@global.javascript>
</body>
</html>