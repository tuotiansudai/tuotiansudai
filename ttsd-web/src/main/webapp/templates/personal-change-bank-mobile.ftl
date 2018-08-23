<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.refer_list}" pageJavascript="${js.refer_list}" activeNav="我的账户" activeLeftNav="推荐送现金" title="推荐管理">
<form id="payForm" action="/personal-info/change-bank-mobile" method="post">
    原手机号: <input name="originMobile" vlaue="${originMobile}"/></br>
    新手机号: <input name="newPhone" vlaue="" /></br>
    是否可用: 是-<input checked="checked" type="radio" name="type" value="1"/>
    否-<input  type="radio" name="type" value="2"/>
    </br>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit" value="tijiao"/>tijiao
</form>
</@global.main>