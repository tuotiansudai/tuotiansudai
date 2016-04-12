<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.full_screen}" pageJavascript="${js.about_us}" activeNav="" activeLeftNav="" title="页面出错了">
<div class="bg-w tc" id="errorContainer">

    <div class="error-tip-container tl">
        <div class="error-info">
            <img src="${staticServer}/images/icons/error-tip-404.png" class="fl error"/>

            <p>
                <span class="sorry">很抱歉，您访问的页面不存在！</span>
                <span class="jump-tip">10秒后将跳转到首页</span>
                <a href="/">返回上一步</a> ｜<a href="/">拓天速贷首页</a>
            </p>
        </div>
        <div class="bRadiusBox clearfix clear-blank-m pad-m">

            <span class="text"> 如果还有问题，请联系客服</span><br/>
            <span class="phone"><i class="fa fa-phone"></i> 400-169-1188<em> (服务时间：9:00-20:00)</em></span><br/>
            <span class="email"><i class="fa fa-envelope-o fa-fw"></i> kefu@tuotiansudai.com</span>
        </div>
    </div>
</div>
</@global.main>