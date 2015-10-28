<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="自动投标" pageCss="${css.global}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="mainFrame autoInvest">
    <aside class="menuBox fl">
        <ul class="menu-list">
            <li><a href="javascript:">账户总览</a></li>
            <li><a href="javascript:">投资记录</a></li>
            <li><a href="javascript:">债权转让</a></li>
            <li><a href="javascript:">资金管理</a></li>
            <li><a href="javascript:">个人资产</a></li>
            <li><a href="javascript:" class="active">自动投标</a></li>
            <li><a href="javascript:">积分红包</a></li>
            <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>
    <div class="contentContainer fr autoHeight">
        <h4 class="columnTitle"><em class="tc">自动投标</em></h4>

        <div class="pad-s">
            <div class="CertifiedImg"></div>

            <p class="notice">
                <b>注意事项：</b> <br/>
                1. 设置并保存后，将在次日零点开启自动投标。<br/>
                2. 自动投标开启后，若有多个项目可投，将尽可能优先选择可使用投资券的项目，投资券优先选择面值大的、快到期的。<br/>
                3. 自动投标开启后，如果不想自动投标，请手动关闭。<br/>
                4. 根据您的设置，筛选并自动为您投资，但我们不能保证对所有的项目投资成功。<br/>
                5. 用户开通自动投标功能即视为委托拓天速贷平台与达到用户指定标准的借款人签署借款合同，并承担该合同项下的一切权利
                及义务。<br/>
                6. 如果您同意开通自动投标请前往联动优势托管平台进行授权，立即享受自动投标功能。<br/>
            </p>

            <div class="btnBox tc">
                <form action="/recharge" method="post" target="_blank">
                    <button type="submit" id="btnAuthority" class="btn btn-normal">立即授权</button>
                </form>
            </div>
        </div>

    </div>
</div>

<div class="auto-invest pad-m tc" style="display: none;">
    <button id="finishAuthor" class="btn btn-normal">已完成授权</button>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.autoInvest}">
</@global.javascript>
</body>
</html>