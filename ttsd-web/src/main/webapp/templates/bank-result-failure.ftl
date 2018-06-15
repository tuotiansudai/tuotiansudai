
<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="业务失败">
<div class="head-banner"></div>

    <div class="callBack_container">
        <div class="success_tip_icon failure"></div>
        <p class="my_pay_tip">${message!('业务处理失败')}</p>
        <div class="handle_btn_container">
            <form id="retry-form" action="${bankCallbackType.getRetryPath()}" method="${bankCallbackType.getMethod()}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" class="retry" style="margin-right: 0;border: none" value="再次尝试"/>
            </form>
        </div>
        <p class="phone-tip">客服电话：400-169-1188 （工作日 9:00-22:00）</p>
    </div>

</@global.main>