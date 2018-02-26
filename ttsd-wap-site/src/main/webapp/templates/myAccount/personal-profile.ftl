<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'personal_profile' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.personal_profile}" pageJavascript="${js.personal_profile}" title="个人资料">

<div class="my-account-content personal-profile">
    <ul class="input-list">
        <li>
            <label for="perMobile">手机号码</label> <input type="text" id="perMobile" value="18510238729">
        </li>
        <li>
            <label for="perName">实名认证</label> <input type="text" id="perName" value="马二龙(142733***541x）" >
        </li>
        <li>
            <label for="perCard">银行卡管理</label>
            <span>
                <input type="text"  id="perCard" value="中国农业银行(尾号5875"><i class="fa fa-angle-right"></i>
            </span>

        </li>

    </ul>

    <span class="manage-note">换卡操作请前往手机端或者电脑端进行操作</span>
</div>
</@global.main>
