<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'sign_enter_point' >

<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/${jsName}.css"}>

<@global.main pageCss="${css.sign_enter_point}" pageJavascript="${js.sign_enter_point}"  title="拓天速贷-互联网金融信息服务平台" keywords="拓天速贷,互联网金融平台,P2P理财,拓天借贷,网络理财" description="拓天速贷是基于互联网的金融信息服务平台,由拓天伟业(北京)资产管理有限公司旗下的拓天伟业(北京)金融信息服务有限公司运营.">

<div class="sign-container" id="weChatStartContainer">
    <div class="logo-note" id="logoNote">
        <i class="logo"></i>
       <span>注册即拿6888元体验金+668元现金红包！</span>
    </div>

    <form id="EntryPointForm" action="/entry-point?redirect=${redirect!('/')}" method="post">
        <input validate type="text" name="mobile" value="${mobile!}" placeholder="请输入手机号" maxlength="11"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <span class="show-mobile"></span>

        <button type="submit" class="btn-wap-normal next-step" disabled>下一步</button>
    </form>

</div>
</@global.main>