<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.personal_profile}" pageJavascript="${m_js.personal_profile}" title="个人资料">

<div class="my-account-content personal-profile">
    <div class="m-header"><em class="icon-left" id="goBackIcon"><i></i></em>个人资料</div>
    <ul class="input-list">
        <li>
            <label for="perMobile">手机号码</label> <input type="text" id="perMobile" value="${mobile}" readonly>
        </li>
        <li>
            <#if userName??>
                <label for="perName">实名认证</label> <input type="text" id="perName" value="${userName}(${identityNumber?replace("^(\\d{6}).*(\\d{3}.)$","$1***$2","r")})" readonly>
            <#else>
                <label for="perName">实名认证</label> <input type="text" id="toPerName" value="未认证" readonly><i class="fa fa-angle-right"></i>
            </#if>

        </li>
        <li>
            <label for="perCard">银行卡</label>
            <span>
                <#if bankCard??>
                    <input type="text" id="perCard" value="${bankName}(尾号${bankCard[(bankCard?length - 4)..]})" readonly>
                <#else >
                    <input type="text" id="perCardUnboundCard" value="未绑卡" readonly><i class="fa fa-angle-right"></i>
                </#if>
            </span>

        </li>

    </ul>
    <#if bankCard??>
    <span class="manage-note">换卡操作请前往APP或者PC端进行操作</span>
    </#if>
</div>

</@global.main>


