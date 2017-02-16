<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" activeLeftNav="" title="登录拓天速贷_拓天速贷" keywords="拓天速贷,拓天会员,新手理财,拓天速贷用户" description="拓天速贷为投资理财人士提供规范、安全、专业的互联网金融信息服务,让您获得稳定收益和高收益的投资理财产品.">
<div>

    <div class="success-container">
        <p>
            <i class="success-icon"></i>
        </p>

        <p> 提交成功！</p>
        <#if "ptp_mer_bind_card" == service>
            <p>3秒后将自动返回个人资料，如果没有跳转，您可以 <a href="/personal-info">点击这里</a></p>
            <p>您还可以 <a href="/recharge">去充值</a></p>
        </#if>

        <#if "ptp_mer_replace_card" == service>
            <p>您的换卡申请已提交，换卡申请最快两小时处理完成。</p>
            <p>3秒后将自动返回个人资料，如果没有跳转，您可以 <a href="/personal-info">点击这里</a></p>
        </#if>

        <p class="fix-nav">如有其它疑问请致电客服 400-169-1188（服务时间：9:00-20:00）</p>
    </div>

</div>
</@global.main>