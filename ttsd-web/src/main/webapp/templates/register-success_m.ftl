<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.sign_enter_point}" pageJavascript="${m_js.register_success}" title="注册成功">

<div class="sign-container register-success" id="weChatRegister">
<div class="goBack_wrapper">注册成功</div>
 <div class="success-tip">
     <i class="success-text">注册成功！</i>
     <span>恭喜您获得以下奖励：</span>
 </div>
    <div class="success-img">
        <span class="experience-gold"></span>
        <i class="note">您可以在“我的--优惠券”中找到体验金及红包</i>


    </div>
    <div class="success-button">
        <a class="btn-wap-normal" href="/m/">返回首页</a>
        <a class="btn-wap-normal only-border" href="/m//register/account/">实名认证</a>

        <span class="note2">在平台进行投资时，需要您进行实名认证完善信息</span>
    </div>

</div>

</@global.main>

