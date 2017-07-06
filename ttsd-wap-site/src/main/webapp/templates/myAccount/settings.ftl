<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'settings' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.settings}" pageJavascript="${js.settings}" title="设置">


<div class="my-account-content" id="settingBox">
    <ul class="input-list">
        <li>
            <label for="perMobile">登录密码</label>
            <a href="edit-login-password.ftl" class="update-login-pwd"> 修改 <i class="fa fa-angle-right"></i></a>

        </li>
        <li>
            <label for="perName">支付密码</label>
            <a href="edit-payment-password.ftl" class="update-payment-pwd">
                修改 <i class="fa fa-angle-right"></i>
            </a>
        </li>
        <li>
            <label for="perCard">免密支付</label>
            <span class="update-payment-nopwd opened">
                <i></i>
            </span>
        </li>
        <li>
            <label for="perName">安心签</label>
            <span>
                <i class="fa fa-angle-right"></i>
            </span>
        </li>


    </ul>

    <button type="button" class="btn-wap-normal next-step" >退出登录</button>


    <div class="tip-to-close pad" style="display: none">
        <em class="icon-notice"></em>
        <span>
            免密支付可以帮助你<br/>
            在投资时快速购买标的；<br/>
            您是否确认关闭免密支付？
        </span>

    </div>

    <div class="tip-closed pad" style="display: none">
        <em class="icon-success"></em>
        免密支付已关闭
    </div>

</div>
</@global.main>
