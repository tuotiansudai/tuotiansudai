<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.personal_profile}" pageJavascript="${m_js.personal_profile}" title="个人资料">

<div class="my-account-content personal-profile">
    <div class="m-header"><em class="icon-left" id="goBackIcon"><i></i></em>个人资料</div>
    <ul class="input-list">
        <li>
            <label for="perMobile">手机号码</label> <input type="text" id="perMobile" value="${mobile}" unselectable="on" onfocus="this.blur()" readonly>
        </li>
        <li>
            <#if userName??>
                <label for="perName">实名认证</label> <input type="text" id="perName" value="${userName}(${identityNumber?replace("^(\\d{6}).*(\\d{3}.)$","$1***$2","r")})" readonly unselectable="on" onfocus="this.blur()">
            <#else>
                <label for="perName">实名认证</label> <span><input type="text" id="toPerName" value="未认证" readonly unselectable="on" onfocus="this.blur()"><i class="iconRight"></i></span>
            </#if>
        </li>
        <li>
            <label for="perCard">银行卡</label>
            <span>
                <#if bankCard??>
                    <input type="text" id="perCard" value="${bankName}(尾号${bankCard[(bankCard?length - 4)..]})" readonly unselectable="on" onfocus="this.blur()">
                <#else>
                    <input type="text" id="perCardUnboundCard" <#if userName??>data-has-name="true"</#if> value="未绑卡" readonly unselectable="on" onfocus="this.blur()"><i class="iconRight"></i>
                    <form id="bindCardForm" action="${requestContext.getContextPath()}/bank-card/bind/source/M" method="post" style="display: none">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                </#if>
            </span>

        </li>

    </ul>
    <#if bankCard??>
    <span class="manage-note">换卡操作请前往APP或者PC端进行操作</span>
    </#if>
</div>
</@global.main>


