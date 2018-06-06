<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.bank_callback}" pageJavascript="${m_js.bank_callback}" title="个人资料">

<div class="my-account-content personal-profile">
    <div class="m-header"><em id="iconRegister" class="icon-left"><i></i></em>实名认证</div>
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-success"></div>
            <p class="desc">银行卡绑定成功</p>
        </div>

    </div>
    <div class="btn-container">
        <a href="/" class="btn-confirm">确定</a>
    </div>
<#--实名认证成功后下一步 去绑卡-->
    <form action="${requestContext.getContextPath()}/bank-card/bind/source/M" method="post" style="display: inline-block;float:right">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <span class="binding-set">
                        <i class="fa fa-times-circle no"></i>未绑定<input type="submit" class="setlink setBankCard" value="绑定" style="border: none;color: #ffac2a;cursor: pointer;font-size: 13px"/>
                    </span>
        <div class="btn-container">
            <input type="submit" class="btn-confirm btn-next"  value="下一步"/>
        </div>
    </form>
</div>

</@global.main>


