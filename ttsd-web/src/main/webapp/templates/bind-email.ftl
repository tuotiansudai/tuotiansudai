<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="${js.personal_info}" activeNav="我的账户" activeLeftNav="个人资料" title="个人资料">
<div class="content-container fr auto-height personal-info">
    <h4 class="column-title"><em class="tc long-title">绑定邮箱提示</em></h4>
    <div class="pad-m tc">
        <div>
            <div class="pad-s">
                <#if email??>
                    <h3 class="pad-m">您的邮箱 ${email}已经绑定成功！</h3>
                <#else>
                    <h3 class="pad-m">您的邮箱验证失败，请重新绑定！</h3>
                </#if>
                <button class="btn btn-normal redirect-to-personal-info" type="button">确定</button>
            </div>
        </div>
    </div>
</div>
</@global.main>