<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.change_mobile}" pageJavascript="${js.change_mobile}" activeNav="我的账户" activeLeftNav="个人资料" title="修改手机号">
<div class="content-container create-transfer-content" id="userMessageList">
    <h4 class="column-title"><em class="tc">修改银行预留手机号</em></h4>

    <div class="mobile-container">
        <form id="payForm" action="/personal-info/change-bank-mobile" method="post">
            <ul>
                <li class="input-li"><div class="label-width">原手机号：</div><div id="originMobile">${originMobile}</div></li>
                <li class="input-li"><div class="label-width">新手机号： </div><input name="newPhone" vlaue="" id="newMobile" /></li>
                <li class="input-li error-li"><div class="label-width"> </div><div class="error-box"></div></li>
                <li class="input-li"><div class="label-width"></div><input class="btn-sub" type="submit" value="确认修改"/></li>
            </ul>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="type" value="2"/>
        </form>
    </div>
</div>

</@global.main>