<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.personal_profile}" pageJavascript="${m_js.personal_profile}" title="个人资料">

<div class="my-account-content personal-profile">
    <div class="m-header"><em class="icon-left" id="goBackIcon"><i class="fa fa-angle-left"></i></em>个人资料</div>
    <ul class="input-list">
        <li>
            <label for="perMobile">手机号码</label> <input type="text" id="perMobile" value="${mobile}">
        </li>
        <li>
            <#if userName??>
                <label for="perName">实名认证</label> <input type="text" id="perName" value="${userName}(${identityNumber?replace("^(\\d{6}).*(\\d{3}.)$","$1***$2","r")})" >
            <#else>
                <label for="perName">实名认证</label> <input type="text" id="perName" value="未认证" >
            </#if>

        </li>
        <li>
            <label for="perCard">银行卡</label>
            <span>
                <#if bankCard??>
                    <input type="text" id="perCard" value="${bankName}(尾号${bankCard[(bankCard?length - 4)..]})">
                <#else >
                    <input type="text" id="perCardUnboundCard" value="未绑卡"><i class="fa fa-angle-right"></i>
                </#if>
            </span>

        </li>

    </ul>

    <span class="manage-note">换卡操作请前往APP或者PC端进行操作</span>
</div>

</@global.main>


