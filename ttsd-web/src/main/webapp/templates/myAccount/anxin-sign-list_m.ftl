<#import "../macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.anxin_sign}" pageJavascript="${m_js.anxin_sign}" title="安心签">
<input type="hidden" data-skip-auth="${anxinProp.skipAuth?c}" id="switchStatus">
<div class="goBack_wrapper">
    安心签
    <div class="go-back-container" id="lastPage">
        <span class="go-back"></span>
    </div>
</div>
<ul class="list">
    <li class="item">
        <span class="item_text">安心签电子签章服务</span>
        <span class="icon_wrapper">
            <span class="icon"></span>
            已授权
        </span>
    </li>
    <li class="item">
        <span class="item_text">安心签免短信授权服务</span>
        <span id="switch" class="" themeColor="#FF473C"></span>
    </li>
</ul>
<div class="spacing_wrapper"></div>
<div class="anxin-electro-sign anxin-electro-sign1">
    <div class="cfca-info">
        安心签是由中国金融认证中心（CFCA）为拓天速贷出借用户提供的一种电子缔约文件在线签署、存储和管理服务的平台功能。它形成的电子缔约文件符合中国法律规定，与纸质文件具有同样的法律效力。
    </div>

    <div class="cfca-advantage">
    <span>
        <i></i>
        保密性
    </span>
        <span>
        <i></i>
        不可篡改性
    </span>
        <span>
        <i></i>
        可校验性
    </span>
    </div>
</div>

<div id="freeSuccess"  style="display: none">
    <div class="success-info-tip">
        <div class="pop_title">温馨提示</div>
        <div class="pop_content">关闭免短信授权服务后，您每次交易均需进行短信验证码授权，确认关闭？</div>
    </div>
</div>
<div class="shade_mine" style="display: none"></div>

</@global.main>