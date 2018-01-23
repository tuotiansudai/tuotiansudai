<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.settings}" pageJavascript="${m_js.settings}" title="设置">

<div class="goBack_wrapper">
    设置
    <div class="go-back-container" id="goBack_applyTransfer">
        <span class="go-back"></span>
    </div>
</div>
<div class="my-account-content" id="settingBox">
    <ul class="input-list">
        <li>
            <label for="perName">支付密码</label>
            <a href="./personal-info/reset-umpay-password" class="update-payment-pwd">
                修改 <i class="fa fa-angle-right"></i>
            </a>
        </li>
        <li>
            <label for="perName">安心签</label>
            <a href="anxin-electro-sign.ftl">
                <i class="fa fa-angle-right"></i>
            </a>
        </li>
    </ul>

    <button type="button" class="btn-wap-normal next-step" id="logout">退出登录</button>
</div>

</@global.main>
