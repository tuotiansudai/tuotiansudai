<#import "../../macro/global-dev.ftl" as global>

<#assign jsName = 'house_decorate_2017' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>


<@global.main pageCss="${css.house_decorate_2017}" pageJavascript="${js.house_decorate_2017}" activeNav="" activeLeftNav="" title="818居家理财节_活动中心_拓天速贷" keywords="拓天速贷,818理财节,现金红包,体验金,实物奖励" description="拓天速贷818居家理财节活动,每日登录抢夺现金红包及加息劵,天降红包雨,全家抢不停,当日累计投资可获得该日投资额的2.18倍体验金奖励,以及相应的居家实物奖励.">
<div class="house-decorate-container" id="houseDecorateContainer">
    <div class="top-img">
        <img src="" width="100%" class="media-pc">
        <img src="" width="100%" class="media-phone">
    </div>
    <div class="content-item">
        <div class="wp">
            <div class="day-bag-item">
                <p>活动期间，用户每日登录，可在活动页面抢到一个“每日红包”，“每日红包”包含投资红包或加息券，金额随机。</p>
            </div>
        </div>
    </div>
    <div class="content-item even clearfix">
        <div class="wp">
            dfsdf
        </div>
    </div>
    <div class="content-item">
        <div class="wp">
            afasfasf
        </div>
    </div>
    <div class="content-item even clearfix">
        <div class="wp">
            <dl class="rule-item clearfix">
                <dt>温馨提示</dt>
                <dd>1.本活动仅限直投项目，债权转让及新手专享项目不参与累计；</dd>
                <dd>2.用户在本活动中所获所有红包及加息券奖励即时发放，用户可在PC端“我的账户”或App端“我的”中进行查看；</dd>
                <dd>3.体验金奖励将于获奖后次日发放至用户账户；</dd>
                <dd>4.实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</dd>
                <dd>5.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
                <dd>6.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</dd>
            </dl>
        </div>
    </div>
    <#include "../../module/login-tip.ftl" />
    
</div>

</@global.main>