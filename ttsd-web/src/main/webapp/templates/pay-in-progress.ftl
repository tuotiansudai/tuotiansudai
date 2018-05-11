<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="成功">
<div class="callBack_container" id="bankResult">
    <div class="loading-wrap">
        <div class="loading"></div>
        <p>处理中...<span class="count-down">5</span></p>
    </div>
    <form id="isPaySuccess" action="${requestContext.getContextPath()}/callback/${bankCallbackType.name()}/order-no/${orderNo}/is-success" method="post" style="display: none">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
</@global.main>