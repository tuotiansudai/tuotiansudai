<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.app_marketing}" pageJavascript="${js.app_marketing}"  title="新手福利_拓天新手投资_拓天速贷" >

<div class="app-marketing-container" id="shareAppContainer">
    <div class="marketing-header">
        <span class="contact">客服热线：400-169-1188（服务时间：9:00－18:00）</span>
        <span class="red-bag"><i>668元</i>红包</span>
    </div>
	<div class="share-container">
        <#include '../module/register-app.ftl' />
	</div>

    <dl class="novice-spree">
        <dt></dt>
        <dd></dd>
    </dl>

    <div class="header-download">
        <div id="closeDownloadBox" class="icon-close img-close-tip" ></div>
        <div class="img-logo-tip sprite-global-logo-tip" ></div>
        <span>APP客户端重磅来袭<br/>更便捷更安全</span>
        <a href="#" class="btn-normal fr" id="btnExperienceNow">立即体验</a>
    </div>
    <#include '../module/register-reason.ftl' />

</div>
</@global.main>